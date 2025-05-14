package com.example.personalitytest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RegisterActivity extends AppCompatActivity {
    private EditText etUsername, etEmail, etPassword, etConfirmPassword;
    private Button btnRegister;
    private ProgressBar progressBar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);
        
        btnRegister.setOnClickListener(v -> attemptRegistration());
    }
    
    private void attemptRegistration() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        
        if (validateInputs(username, email, password, confirmPassword)) {
            progressBar.setVisibility(View.VISIBLE);
            btnRegister.setEnabled(false);
            
            new RegisterTask().execute(username, email, password);
        }
    }
    
    private boolean validateInputs(String username, String email, String password, String confirmPassword) {
        if (username.isEmpty()) {
            etUsername.setError("Benutzername erforderlich");
            return false;
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Gültige E-Mail erforderlich");
            return false;
        }
        if (password.isEmpty() || password.length() < 6) {
            etPassword.setError("Mindestens 6 Zeichen erforderlich");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwörter stimmen nicht überein");
            return false;
        }
        return true;
    }
    
    private class RegisterTask extends AsyncTask<String, Void, Boolean> {
        private String errorMessage;
        private int userId;
        
        @Override
        protected Boolean doInBackground(String... credentials) {
            String username = credentials[0];
            String email = credentials[1];
            String password = credentials[2];
            
            try {
                String urlStr = ((PersonalityTestApp) getApplication()).getApiBaseUrl() + "register.php";
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                
                JSONObject jsonInput = new JSONObject();
                jsonInput.put("username", username);
                jsonInput.put("email", email);
                jsonInput.put("password", password);
                
                OutputStream os = conn.getOutputStream();
                os.write(jsonInput.toString().getBytes(StandardCharsets.UTF_8));
                os.close();
                
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
                        userId = jsonResponse.getInt("user_id");
                        return true;
                    } else {
                        errorMessage = jsonResponse.getString("message");
                    }
                }
            } catch (IOException | JSONException e) {
                errorMessage = "Verbindungsfehler: " + e.getMessage();
            }
            return false;
        }
        
        @Override
        protected void onPostExecute(Boolean success) {
            progressBar.setVisibility(View.GONE);
            btnRegister.setEnabled(true);
            
            if (success) {
                Toast.makeText(RegisterActivity.this, 
                    "Registrierung erfolgreich", Toast.LENGTH_SHORT).show();
                finish(); // Zurück zur Login-Activity
            } else {
                Toast.makeText(RegisterActivity.this, 
                    errorMessage != null ? errorMessage : "Registrierung fehlgeschlagen", 
                    Toast.LENGTH_LONG).show();
            }
        }
    }
}