package com.example.personalitytest.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.personalitytest.models.Profile;
import com.example.personalitytest.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ApiClient {
    private static final int CONNECT_TIMEOUT = 15000; // 15 Sekunden
    private static final int READ_TIMEOUT = 10000;    // 10 Sekunden
    
    private final Context context;
    private final String baseUrl;
    
    public ApiClient(Context context, String baseUrl) {
        this.context = context;
        this.baseUrl = baseUrl;
    }
    
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = 
            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    
    public JSONObject login(String username, String password) throws IOException, JSONException {
        String url = baseUrl + "login.php?username=" + username + "&password=" + password;
        return getJsonFromUrl(url);
    }
    
    public JSONObject register(User user) throws IOException, JSONException {
        String url = baseUrl + "register.php";
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("username", user.getUsername());
        jsonInput.put("email", user.getEmail());
        jsonInput.put("password", user.getPasswordHash());
        
        return postJsonToUrl(url, jsonInput);
    }
    
    public JSONObject saveProfile(Profile profile) throws IOException, JSONException {
        String url = baseUrl + "saveProfile.php";
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("user_id", profile.getUserId());
        jsonInput.put("answers", profile.getAnswers());
        
        if (profile.getId() > 0) {
            jsonInput.put("profile_id", profile.getId());
        }
        
        return postJsonToUrl(url, jsonInput);
    }
    
    public JSONObject getNormTable() throws IOException, JSONException {
        String url = baseUrl + "readNormTable.php";
        return getJsonFromUrl(url);
    }
    
    public JSONObject getUserProfiles(int userId) throws IOException, JSONException {
        String url = baseUrl + "getProfiles.php?user_id=" + userId;
        return getJsonFromUrl(url);
    }
    
    private JSONObject getJsonFromUrl(String urlString) throws IOException, JSONException {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = conn.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                
                return new JSONObject(response.toString());
            } else {
                throw new IOException("HTTP error code: " + responseCode);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
    
    private JSONObject postJsonToUrl(String urlString, JSONObject jsonInput) throws IOException, JSONException {
        HttpURLConnection conn = null;
        OutputStream os = null;
        BufferedReader reader = null;
        
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            
            os = conn.getOutputStream();
            os.write(jsonInput.toString().getBytes(StandardCharsets.UTF_8));
            os.flush();
            
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = conn.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                
                return new JSONObject(response.toString());
            } else {
                throw new IOException("HTTP error code: " + responseCode);
            }
        } finally {
            if (os != null) {
                os.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}