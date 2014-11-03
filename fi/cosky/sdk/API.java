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
import java.util.Timer;

import com.google.gson.*;

import org.apache.commons.codec.binary.Base64;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

/**
 * API-class to handle the communication between SDK-user and CO-SKY
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
	
	static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'").create();
	
	public API(String baseUrl) {
		this.baseUrl = baseUrl;
		this.objectCache = new ObjectCache();
		this.timed = false;
		this.retry  = true;
		this.useMimeTypes = false; //change this when production will support mimetypes.
		
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
				System.out.println("Could not authenticate, please check credentials");
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
		long start = 0; 
		long end;
		
		if (isTimed()) {
			start = System.currentTimeMillis();
		}
		if (tClass.equals(TokenData.class)) {
			result = sendRequest(Verb.GET, l.getUri(), tClass, null);
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
			result = sendRequest(Verb.GET, this.baseUrl + uri, tClass, null);
		} else if (l.getMethod().equals("PUT")) {
			result = sendRequest(Verb.PUT, this.baseUrl + uri, tClass, null);
		} else if (l.getMethod().equals("POST")) {
			result = sendRequest(Verb.POST, this.baseUrl + uri, tClass, null);
		} else if (l.getMethod().equals("DELETE")) {
			result = sendRequest(Verb.DELETE, this.baseUrl + uri, tClass, new Object());
		} else {
			result = sendRequest(Verb.GET, uri, tClass, null);
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
		Object result = null;
		if (l.getMethod().equals("PUT")) {
			result = sendRequest(Verb.PUT, this.baseUrl + l.getUri(), tClass, object);
		}

		if (l.getMethod().equals("POST")) {
			String url = this.baseUrl + l.getUri();
			result = sendRequest(Verb.POST, url, tClass, object);
		}

		if (l.getMethod().equals("PATCH")) {
			String url = this.baseUrl + l.getUri();
			result = sendRequest(Verb.PATCH, url, tClass, object);
		}
		
		if (l.getMethod().equals("DELETE")) {
			String url = this.baseUrl + l.getUri();
			result = sendRequest(Verb.DELETE, url, tClass, object);
		}
		if (isTimed()) {
			end = System.currentTimeMillis();
			long time = end - start;
			System.out.println("Method " + l.getMethod() + " on " + l.getUri() + " took " + time + " ms.");
		}
		return (T) result;
	}

	public Link getRoot() {
		return new Link("self", baseUrl, "GET", true);
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	private <T extends BaseData> T sendRequest(Verb verb, String url, Class<T> tClass, Object object) throws IOException {
		URL serverAddress;
		BufferedReader br;
		String result = "";
		HttpURLConnection connection = null;
		try {
			serverAddress = new URL(url);
			connection = (HttpURLConnection) serverAddress.openConnection();
			boolean doOutput = doOutput(verb);
			connection.setDoOutput(doOutput);
			connection.setRequestMethod(method(verb));
			connection.setInstanceFollowRedirects(false);
			
			if (verb == Verb.GET && useMimeTypes)
				addMimeTypeAcceptToRequest(object, tClass, connection);
			if (!useMimeTypes) 
				connection.setRequestProperty("Accept", "application/json");
			
			
			if (doOutput && useMimeTypes)
				addMimeTypeContentTypeToRequest(object, tClass, connection);
			if (!useMimeTypes)
				connection.setRequestProperty("Content-Type", "application/json");
			
			if (tokenData != null) {
				connection.addRequestProperty("Authorization", tokenData.getTokenType() + " " + tokenData.getAccessToken());
			}
					
			addVersionNumberToHeader(object, url, connection);
			
			if (verb == Verb.POST || verb == Verb.PUT) {
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
				Link l = parseLocationLinkFromString(connection.getHeaderField("Location"));
				data.setLocation(l);
				connection.disconnect();
				return (T) data;
			}

			if (connection.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
				System.out.println("Authentication expired " + connection.getResponseMessage());
				if (retry && this.tokenData != null) {
					this.tokenData = null;
					retry = false;
					if( authenticate() ) {
						System.out.println("Authenticated again");
						return sendRequest(verb, url, tClass, object);
					}
				}		
				else throw new IOException("Could not authenticate");	
			}

			if (connection.getResponseCode() == HttpURLConnection.HTTP_NOT_MODIFIED) {
				return (T) objectCache.getObject(url);
			}

			if (connection.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT) {
				return (T) new ResponseData();
			}
			
			if (connection.getResponseCode() >= HttpURLConnection.HTTP_BAD_REQUEST && connection.getResponseCode() < HttpURLConnection.HTTP_INTERNAL_ERROR) {
				System.out.println("ErrorCode: " + connection.getResponseCode() + " " + connection.getResponseMessage() +
									" " + url + ", verb: " + verb);
				
				String errorString = readErrorStreamAndCloseConnection(connection);
				throw (NFleetRequestException) gson.fromJson(errorString, NFleetRequestException.class);
			}
			else if (connection.getResponseCode() >= HttpURLConnection.HTTP_INTERNAL_ERROR ) {
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

			if (connection.getResponseCode() == HttpURLConnection.HTTP_SEE_OTHER	|| connection.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
				Link location = parseLocationLinkFromString(connection.getHeaderField("Location"));
				Link l = new Link("self", "/tokens", "GET", true);
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
						System.out.println("Authenticated again");
						return sendRequestWithAddedHeaders(verb, url, tClass, object, headers);
					}
					System.out.println("Could not authenticate");
				}
				else throw new IOException("Could not authenticate");	
			}
			
			if (connection.getResponseCode() >= HttpURLConnection.HTTP_BAD_REQUEST && connection.getResponseCode() < HttpURLConnection.HTTP_INTERNAL_ERROR) {
				System.out.println("ErrorCode: " + connection.getResponseCode() + " " + connection.getResponseMessage() +
									" " + url + ", verb: " + verb);
				
				String errorString = readErrorStreamAndCloseConnection(connection);
				throw (NFleetRequestException) gson.fromJson(errorString, NFleetRequestException.class);
			}
			else if (connection.getResponseCode() >= HttpURLConnection.HTTP_INTERNAL_ERROR ) {
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
		return new Link("authenticate", "/tokens", "POST", true);
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
		//Azure emulator hack
		if (s.contains("82"))
			s = s.replace("82", "81");
		if (s.contains("/tokens"))
			return new Link("location", s, "GET", true);
		else
			s = s.substring(s.lastIndexOf("/users"));
		return new Link("location", s, "GET", true);
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
	
	private enum Verb {
		GET, PUT, POST, DELETE, PATCH
	}
	
	
	private <T> void addMimeTypeAcceptToRequest(Object object, Class<T> tClass, HttpURLConnection connection) {
		Field f = null;
		try {
			
			f = tClass.getDeclaredField("MimeType");
			String type = null;
			if (f != null) {
				f.setAccessible(true);
				type = f.get(tClass).toString();						
			}
			
			double version = 0;
			f = tClass.getDeclaredField("MimeVersion");
			if (f != null) {
				f.setAccessible(true);
				version = f.getDouble(tClass);
			}
			if (type != null && version > 0) 
				connection.setRequestProperty("Accept", type + ";version=" + version);
		} catch (Exception e) {
			connection.setRequestProperty("Accept", "application/json");
		}
	}

	private <T> void addMimeTypeContentTypeToRequest(Object object, Class<T> tClass, HttpURLConnection connection) {
		Field f = null;
		try {
			f = object.getClass().getDeclaredField("MimeType");
			String type = null;
			if (f != null) {
				f.setAccessible(true);
				type = f.get(object).toString();
			}
			f = object.getClass().getDeclaredField("MimeVersion");
			double version = 0;
			if (f != null) {
				f.setAccessible(true);
				version = f.getDouble(object);
			}
			if (type != null && version > 0) 
				connection.setRequestProperty("Content-Type", type + ";version=" + version);
		} catch (Exception e) {
			connection.setRequestProperty("Accept", "application/json");
		}
	}
	
	public void addVersionNumberToHeader(Object object, String url, HttpURLConnection connection) {
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
			e.printStackTrace();
		}
		String body = sb.toString();
		connection.disconnect();
		return body;
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
			e.printStackTrace();
		}
		return sb.toString();
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

