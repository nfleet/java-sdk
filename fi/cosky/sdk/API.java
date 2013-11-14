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
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.*;

import org.apache.commons.codec.binary.Base64;

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */

/**
 *  API-class to handle the communication between SDK-user and CO-SKY optimization's REST-API.
 */
public class API {
    private String baseUrl;
    private String ClientKey;
    private String ClientSecret;
    private ApiData apiData;
    private TokenData tokenData;
    public static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSX").create();

    public API(String baseUrl) {
        this.baseUrl = baseUrl;
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
	public <T extends BaseData> T navigate(Class<T> tClass, Link l, HashMap<String, String> queryParameters) {
        String result;

        if (tClass.equals(TokenData.class)) {
            result = sendRequest(Verb.GET, this.baseUrl + l.getUri(), "");
            return gson.fromJson(result, tClass);
        }

        if (l.getRel().equals("authenticate")) {
            HashMap<String, String> headers = new HashMap<String, String>();
            String authorization = "Basic " + Base64.encodeBase64String((ClientKey + ":" + ClientSecret).getBytes());
            headers.put("authorization", authorization);
            result = sendRequestWithAddedHeaders(this.baseUrl + l.getUri(), headers);
            if (result.length() < 1) {
                ErrorData d = new ErrorData();
                ArrayList<ErrorData> errors = new ArrayList<ErrorData>();
                ResponseData a = new ResponseData();
                errors.add(d);
                a.setItems(errors);
                return (T) a;
            }
            result = result.substring(result.indexOf("/tokens"), result.length());
            ResponseData data = new ResponseData();
            data.setLocation(new Link("location", result, "GET"));
            return (T) data;
        }

        String uri = l.getUri();
        if (l.getMethod().equals("GET") && queryParameters != null && !queryParameters.isEmpty()) {
            StringBuilder sb = new StringBuilder(uri + "?");

            for(String key : queryParameters.keySet()) {
                 sb.append(key + "="+ queryParameters.get(key)+"&");
            }
            sb.deleteCharAt(sb.length()-1);
            uri = sb.toString();
        }

        if (l.getMethod().equals("GET") && !uri.contains(":") && !l.getRel().equals("create-new-optimization")) {
            result = sendRequest(Verb.GET, this.baseUrl + uri, "");
        } else if (l.getMethod().equals("PUT")) {
            result = sendRequest(Verb.PUT, this.baseUrl + uri, "");
        } else if (l.getRel().equals("create-new-optimization") || l.getRel().equals("start") || l.getRel().equals("create-user")) {
            result = sendRequest(Verb.POST, this.baseUrl + uri, "");
        }  else {
            result = sendRequest(Verb.GET, uri, "");
        }

        if (tClass.equals(ResponseData.class)) {
            ResponseData data = new ResponseData();
            data.setLocation(new Link("location", l.getUri() + result, "GET"));
            return (T) data;
        }
        
        
        return gson.fromJson(result, tClass);
    }

    @SuppressWarnings("unchecked")
	public <T extends BaseData> T navigate(Class<T> tClass, Link l, Object object ) {
        String result = "";

        if (l.getMethod().equals("PUT")) {
            result = sendRequest(Verb.PUT, this.baseUrl + l.getUri(), createJsonFromDto(object));
        }

        if (l.getMethod().equals("POST") ) {
            String url = this.baseUrl + l.getUri();
            result = sendRequest(Verb.POST, url, createJsonFromDto(object));
        }

        if (tClass.equals(ResponseData.class) && result.length() > 0) {
            ResponseData data = new ResponseData();
            if (result.contains("/problems")){
                result = result.substring(result.indexOf("/problems"), result.length());
            } else if (result.contains("/api")) {
                result = result.substring(result.indexOf("/api"), result.length());
            } else if (l.getRel().equals("create-user")){
            	
            }else if (result.contains("http:") || result.contains("https:")) {
            	result = result.replace("82", "81");
            	data.setLocation(new Link("location", result, "GET", true));
            	return (T) data;
            }
            else {
                data = gson.fromJson(result, ResponseData.class);
            }
            
            //Azure emulator hack
            if (result.contains("82")) result = result.replace("82", "81");
            result = result.substring(result.lastIndexOf("/"));
            data.setLocation(new Link("location", l.getUri() + result, "GET", true));

            return (T) data;
        }
        return gson.fromJson(result, tClass);
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

    private String sendRequest(Verb verb, String url, String json) {

        URL serverAddress;
        BufferedReader br;
        String result = "";
        HttpURLConnection connection = null;
        try {
            serverAddress = new URL(url);
            connection = (HttpURLConnection) serverAddress.openConnection();
	        boolean doOutput = (verb != Verb.GET);
            connection.setDoOutput(doOutput);
            connection.setRequestMethod( method(verb) );
            connection.setInstanceFollowRedirects(false);
            
            if (json.contains("VersionNumber")) {
            	try {
            		String s = json.substring(json.indexOf("VersionNumber\":"));
            		String number = s.substring(s.indexOf(":")+1, s.indexOf('}'));
            		int nasdf = Integer.parseInt(number);
            		connection.setRequestProperty("If-None-Match", nasdf+"");
            	} catch (Exception e) {
            		//Very purkka
            		String s = json.substring(json.indexOf("VersionNumber\":"));
            		String number = s.substring(s.indexOf(':')+1, s.indexOf(","));
            		connection.setRequestProperty("If-None-Match", number);
            	}
            	            	
            }
            if (tokenData != null) {
                connection.addRequestProperty("Authorization", tokenData.getTokenType() + " " + tokenData.getAccessToken());
            }
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            if (json.length() > 0 || (verb == Verb.POST || verb == Verb.PUT)) {
                connection.addRequestProperty("Content-Length", json.getBytes("UTF-8").length + "");
                OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
                osw.write(json);
                osw.flush();
                osw.close();
            }

            connection.connect();
            
             if (connection.getResponseCode() == 303 || connection.getResponseCode() == 201) {
                String s = connection.getHeaderField("Location");
                return s;
            }

            if (connection.getResponseCode() == 401) {
                System.out.println("Authentication expired");
                this.authenticate(this.ClientKey, this.ClientSecret);
                System.out.println("Authenticated again");
                return sendRequest(verb, url, json);
            }

            if (connection.getResponseCode() > 401) {
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
                System.out.println("Please check the request " + connection.getResponseMessage() + body + " " + url) ;
                return body;
            }

            InputStream is = connection.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));

            StringBuilder sb = new StringBuilder();
            String sa;
            while (( sa = br.readLine()) != null) {
                sb.append(sa).append("\n");
            }
            String abba = null;
            if ((abba = connection.getHeaderField("ETag")) != null) {
            	sb.insert(sb.lastIndexOf("}"),",\"VersionNumber\":" + abba+"" );
            	
            }
            result = sb.toString();

         } catch (MalformedURLException e) {
            System.out.println("Troubles with the url " + url);
            return "";
        } catch (ProtocolException e) {
            System.out.println("Troubles reaching the service, please check service status");
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            assert connection != null;
            connection.disconnect();
        }
            return result;
        }

