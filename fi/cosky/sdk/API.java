package fi.cosky.sdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import com.google.gson.*;

import org.apache.commons.codec.binary.Base64;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

/**
 * API-class to handle the communication between SDK-user and NFleet
 * optimization's REST-API.
 */
public class API {
	private String baseUrl;
	private String ClientKey;
	private String ClientSecret;
	private ApiData apiData;
	private TokenData tokenData;
	private boolean timed;
	private ObjectCache objectCache;
	private boolean retry;
	private boolean useMimeTypes;
	private MimeTypeHelper helper;
	
	private static int RETRY_WAIT_TIME = 2000;
	
	static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'").create();
	
	public API(String baseUrl) {
		this.baseUrl = baseUrl;
		this.objectCache = new ObjectCache();
		this.timed = false;
		this.retry  = true;
		this.useMimeTypes = true; //change this when production will support mimetypes.
		this.helper = new MimeTypeHelper();
		
		//Delete-Verb causes connection to keep something somewhere that causes the next request to fail.
		//this hopefully helps with that.
		System.setProperty("http.keepAlive", "false");
	} 

	private boolean authenticate() {
		return authenticate(this.ClientKey, this.ClientSecret);
	}
	
	
	public boolean authenticate(String username, String password) {
		this.ClientKey = username;
		this.ClientSecret = password;
		System.out.println("Authenticating API with username: " +username + " and pass: " + password);
		try {
			ResponseData result = navigate(ResponseData.class, getAuthLink());

			if (result == null || result.getItems() != null) {
				System.out.println("Could not authenticate, please check credentials and service status from http://status.nfleet.fi");
				return false;
			}

			TokenData authenticationData = navigate(TokenData.class, result.getLocation());

			this.tokenData = authenticationData;
		} catch (Exception e) {
			System.out.println( e.toString());
			return false;
		}
		return true;
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
		return navigate(tClass, l, null);
	}

