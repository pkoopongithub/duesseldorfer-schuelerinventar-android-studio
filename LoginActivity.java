package com.example.personalitytest;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.personalitytest.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnLogin, btnRegister;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);

        btnLogin.setOnClickListener(v -> attemptLogin());
        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    private void attemptLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (validateInputs(username, password)) {
            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setEnabled(false);
            
            new LoginTask().execute(username, password);
        }
    }

    private boolean validateInputs(String username, String password) {
        if (username.isEmpty()) {
            etUsername.setError("Benutzername erforderlich");
            return false;
        }
        if (password.isEmpty()) {
            etPassword.setError("Passwort erforderlich");
            return false;
        }
        return true;
    }

    private class LoginTask extends AsyncTask<String, Void, User> {
        private String errorMessage;

        @Override
        protected User doInBackground(String... credentials) {
            String username = credentials[0];
            String password = credentials[1];
            
            try {
                String urlStr = ((PersonalityTestApp) getApplication()).getApiBaseUrl() + 
                    "login.php?username=" + username + "&password=" + password;
                
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    JSONObject jsonResponse = new JSONObject(response.toString());
                    if (jsonResponse.getString("status").equals("success")) {
                        int userId = jsonResponse.getInt("user_id");
                        String email = jsonResponse.getString("email");
                        boolean isAdmin = jsonResponse.getBoolean("is_admin");
                        return new User(userId, username, email, isAdmin);
                    } else {
                        errorMessage = jsonResponse.getString("message");
                    }
                }
            } catch (IOException | JSONException e) {
                errorMessage = "Verbindungsfehler: " + e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(User user) {
            progressBar.setVisibility(View.GONE);
            btnLogin.setEnabled(true);
            
            if (user != null) {
                onLoginSuccess(user);
            } else {
                Toast.makeText(LoginActivity.this, 
                    errorMessage != null ? errorMessage : "Login fehlgeschlagen", 
                    Toast.LENGTH_LONG).show();
            }
        }
    }

    private void onLoginSuccess(User user) {
        ((PersonalityTestApp) getApplication()).setCurrentUser(user);
        
        // Normtabelle laden
        new LoadNormTableTask().execute();
    }

    private class LoadNormTableTask extends AsyncTask<Void, Void, double[][]> {
        @Override
        protected double[][] doInBackground(Void... voids) {
            try {
                String urlStr = ((PersonalityTestApp) getApplication()).getApiBaseUrl() + "readNormTable.php";
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    JSONObject jsonResponse = new JSONObject(response.toString());
			if (jsonResponse.getString("status").equals("success")) {
    				JSONArray data = jsonResponse.getJSONArray("data");
    				double[][] normTable = new double[6][5];
    				for (int i = 0; i < data.length(); i++) {
        				JSONArray row = data.getJSONArray(i);
        					for (int j = 0; j < row.length(); j++) {
            					normTable[i][j] = row.getDouble(j);
        				}
    				}
    				return normTable;
			}
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(double[][] normTable) {
            if (normTable != null) {
                ((PersonalityTestApp) getApplication()).setNormTable(normTable);
                startActivity(new Intent(LoginActivity.this, MainMenuActivity.class));
                finish();
            } else {
                Toast.makeText(LoginActivity.this, 
                    "Normtabelle konnte nicht geladen werden", 
                    Toast.LENGTH_LONG).show();
            }
        }
    }
}