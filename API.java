import com.google.gson.*;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;



/**
 * Created with IntelliJ IDEA.
 * User: japelait
 * Date: 5.4.2013
 * Time: 10:28
 * API-class to handle the communication between SDK-user and CO-SKY optimization's REST-API.
 *
 * TODO: There will be an change to the ResultData class and thus some changes will occur.
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
        String result;

        if (tClass.equals(AuthenticationData.class)) {
            result = sendGet(this.baseUrl + l.getUri());
            return gson.fromJson(result, tClass);
        }


        if (l.getRel().equals("authenticate")) {
            HashMap<String, String> headers = new HashMap<String, String>();
            String authorization = "Basic " + Base64.encode((username + ":" + password).getBytes());
            headers.put("authorization", authorization);
            result = sendCredentials(this.baseUrl + l.getUri(), headers);
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

        if (l.getMethod().equals("GET") && !l.getUri().contains(":") && !l.getRel().equals("start-new-optimization")) {
            result = sendGet(this.baseUrl + l.getUri());
        } else if (l.getRel().equals("start-new-optimization")) {
            result = sendPost("", this.baseUrl + l.getUri());
        }  else {
            result = sendGet(l.getUri());
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
            result = sendPut(createJsonFromDto(object), this.baseUrl + l.getUri());
        }

        if (l.getMethod().equals("POST") ) {
            String url = this.baseUrl + l.getUri();
            result = sendPost(createJsonFromDto(object), url);
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
            data.setLocation(new Link("location", result, "GET"));
            return (T) data;
        }
        return gson.fromJson(result, tClass);
    }
    private String sendPut(String json, String address) {
        return "not supported yet " + json + " " + address;
    }

    private String sendPost(String json, String address) {
        URL serverAddress;
        BufferedReader br;
        String result = "";
        HttpURLConnection connection = null;
        try {
            serverAddress = new URL(address);
            connection = (HttpURLConnection) serverAddress.openConnection();
            //connection = (HttpsURLConnection) serverAddress.openConnection();
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");

            connection.addRequestProperty("Content-Type", "application/json");
            connection.addRequestProperty("Accept", "application/json");
            connection.addRequestProperty("Content-Length", json.getBytes("UTF-8").length + "");
            if (authenticationData != null) {
                connection.addRequestProperty("Authorization" , authenticationData.getTokenType() + " " + authenticationData.getAccessToken());
            }
            OutputStreamWriter os = new OutputStreamWriter(connection.getOutputStream());
            os.write(json);
            os.flush();
            os.close();
            connection.connect();

            if ( connection.getResponseCode() == 303 || connection.getResponseCode() == 201) {
                return connection.getHeaderField("Location");
            }

            if ( connection.getResponseCode() == 401 ) {
                authenticate(this.username, this.password);
                return sendPost(json, address);
            }

            if ( connection.getResponseCode() != 200 ) {
                InputStream is = connection.getErrorStream();
                br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String s;
                while ((s = br.readLine()) != null) {
                    sb.append(s);
                }
                System.out.println("Please check the request " + connection.getResponseMessage() + sb.toString()+ " " + address) ;
                return sb.toString();
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
            System.out.println("Troubles with the url " + address );
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

    private String sendGet(String url) {
        URL serverAddress;
        HttpURLConnection connection = null;
        BufferedReader br;
        String result = "";
        try {
            serverAddress = new URL(url);
             connection = (HttpURLConnection) serverAddress.openConnection();
            //connection = (HttpsURLConnection) serverAddress.openConnection();
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");
            if (authenticationData != null) {
                connection.addRequestProperty("Authorization" , authenticationData.getTokenType() + " " + authenticationData.getAccessToken());
            }
            connection.addRequestProperty("Accept", "application/json");
            connection.addRequestProperty("Content-Type", "application/json" );

            connection.connect();

            if (connection.getResponseCode() == 401) {
                authenticate(username, password);
                return sendGet(url);
            }
            if (connection.getResponseCode() != 200) {
                System.out.println("Troubles with the request, please check, " + url + " " + connection.getResponseMessage() );
                return "";
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
            System.out.println("Troubles with the url: " + url);
            return "";
        } catch (ProtocolException e) {
            System.out.println("Troubles reaching the service, please check service status");
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        } catch (IOException e) {
            try {
                String s = (connection != null) ? connection.getResponseMessage() : "";
                System.out.println("Troubles with IO : " + s);
            } catch (Exception e1){

            }
            return "";
        }
        return result;
    }

    private String sendCredentials(String url, HashMap<String, String> headers) {
        URL serverAddress;
        HttpURLConnection connection;
        BufferedReader br;
        String result = "";
        try {
            serverAddress = new URL(url);
            //connection = (HttpsURLConnection) serverAddress.openConnection();
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
            return "";
        } catch (ProtocolException e) {
            System.out.println("Please check if the service is up");
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        } catch (IOException e) {
            return "";
        }
        return result;
    }

    private Link getAuthLink() {
        return new Link("authenticate", "/tokens", "POST");
    }

    private static String createJsonFromDto(Object item) {
        return gson.toJson(item);
    }

    public Link getRoot() {
        return new Link("self", baseUrl, "GET");
    }
}