	@SuppressWarnings("unchecked")
	public <T extends BaseData> T navigate(Class<T> tClass, Link l,	HashMap<String, String> queryParameters) throws IOException {
		Object result;
		retry = true;
		long start = 0; 
		long end;
		
		if (isTimed()) {
			start = System.currentTimeMillis();
		}
		
		if (tClass.equals(TokenData.class)) {
			result = sendRequest(l, tClass, null);
			return (T) result;
		}

		if (l.getRel().equals("authenticate")) {
			HashMap<String, String> headers = new HashMap<String, String>();
			String authorization = "Basic " + Base64.encodeBase64String((this.ClientKey + ":" + this.ClientSecret).getBytes());
			headers.put("authorization", authorization);
			result = sendRequestWithAddedHeaders(Verb.POST,	this.baseUrl + l.getUri(), tClass, null, headers);
			return (T) result;
		}

		String uri = l.getUri();
		if (l.getMethod().equals("GET") && queryParameters != null	&& !queryParameters.isEmpty()) {
			StringBuilder sb = new StringBuilder(uri + "?");

			for (String key : queryParameters.keySet()) {
				sb.append(key + "=" + queryParameters.get(key) + "&");
			}
			sb.deleteCharAt(sb.length() - 1);
			uri = sb.toString();
		}

		if (l.getMethod().equals("GET") && !uri.contains(":")) {
			result = sendRequest(l, tClass, null);
		} else if (l.getMethod().equals("PUT")) {
			result = sendRequest(l, tClass, null);
		} else if (l.getMethod().equals("POST")) {
			result = sendRequest(l, tClass, null);
		} else if (l.getMethod().equals("DELETE")) {
			result = sendRequest(l, tClass, new Object());
		} else {
			result = sendRequest(l, tClass, null);
		}
		if (isTimed()) {
			end = System.currentTimeMillis();
			long time = end - start;
			System.out.println("Method " + l.getMethod() + " on " + l.getUri() + " doing " + l.getRel() + " took " + time + " ms.");
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
	public <T extends BaseData> T navigate(Class<T> tClass, Link l, Object object) throws IOException {
		long start = 0; 
		long end;
		
		if (isTimed()) {
			start = System.currentTimeMillis();
		}
							
		Object result = sendRequest(l, tClass, object);
				
		if (isTimed()) {
			end = System.currentTimeMillis();
			long time = end - start;
			System.out.println("Method " + l.getMethod() + " on " + l.getUri() + " took " + time + " ms.");
		}
		return (T) result;
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

	private <T extends BaseData> T sendRequest(Link l, Class<T> tClass, Object object) throws IOException {
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
			
			if (method.equals("GET") && useMimeTypes)
				if (type == null || type.equals("")) {
					addMimeTypeAcceptToRequest(object, tClass, connection);
				} else {
					connection.addRequestProperty("Accept", helper.getSupportedType(type));
				}
			if (!useMimeTypes) 
				connection.setRequestProperty("Accept", "application/json");
			
			if (doOutput && useMimeTypes) {
				//this handles the case if the link is self made and the type field has not been set.
				if (type == null || type.equals("")) {
					addMimeTypeContentTypeToRequest(l, tClass, connection);
					addMimeTypeAcceptToRequest(l, tClass, connection);
				} else {
					connection.addRequestProperty("Accept", helper.getSupportedType(type));
					connection.addRequestProperty("Content-Type", helper.getSupportedType(type));
				}
			}
			
			if (!useMimeTypes)
				connection.setRequestProperty("Content-Type", "application/json");
			
			if (tokenData != null) {
				connection.addRequestProperty("Authorization", tokenData.getTokenType() + " " + tokenData.getAccessToken());
			}
					
			addVersionNumberToHeader(object, url, connection);
			
			if (method.equals("POST") || method.equals("PUT")) {
					String json = object != null ? gson.toJson(object) : ""; //should handle the case when POST without object.
					connection.addRequestProperty("Content-Length",	json.getBytes("UTF-8").length + "");
					OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
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
				System.out.println("Authentication expired " + connection.getResponseMessage() + " trying to reauthenticate");
				if (retry && this.tokenData != null) {
					this.tokenData = null;
					retry = false;
					if( authenticate() ) {
						System.out.println("Reauthentication success, will continue with " + l.getMethod() + " request on " + l.getRel());
						return sendRequest(l, tClass, object);
					}
				}		
				else throw new IOException("Tried to reauthenticate but failed, please check the credentials and status of NFleet-API");	
			}

			if (connection.getResponseCode() == HttpURLConnection.HTTP_NOT_MODIFIED) {
				return (T) objectCache.getObject(url);
			}

			if (connection.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT) {
				return (T) new ResponseData();
			}
			
			if (connection.getResponseCode() >= HttpURLConnection.HTTP_BAD_REQUEST && connection.getResponseCode() < HttpURLConnection.HTTP_INTERNAL_ERROR) {
				System.out.println("ErrorCode: " + connection.getResponseCode() + " " + connection.getResponseMessage() +
									" " + url + ", verb: " + method);
				
				String errorString = readErrorStreamAndCloseConnection(connection);
				throw (NFleetRequestException) gson.fromJson(errorString, NFleetRequestException.class);
			}
			else if (connection.getResponseCode() >= HttpURLConnection.HTTP_INTERNAL_ERROR ) {
				if (retry) {
					System.out.println("Request caused internal server error, waiting "+ RETRY_WAIT_TIME + " ms and trying again.");
					return waitAndRetry(connection, l, tClass, object);
				} else {
					System.out.println("Requst caused internal server error, please contact dev@nfleet.fi");
					String errorString = readErrorStreamAndCloseConnection(connection);
					throw new IOException(errorString);
				}
			}
			
			if (connection.getResponseCode() >= HttpURLConnection.HTTP_BAD_GATEWAY) {
				if (retry) {
					System.out.println("Could not connect to NFleet-API, waiting "+ RETRY_WAIT_TIME + " ms and trying again.");
					return waitAndRetry(connection, l, tClass, object);
				} else {
					System.out.println("Could not connect to NFleet-API, please check service status from http://status.nfleet.fi and try again later.");
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
		Object newEntity = gson.fromJson(result, tClass);
		objectCache.addUri(url, newEntity);
		return (T) newEntity;
	}

	private <T extends BaseData> T sendRequestWithAddedHeaders(Verb verb, String url, Class<T> tClass, Object object, HashMap<String, String> headers) throws IOException {
		URL serverAddress;
		HttpURLConnection connection;
		BufferedReader br;
		String result = "";
		try {
			serverAddress = new URL(url);
			connection = (HttpURLConnection) serverAddress.openConnection();
			connection.setInstanceFollowRedirects(false);
			boolean doOutput = doOutput(verb);
			connection.setDoOutput(doOutput);
			connection.setRequestMethod(method(verb));
			connection.setRequestProperty("Authorization", headers.get("authorization"));
			connection.addRequestProperty("Accept", "application/json");
			
			if (doOutput){ 
				connection.addRequestProperty("Content-Length", "0");
				OutputStreamWriter os = new OutputStreamWriter(connection.getOutputStream());
				os.write("");
				os.flush();
				os.close();
			}
			connection.connect();

			if (connection.getResponseCode() == HttpURLConnection.HTTP_SEE_OTHER || connection.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
				Link location = parseLocationLinkFromString(connection.getHeaderField("Location"));
				Link l = new Link("self", "/tokens", "GET","", true);
				ArrayList<Link> links = new ArrayList<Link>();
				links.add(l);
				links.add(location);
				ResponseData data = new ResponseData();
				data.setLocation(location);
				data.setLinks(links);
				return (T) data;
			}

			if (connection.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
				System.out.println("Authentication expired: " + connection.getResponseMessage());
				if ( retry && this.tokenData != null ) {
					retry = false;
					this.tokenData = null;
					if( authenticate() ) {
						System.out.println("Reauthentication success, will continue with " + verb + " request on " + url);
						return sendRequestWithAddedHeaders(verb, url, tClass, object, headers);
					}
				}
				else throw new IOException("Tried to reauthenticate but failed, please check the credentials and status of NFleet-API");	
			}
			
			if (connection.getResponseCode() >= HttpURLConnection.HTTP_BAD_REQUEST && connection.getResponseCode() < HttpURLConnection.HTTP_INTERNAL_ERROR) {
				System.out.println("ErrorCode: " + connection.getResponseCode() + " " + connection.getResponseMessage() +
									" " + url + ", verb: " + verb);
				
				String errorString = readErrorStreamAndCloseConnection(connection);
				throw (NFleetRequestException) gson.fromJson(errorString, NFleetRequestException.class);
			}
			else if (connection.getResponseCode() >= HttpURLConnection.HTTP_INTERNAL_ERROR ) {
				if (retry) {
					System.out.println("Server responded with internal server error, trying again in " + RETRY_WAIT_TIME + " msec.");
					try {
						retry = false;
						Thread.sleep(RETRY_WAIT_TIME);
						return sendRequestWithAddedHeaders(verb, url, tClass, object, headers);
					} catch (InterruptedException e) {
						
					}
				} else {
					System.out.println("Server responded with internal server error, please contact dev@nfleet.fi");
				}
				
				String errorString = readErrorStreamAndCloseConnection(connection);
				throw new IOException(errorString);
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
		}
		return (T) gson.fromJson(result, tClass);
	}

	private Link getAuthLink() {
		return new Link("authenticate", "/tokens", "POST", "", true);
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
			s = s.substring(s.lastIndexOf("/users"));
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
	
	
	private <T> void addMimeTypeAcceptToRequest(Object object, Class<T> tClass, HttpURLConnection connection) {
		Field f = null;
		StringTokenizer st = null;
		StringBuilder sb = null;
		Field[] fields = null;
		try {
			fields = object != null ? fields = object.getClass().getDeclaredFields() : tClass.getDeclaredFields();  
			 
			for (Field field : fields) {
				if (field.getName().equals("MimeType")) f = field;
			}
			if (f == null) {
				connection.setRequestProperty("Accept", "application/json");
				return;
			}
			
			String type = null;
			
			f.setAccessible(true);
			type = f.get(tClass).toString();						
						
			if (type != null) {
				 connection.addRequestProperty("Accept", helper.getSupportedType(type));
			}
		} catch (Exception e) {
			
		}
	}

	private <T> void addMimeTypeContentTypeToRequest(Object object, Class<T> tClass, HttpURLConnection connection) {
		Field f = null;
		StringTokenizer st = null;
		StringBuilder sb = null;
		Field[] fields = null;
		try {
			fields = object != null ? fields = object.getClass().getDeclaredFields() : tClass.getDeclaredFields();
			
			for (Field field : fields) {
				if (field.getName().equals("MimeType")) f = field;
			}
			
			if (f == null) {
				connection.setRequestProperty("Content-Type", "application/json");
				return;
			}

			String type = null;
			
			f.setAccessible(true);
			type = f.get(object).toString();
							
			if (type != null) {
			 	connection.addRequestProperty("Content-Type", helper.getSupportedType(type));
			}
		} catch (Exception e) {
		
		}
	}
	
	private void addVersionNumberToHeader(Object object, String url, HttpURLConnection connection) {
		Field f = null;
		Object fromCache = null;
		if (objectCache.containsUri(url))  {
			try {
				fromCache = objectCache.getObject(url);
				f = fromCache.getClass().getDeclaredField("VersionNumber");
			} catch (NoSuchFieldException e) {
			// Object does not have versionnumber.
			}
			if (f != null) {
				f.setAccessible(true);
				int versionNumber = 0;
				try {
					versionNumber = f.getInt(fromCache);
				} catch (IllegalAccessException e) {
					
				}
				connection.setRequestProperty("If-None-Match", versionNumber + "");
			}
		}
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
			System.out.println("Could not read error stream from the connection.");
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
			br = new BufferedReader(new InputStreamReader(is));

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
			System.out.println("Could not read data from connection");
		} 
		return sb.toString();
	}
	
	private <T extends BaseData> T waitAndRetry(HttpURLConnection connection, Link l, Class<T> tClass, Object object) {
		try {
			retry = false;
			Thread.sleep(RETRY_WAIT_TIME);
			return sendRequest(l, tClass, object);
		} catch (InterruptedException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}
	
	
	public TokenData getTokenData() {
		return this.tokenData;
	}

	public void setTokenData(TokenData data) {
		tokenData = data;
	}

	public ApiData getApiData() {
		return apiData;
	}

	public void setApiData(ApiData apiData) {
		this.apiData = apiData;
	}

	public boolean isTimed() {
		return timed;
	}

	public void setTimed(boolean timed) {
		this.timed = timed;
	}
}

