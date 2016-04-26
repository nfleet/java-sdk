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
import java.util.List;

import com.google.gson.*;

import org.apache.commons.codec.binary.Base64;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

/**
 * API class to handle the communication between SDK user and NFleet
 * optimization's REST API.
 */
public class API {
	private String baseUrl;
	private String ClientKey;
	private String ClientSecret;
	private ApiData apiData;
	private TokenData tokenData;
	private boolean timed;
	private ObjectCache objectCache;
	private boolean useMimeTypes;
	private MimeTypeHelper helper;
	
	private static int RETRY_WAIT_TIME_FACTOR = 1000;
	private static int UNAVAILABLE_RETRY_WAIT_TIME_FACTOR = 10000;
    private static int REQUEST_ATTEMPTS = 3;
	
	static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'").create();
	
	public API(String baseUrl) {
		this.baseUrl = baseUrl;
		this.objectCache = new ObjectCache();
		this.timed = false;
		this.useMimeTypes = true;
		this.helper = new MimeTypeHelper();
	} 

	private boolean authenticate() {
		return authenticate(this.ClientKey, this.ClientSecret);
	}	
	
	public boolean authenticate(String username, String password) {
		this.ClientKey = username;
		this.ClientSecret = password;
		System.out.println("Authenticating API user " + username);
		try {
			ResponseData result = navigate(ResponseData.class, getAuthLink());

			if (result == null || result.getItems() != null) {
				System.out.println("Unexpected authentication response. Please check credentials and service status.");
				return false;
			}

			TokenData authenticationData = navigate(TokenData.class, result.getLocation());

			this.tokenData = authenticationData;
        } catch (NFleetRequestException e) {
            return false;        
		} catch (Exception e) {
            System.out.println("Authentication failed: " + e.toString());
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

    public <T extends BaseData> T navigate(Class<T> tClass, Link l, Object object) throws IOException {
        return navigate(tClass, l, object, null);
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
	public <T extends BaseData> T navigate(Class<T> tClass, Link l, Object object, HashMap<String, String> queryParameters) throws IOException {
        long start = 0;
		long end;

        if (l.getMethod().equals("GET") && queryParameters != null	&& !queryParameters.isEmpty()) {
            String uri = l.getUri();
            StringBuilder sb = new StringBuilder(uri + "?");

            for (String key : queryParameters.keySet()) {
                sb.append(key + "=" + queryParameters.get(key) + "&");
            }
            sb.deleteCharAt(sb.length() - 1);
            l.setUri(sb.toString());
        }

        if (l.getMethod().equals(Verb.DELETE)) object = new Object();

        if (isTimed()) {
			System.out.println("Doing " + l.getMethod() + " on " + l.getUri() + ".");
            start = System.currentTimeMillis();
        }

        Object result = sendRequest(l, tClass, object, REQUEST_ATTEMPTS);
				
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

	@SuppressWarnings("unchecked")
	private <T extends BaseData> T sendRequest(Link l, Class<T> tClass, Object object, int attempts) throws IOException {
		URL serverAddress;
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

            if (l.getRel().equals("authenticate")) {
                String authorization = "Basic " + Base64.encodeBase64String((this.ClientKey + ":" + this.ClientSecret).getBytes());
                connection.setRequestProperty("Authorization", authorization);
            }

			if (tokenData != null) {
				connection.addRequestProperty("Authorization", tokenData.getTokenType() + " " + tokenData.getAccessToken());
			}
			
			if (connection.getRequestProperty("Accept") == null)
				connection.setRequestProperty("Accept", "application/json");
			if (connection.getRequestProperty("Content-Type") == null);
				connection.setRequestProperty("Content-Type", "application/json");

			addVersionNumberToHeader(url, connection);
			
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
                if (tokenData == null) {
                    System.out.println("Authentication failed. Make sure you have invoked authenticate() with correct credentials.");
                }
                else {                    
                    System.out.println("Access token has expired. Trying to reauthenticate.");
                    this.tokenData = null;
                    if( authenticate() ) {
                        System.out.println("Reauthentication success, continuing " + l.getRel() + ".");
                        return sendRequest(l, tClass, object, attempts - 1);
                    }
                    else throw new IOException("Reauthentication failed. Please check the credentials and service status.");
                }
			}

			if (connection.getResponseCode() == HttpURLConnection.HTTP_NOT_MODIFIED) {
				return (T) objectCache.getObject(url);
			}

			if (connection.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT) {
				return (T) new ResponseData();
			}

            if (connection.getResponseCode() >= HttpURLConnection.HTTP_BAD_REQUEST && connection.getResponseCode() < HttpURLConnection.HTTP_INTERNAL_ERROR) {
                throw createException(connection);                
			}
			else if (connection.getResponseCode() >= HttpURLConnection.HTTP_INTERNAL_ERROR && connection.getResponseCode() < HttpURLConnection.HTTP_BAD_GATEWAY) {
				if (attempts > 0) {
                    int attempt = REQUEST_ATTEMPTS - attempts + 1;
                    int waiting = attempt * attempt * RETRY_WAIT_TIME_FACTOR;
					System.out.println("Request caused internal server error, waiting " + waiting + "ms and trying again (attempt " + attempt + " of " + REQUEST_ATTEMPTS + ").");
                    wait(waiting);
                    return sendRequest(l, tClass, object, attempts - 1);
				} else {
					System.out.println("Request caused internal server error, please contact support at dev@nfleet.fi.");
					throw createException(connection);
				}
			}
			else if (connection.getResponseCode() >= HttpURLConnection.HTTP_BAD_GATEWAY && connection.getResponseCode() < HttpURLConnection.HTTP_VERSION) {
				if (attempts > 0) {
                    int attempt = REQUEST_ATTEMPTS - attempts + 1;
                    int waiting = attempt * attempt * UNAVAILABLE_RETRY_WAIT_TIME_FACTOR;
					System.out.println("NFleet service is unavailable, waiting " + waiting + "ms and trying again (attempt " + attempt + " of " + REQUEST_ATTEMPTS + ").");
                    wait(waiting);
                    return sendRequest(l, tClass, object, attempts - 1);
				} else {
					System.out.println("NFleet service is unavailable, please try again later. If the problem persists, contact support at dev@nfleet.fi.");
					throw createException(connection);
				}
			}
			else if (connection.getResponseCode() >= HttpURLConnection.HTTP_VERSION) {
				System.out.println("Could not connect to NFleet service.");
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
    
    private NFleetRequestException createException(HttpURLConnection connection) throws IOException {
        NFleetRequestException ex = null;
        String errorString = readErrorStreamAndCloseConnection(connection);

        ex = gson.fromJson(errorString, NFleetRequestException.class);

        if (ex.getItems() == null || ex.getItems().size() == 0) {
            ErrorData d = new ErrorData();
            d.setCode(connection.getResponseCode());
            d.setMessage(connection.getResponseMessage());
            List<ErrorData> errors = new ArrayList<ErrorData>();
            errors.add(d);
            ex.setItems(errors);
        }
        ex.setStatusCode(connection.getResponseCode());

        return ex;
    }

	private Link getAuthLink() {
		return new Link("authenticate", "/tokens", "POST", "", true);
	}

	private Link parseLocationLinkFromString(String s) {
		if (!s.contains("/tokens"))
			s = s.substring(s.lastIndexOf("/users"));
		return new Link("location", s, "GET", "", true);
	}

	private boolean doOutput(String verb) {
		return (verb.equals("POST") || verb.equals("PUT") || verb.equals("PATCH"));
	}
	
	private enum Verb { GET, PUT, POST, DELETE, PATCH }
	
	private <T> void addMimeTypeAcceptToRequest(Object object, Class<T> tClass, HttpURLConnection connection) {
		Field f = null;
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
	
	private void addVersionNumberToHeader(String url, HttpURLConnection connection) {
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
	
	private String readDataFromConnection(HttpURLConnection connection) throws IOException {
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
			System.out.println("Could not read data from connection: " + e.getMessage() );
            throw e;
		} 
		return sb.toString();
	}	
    
    private void wait(int timeInMilliseconds)
    {
        try {
            Thread.sleep(timeInMilliseconds);
        } catch (InterruptedException e) {
            // no action
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