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
 * API-class to handle the communication between SDK-user and CO-SKY
 * optimization's REST-API.
 */
public class API {
	private String baseUrl;
	private String ClientKey;
	private String ClientSecret;
	private ApiData apiData;
	private TokenData tokenData;
	private ObjectCache objectCache;
	static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSX").create();

	public API(String baseUrl) {
		this.baseUrl = baseUrl;
		this.objectCache = new ObjectCache();
	}

	public boolean authenticate(String username, String password) {
		this.ClientKey = username;
		this.ClientSecret = password;
		ResponseData result = navigate(ResponseData.class, getAuthLink());

		if (result == null || result.getItems() != null) {
			return false;
		}

		TokenData authenticationData = navigate(TokenData.class, result.getLocation());

		this.tokenData = authenticationData;
		return true;
	}

	public <T extends BaseData> T navigate(Class<T> tClass, Link l) {
		return navigate(tClass, l, null);
	}

	@SuppressWarnings("unchecked")
	public <T extends BaseData> T navigate(Class<T> tClass, Link l,	HashMap<String, String> queryParameters) {
		Object result;

		if (tClass.equals(TokenData.class)) {
			result = sendRequest(Verb.GET, this.baseUrl + l.getUri(), tClass, null);
			return (T) result;
		}

		if (l.getRel().equals("authenticate")) {
			HashMap<String, String> headers = new HashMap<String, String>();
			String authorization = "Basic " + Base64.encodeBase64String((ClientKey + ":" + ClientSecret).getBytes());
			headers.put("authorization", authorization);
			result = sendRequestWithAddedHeaders(Verb.POST,	this.baseUrl + l.getUri(), tClass, null, headers);
			ResponseData data = (ResponseData) result;
			Link link = data.getLink("location");
			link.setUri(link.getUri());
			data.setLocation(link);
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
		} else if (l.getRel().equals("start") || l.getRel().equals("create-user")) {
			result = sendRequest(Verb.POST, this.baseUrl + uri, tClass, null);
		} else {
			result = sendRequest(Verb.GET, uri, tClass, null);
		}

		return (T) result;
	}

