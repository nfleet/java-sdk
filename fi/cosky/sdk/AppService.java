package fi.cosky.sdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;

import com.google.gson.*;

import org.apache.commons.codec.binary.Base64;

public class AppService {

		private String baseUrl;
		private String ClientKey;
		private String ClientSecret;
		private boolean retry;
		public AppUserDataSet Root;	
		private String user;
		private String password;
		private static int RETRY_WAIT_TIME = 2000;
		private String appServiceUrl;
		private AppTokenData token;
		private int currentAppUserId;
		private String appUrl;
		
		static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'").create();
		
		public AppService(String appServiceUrl,String appUrl, String clientKey, String clientSecret) {
			this.baseUrl = appServiceUrl + "/appusers";
			this.retry  = true;
			this.ClientKey = clientKey;
			this.ClientSecret = clientSecret;
			this.appServiceUrl = appServiceUrl;
			this.appUrl = appUrl;
			//Delete-Verb causes connection to keep something somewhere that causes the next request to fail.
			//this hopefully helps with that.
			System.setProperty("http.keepAlive", "false");
			this.Root = getAppUserDataSet();
		} 

		
		private AppUserDataSet getAppUserDataSet() {
			AppUserDataSet set = null;
			try {
				set = navigate(AppUserDataSet.class, new Link("", "", "GET"));
			} catch (Exception e) {
				System.out.println(e);
			}
			return set;
			
		}
		/**
		 * 
		 * @param tClass return type.
		 * @param l navigation link
		 * @return object of type tClass
		 * @throws  NFleetRequestException when there is data validation problems 
		 * @throws IOException when there is problems with infrastructure (connection etc..)
		 */
		public <T extends BaseData> T navigate(Class<T> tClass, Link l) throws IOException {
			return navigate(tClass, l, null, null, null);
		}

		
		public <T extends BaseData> T navigate(Class<T> tClass, Link l, String user, String password) throws IOException {
			return navigate(tClass, l, null, user, password);
		}

		@SuppressWarnings("unchecked")
		public <T extends BaseData> T navigate(Class<T> tClass, Link l,	HashMap<String, String> queryParameters, String user, String password) throws IOException {
			Object result;
			retry = true;
						
			String uri = l.getUri();
			//check
			if (l.getMethod().equals("DELETE")) {
				result = sendRequest(l, tClass, new Object(), user, password);
			} else {
				result = sendRequest(l, tClass, null, user, password);
			}
			return (T) result;
		}

		/**
		 * Navigate method for sending data
		 * @param tClass    return type.
		 * @param l 		navigation link
		 * @param object	object to send
		 * @return 			object of type tClass
		 * @throws 	NFleetRequestException when there is problems with data validation, exception contains list of the errors.
		 * @throws	IOException  when there is problems with infrastructure ( connection etc.. )
		 */
		@SuppressWarnings("unchecked")
		public <T extends BaseData> T navigate(Class<T> tClass, Link l, Object object, String user, String password) throws IOException {
			 return (T) sendRequest(l, tClass, object, user, password);
		}

		public <T extends BaseData> T navigate(Class<T> tClass, Link l, Object object) throws IOException {
			 return (T) sendRequest(l, tClass, object, null, null);
		}
		public Link getRoot() {
			return new Link("self", baseUrl, "GET","", true);
		}

		public String getBaseUrl() {
			return baseUrl;
		}

		public void setBaseUrl(String baseUrl) {
			this.baseUrl = baseUrl;
		}