    private String sendRequestWithAddedHeaders(String url, HashMap<String, String> headers) {
        URL serverAddress;
        HttpURLConnection connection;
        BufferedReader br;
        String result = "";
        try {
            serverAddress = new URL(url);
            connection =(HttpURLConnection) serverAddress.openConnection();
            connection.setInstanceFollowRedirects(false);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", headers.get("authorization"));
            connection.addRequestProperty("Accept", "application/json");
            connection.addRequestProperty("Content-Length", "0");

            OutputStreamWriter os = new OutputStreamWriter(connection.getOutputStream());
            os.write("");
            os.flush();
            os.close();
            connection.connect();

            if (connection.getResponseCode() == 303 || connection.getResponseCode() == 201) {
                return connection.getHeaderField("Location");
            }

            if (connection.getResponseCode() != 200) {
                throw new IOException(connection.getResponseMessage() + connection.getContent());
            }

            InputStream is = connection.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));

            StringBuilder sb = new StringBuilder();
            String sa;
            while (( sa = br.readLine()) != null) {
                sb.append(sa).append("\n");
            }
            result = sb.toString();
        } catch (MalformedURLException e) {
            System.out.println("Url is not correct, please check");
            System.out.println(e.toString());
            return "";
        } catch (ProtocolException e) {
            System.out.println("Please check if the service is up");
            System.out.println(e.toString());
            return "";
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.toString());
            return "";
        } catch (IOException e) {
            System.out.println(e.toString());
            return "";
        }
        return result;
    }

    private Link getAuthLink() {
        return new Link("authenticate", "/tokens", "POST", true);
    }

    private static String createJsonFromDto(Object item) {
        return gson.toJson(item);
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
        }
        return "";
    }

    private enum Verb {GET, PUT, POST, DELETE}

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