	@SuppressWarnings("unchecked")
	public <T extends BaseData> T navigate(Class<T> tClass, Link l, Object object) {
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

	private <T extends BaseData> T sendRequest(Verb verb, String url, Class<T> tClass, Object object) {
		URL serverAddress;
		BufferedReader br;
		String result = "";
		HttpURLConnection connection = null;
		try {
			serverAddress = new URL(url);
			connection = (HttpURLConnection) serverAddress.openConnection();
			boolean doOutput = (verb != Verb.GET);
			connection.setDoOutput(doOutput);
			connection.setRequestMethod(method(verb));
			connection.setInstanceFollowRedirects(false);

			if (tokenData != null) {
				connection.addRequestProperty("Authorization", tokenData.getTokenType() + " " + tokenData.getAccessToken());
			}
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Accept", "application/json");

			if (verb == Verb.GET) {
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
						int versionNumber = f.getInt(fromCache);
						connection.setRequestProperty("If-None-Match", versionNumber + "");
					}
				}
				
			}

			Field f = null;
			if (object != null) {
				try {
					f = object.getClass().getDeclaredField("VersionNumber");
				} catch (NoSuchFieldException e) {
					// Object does not have versionnumber.
				}
				if (f != null) {
					f.setAccessible(true);
					int versionNumber = f.getInt(object);
					connection.setRequestProperty("If-None-Match", versionNumber + "");

				}

				if ((verb == Verb.POST || verb == Verb.PUT)) {
					String json = gson.toJson(object);
					connection.addRequestProperty("Content-Length",	json.getBytes("UTF-8").length + "");
					OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
					osw.write(json);
					osw.flush();
					osw.close();
				}
			}

			connection.connect();

			if (connection.getResponseCode() == HttpURLConnection.HTTP_SEE_OTHER || connection.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
				ResponseData data = new ResponseData();
				Link l = parseLocationLinkFromString(connection.getHeaderField("Location"));
				data.setLocation(l);
				connection.disconnect();
				return (T) data;
			}

			if (connection.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
				System.out.println("Authentication expired");
				this.authenticate(this.ClientKey, this.ClientSecret);
				System.out.println("Authenticated again");
				connection.disconnect();
				return sendRequest(verb, url, tClass, object);
			}

			if (connection.getResponseCode() == HttpURLConnection.HTTP_NOT_MODIFIED) {
				return (T) objectCache.getObject(url);
			}

			if (connection.getResponseCode() > HttpURLConnection.HTTP_FORBIDDEN) {
				System.out.println("code: " + connection.getResponseCode() + " " + connection.getResponseMessage() + " " + url);
				InputStream stream = connection.getErrorStream();
				br = new BufferedReader(new InputStreamReader(stream));
				StringBuilder sb = new StringBuilder();
				String s;
				while ((s = br.readLine()) != null) {
					sb.append(s);
				}
				String body = sb.toString();
				System.out.println(body);
				System.out.println("Please check the request " + connection.getResponseMessage() + body + " " + url);
				connection.disconnect();
				return (T) gson.fromJson(body, ResponseData.class);
			}

			InputStream is = connection.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));

			StringBuilder sb = new StringBuilder();
			String sa;
			while ((sa = br.readLine()) != null) {
				sb.append(sa).append("\n");
			}
			String abba = null;
			if ((abba = connection.getHeaderField("ETag")) != null) {
				sb.insert(sb.lastIndexOf("}"), ",\"VersionNumber\":" + abba	+ "");

			}
			result = sb.toString();

		} catch (MalformedURLException e) {
			System.out.println("Troubles with the url " + url);
			return null;
		} catch (ProtocolException e) {
			System.out
					.println("Troubles reaching the service, please check service status");
			return null;
		} catch (UnsupportedEncodingException e) {
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} finally {
			assert connection != null;
			connection.disconnect();
		}
		Object newEntity = gson.fromJson(result, tClass);
		objectCache.addUri(url, newEntity);
		return (T) newEntity;
	}

	private <T extends BaseData> T sendRequestWithAddedHeaders(Verb verb, String url, Class<T> tClass, Object object, HashMap<String, String> headers) {
		URL serverAddress;
		HttpURLConnection connection;
		BufferedReader br;
		String result = "";
		try {
			serverAddress = new URL(url);
			connection = (HttpURLConnection) serverAddress.openConnection();
			connection.setInstanceFollowRedirects(false);
			connection.setDoOutput(true);
			connection.setRequestMethod(method(verb));
			connection.setRequestProperty("Authorization", headers.get("authorization"));
			connection.addRequestProperty("Accept", "application/json");
			connection.addRequestProperty("Content-Length", "0");

			OutputStreamWriter os = new OutputStreamWriter(connection.getOutputStream());
			os.write("");
			os.flush();
			os.close();
			connection.connect();

			if (connection.getResponseCode() == HttpURLConnection.HTTP_SEE_OTHER	|| connection.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
				Link location = parseLocationLinkFromString(connection.getHeaderField("Location"));
				Link l = new Link("self", "/tokens", "GET", true);
				ArrayList<Link> links = new ArrayList<Link>();
				links.add(l);
				links.add(location);
				ResponseData data = new ResponseData();
				data.setLinks(links);
				return (T) data;
			}

			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new IOException(connection.getResponseMessage() + connection.getContent());
			}

			InputStream is = connection.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));

			StringBuilder sb = new StringBuilder();
			String sa;
			while ((sa = br.readLine()) != null) {
				sb.append(sa).append("\n");
			}
			result = sb.toString();
			
		} catch (MalformedURLException e) {
			System.out.println("Url is not correct, please check");
			System.out.println(e.toString());
			return null;
		} catch (ProtocolException e) {
			System.out.println("Please check if the service is up");
			System.out.println(e.toString());
			return null;
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.toString());
			return null;
		} catch (IOException e) {
			System.out.println(e.toString());
			return null;
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
		if (s.contains("82"))
			s = s.replace("82", "81");
		if (s.contains("/users"))
			s = s.substring(s.indexOf("/users"));
		else
			s = s.substring(s.lastIndexOf("/"));
		return new Link("location", s, "GET", true);
	}

	private enum Verb {
		GET, PUT, POST, DELETE, PATCH
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
}
