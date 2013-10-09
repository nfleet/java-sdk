import com.google.gson.*;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: japelait
 * Date: 5.4.2013
 * Time: 10:28
 * API-class to handle the communication between SDK-user and CO-SKY optimization's REST-API.
 *
 */
public class API {
    private String baseUrl;
    private String username;
    private String password;
    private ApiData apiData;
    private AuthenticationData authenticationData;
    public static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSX").create();

    public API(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public boolean authenticate(String username, String password) {
        this.username = username;
        this.password = password;
        ResultData result = navigate(ResultData.class, getAuthLink());

        if (result == null || result.getItems() != null) {
            return false;
        }

        AuthenticationData authenticationData = navigate(AuthenticationData.class, result.getLocation());

        this.authenticationData = authenticationData;
        return true;
    }

    public <T extends BaseData> T navigate(Class<T> tClass, Link l) {

        return navigate(tClass, l, null);
    }

    public <T extends BaseData> T navigate(Class<T> tClass, Link l, HashMap<String, String> queryParameters) {
        String result;

        if (tClass.equals(AuthenticationData.class)) {
            result = sendRequest(Verb.GET, this.baseUrl + l.getUri(), "");
            return gson.fromJson(result, tClass);
        }


        if (l.getRel().equals("authenticate")) {
            HashMap<String, String> headers = new HashMap<String, String>();
            String authorization = "Basic " + Base64.encode((username + ":" + password).getBytes());
            headers.put("authorization", authorization);
            result = sendRequestWithAddedHeaders(this.baseUrl + l.getUri(), headers);
            if (result.length() < 1) {
                ErrorData d = new ErrorData();
                ArrayList<ErrorData> errors = new ArrayList<ErrorData>();
                ResultData a = new ResultData();
                errors.add(d);
                a.setItems(errors);
                return (T) a;
            }
            result = result.substring(result.indexOf("/tokens"), result.length());
            ResultData data = new ResultData();
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
        } else if (l.getRel().equals("create-new-optimization") || l.getRel().equals("start")) {
            result = sendRequest(Verb.POST, this.baseUrl + uri, "");
        }  else {
            result = sendRequest(Verb.GET, uri, "");
        }

        if (tClass.equals(ResultData.class)) {
            ResultData data = new ResultData();
            data.setLocation(new Link("location", result, "GET"));
            return (T) data;
        }
        return gson.fromJson(result, tClass);
    }

    public <T extends BaseData> T navigate(Class<T> tClass, Link l, Object object ) {
        String result = "";

        if (l.getMethod().equals("PUT")) {
            result = sendRequest(Verb.PUT, this.baseUrl + l.getUri(), createJsonFromDto(object));
        }

        if (l.getMethod().equals("POST") ) {
            String url = this.baseUrl + l.getUri();
            result = sendRequest(Verb.POST, url, createJsonFromDto(object));
        }

        if (tClass.equals(ResultData.class) && result.length() > 0) {
            ResultData data = new ResultData();
            if (result.contains("/problems")){
                result = result.substring(result.indexOf("/problems"), result.length());
            } else if (result.contains("/api")) {
                result = result.substring(result.indexOf("/api"), result.length());
            } else {
                data = gson.fromJson(result, ResultData.class);
            }
            data.setLocation(new Link("location", result, "GET", true));

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

            if (authenticationData != null) {
                connection.addRequestProperty("Authorization", authenticationData.getTokenType() + " " + authenticationData.getAccessToken());
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
                this.authenticate(this.username, this.password);
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

    public AuthenticationData getAuthenticationData() {
        return this.authenticationData;
    }

    public void setAuthenticationData(AuthenticationData data) {
        authenticationData = data;
    }
}