		private <T extends BaseData> T sendRequest(Link l, Class<T> tClass, Object object, String user, String password) throws IOException {
			URL serverAddress;
			BufferedReader br;
			String result = "";
			HttpURLConnection connection = null;
			String url = l.getUri().contains("://") ? l.getUri() : this.baseUrl + l.getUri();
			try {
				String method = l.getMethod();
				String type = l.getType();
				
				serverAddress = new URL(url);
				connection = (HttpURLConnection) serverAddress.openConnection();
				boolean doOutput = doOutput(method);
				connection.setDoOutput(doOutput);
				connection.setRequestMethod(method);
				connection.setInstanceFollowRedirects(false);
				
			
				connection.setRequestProperty("Content-Type", "application/json");
				connection.setRequestProperty("Accept", "application/json");
				
				if (user == null || password == null)
					connection.addRequestProperty("Authorization", "Basic " + Base64.encodeBase64String((this.ClientKey + ":" + this.ClientSecret).getBytes()));
				else
					connection.addRequestProperty("Authorization", "Basic " + Base64.encodeBase64String((user+ ":" + password).getBytes()));
							
				if (method.equals("POST") || method.equals("PUT")) {
						String json = object != null ? gson.toJson(object) : ""; //should handle the case when POST without object.
						connection.addRequestProperty("Content-Length",	json.getBytes("UTF-8").length + "");

						OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream(), "UTF-8" );

						osw.write(json);
						osw.flush();
						osw.close();		
				}

				connection.connect();

				if (connection.getResponseCode() == HttpURLConnection.HTTP_CREATED || connection.getResponseCode() == HttpURLConnection.HTTP_SEE_OTHER ) {
					ResponseData data = new ResponseData();
					Link link = parseLocationLinkFromString(connection.getHeaderField("Location"));
					link.setType(type);
					data.setLocation(link);
					connection.disconnect();
					return (T) data;
				}

				if (connection.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
					throw new NFleetUnauthorizedException();	
				}
				
				if (connection.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT) {
					return (T) new ResponseData();
				}
				
				if (connection.getResponseCode() >= HttpURLConnection.HTTP_BAD_REQUEST && connection.getResponseCode() < HttpURLConnection.HTTP_INTERNAL_ERROR) {
					System.out.println("App: ErrorCode: " + connection.getResponseCode() + " " + connection.getResponseMessage() +
										" " + url + ", verb: " + method);
					
					String errorString = readErrorStreamAndCloseConnection(connection);
					throw (NFleetRequestException) gson.fromJson(errorString, NFleetRequestException.class);
				}
				else if (connection.getResponseCode() >= HttpURLConnection.HTTP_INTERNAL_ERROR ) {
					if (retry) {
						
						System.out.println("App: Request caused internal server error, waiting "+ RETRY_WAIT_TIME + " ms and trying again.");
						System.out.println("Url was " + url);
						return waitAndRetry(connection, l, tClass, object, user, password);
					} else {
						System.out.println("App: Request caused internal server error, please contact dev@nfleet.fi");
						String errorString = readErrorStreamAndCloseConnection(connection);
						throw new IOException(errorString);
					}
				}
				
				if (connection.getResponseCode() >= HttpURLConnection.HTTP_BAD_GATEWAY) {
					if (retry) {
						System.out.println("App: Could not connect to NFleet-API, waiting "+ RETRY_WAIT_TIME + " ms and trying again.");
						return waitAndRetry(connection, l, tClass, object, user, password);
					} else {
						System.out.println("App: Could not connect to NFleet-API, please check service status from http://status.nfleet.fi and try again later.");
						String errorString = readErrorStreamAndCloseConnection(connection);
						throw new IOException(errorString);
					}
					
				}
				
				result = readDataFromConnection(connection);
				
			} catch (MalformedURLException e) {
				throw e;
			} catch (ProtocolException e) {
				throw e;
			} catch (UnsupportedEncodingException e) {
				throw e;
			} catch (IOException e) {
				throw e;
			} catch (SecurityException e) {
				throw e;
			} catch (IllegalArgumentException e) {
				throw e;
			} finally {
				assert connection != null;
				connection.disconnect();
			}
			return (T) gson.fromJson(result, tClass);
		}
	
		private String method(Verb verb) {
			switch (verb) {
				case GET:
					return "GET";
				case PUT:
					return "PUT";
				case POST:
					return "POST";
				case DELETE:
					return "DELETE";
				case PATCH:
					return "PATCH";
			}
			return "";
		}
		
		private Link parseLocationLinkFromString(String s) {
			if (!s.contains("/tokens"))
				s = s.substring(s.lastIndexOf("/"));
			return new Link("location", s, "GET", "", true);
		}

		private boolean doOutput(Verb verb) {
			switch (verb) {
			case GET:
			case DELETE:
				return false;
			default:
				return true;
			}
		}
		
		private boolean doOutput(String verb) {
			return (verb.equals("POST") || verb.equals("PUT") || verb.equals("PATCH"));
		}
		
		private enum Verb {
			GET, PUT, POST, DELETE, PATCH
		}
		
		
			
		private String readErrorStreamAndCloseConnection(HttpURLConnection connection) {
			InputStream stream = connection.getErrorStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(stream));
			StringBuilder sb = new StringBuilder();
			String s;
			try {
				while ((s = br.readLine()) != null) {
					sb.append(s);
				}
			} catch (IOException e) {
				System.out.println("App: Could not read error stream from the connection.");
			} finally {
				connection.disconnect();
			}
			return sb.toString();
		}
		
		private String readDataFromConnection(HttpURLConnection connection) {
			InputStream is = null;
			BufferedReader br = null;
			StringBuilder sb = null;
			try {
				is = connection.getInputStream();
				br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

				sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line).append("\n");
				}
				String eTag = null;
				if ((eTag = connection.getHeaderField("ETag")) != null) {
					sb.insert(sb.lastIndexOf("}"), ",\"VersionNumber\":" + eTag	+ "");
				}
			} catch (IOException e) {
				System.out.println("App: Could not read data from connection");
			} 
			return sb.toString();
		}
		
		private <T extends BaseData> T waitAndRetry(HttpURLConnection connection, Link l, Class<T> tClass, Object object, String user, String password) {
			try {
				retry = false;
				Thread.sleep(RETRY_WAIT_TIME);
				return sendRequest(l, tClass, object, user, password);
			} catch (InterruptedException e) {
				return null;
			} catch (IOException e) {
				return null;
			}
		}
		
		public boolean Login(String user, String password) throws IOException {
			Link l = new Link("signin", appServiceUrl + "/signin", "GET","" , true);
			
			try {
				this.token = navigate(AppTokenData.class, l, user, password);
				this.user = user;
				this.password = password;
				
			} catch (NFleetUnauthorizedException e) {
				System.out.println(e);
				Logout();
				return false;
			} 
			
			return true;
			
		}
		
		public boolean Login() throws IOException {
			return Login(this.user, this.password);
		}
		
		public boolean HasValidToken() {
			return true;
		}
		
		public String MakeAppUrl(String url) {
			url = url.replaceAll("/users/\\d+", "");
			return appUrl + "/#view" + url + "/plan?accesstoken=" + token.getToken();
		}
		
		public void Logout() {
			token = null;
			user = null;
			password = null;
		}


		public int getCurrentAppUserId() {
			return currentAppUserId;
		}


		public void setCurrentAppUserId(int currentAppUserId) {
			this.currentAppUserId = currentAppUserId;
		}
}

