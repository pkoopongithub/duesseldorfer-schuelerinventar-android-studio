package com.example.personalitytest;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalitytest.adapters.ProfileAdapter;
import com.example.personalitytest.models.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProfileListActivity extends AppCompatActivity implements ProfileAdapter.OnProfileClickListener {
    private RecyclerView rvProfiles;
    private ProgressBar progressBar;
    private ProfileAdapter adapter;
    private List<Profile> profiles = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_list);
        
        rvProfiles = findViewById(R.id.rvProfiles);
        progressBar = findViewById(R.id.progressBar);
        
        rvProfiles.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProfileAdapter(profiles, this);
        rvProfiles.setAdapter(adapter);
        
        loadProfiles();
    }
    
    private void loadProfiles() {
        int userId = ((PersonalityTestApp) getApplication()).getCurrentUser().getId();
        new LoadProfilesTask().execute(userId);
    }
    
    @Override
    public void onProfileClick(Profile profile) {
        ((PersonalityTestApp) getApplication()).setCurrentProfile(profile);
        startActivity(new Intent(this, ProfileActivity.class)
            .putExtra("profile_id", profile.getId()));
    }
    
    @Override
    public void onProfileDelete(Profile profile) {
        new DeleteProfileTask().execute(profile.getId());
    }
    
    private class LoadProfilesTask extends AsyncTask<Integer, Void, List<Profile>> {
        private String errorMessage;
        
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }
        
        @Override
        protected List<Profile> doInBackground(Integer... userIds) {
            int userId = userIds[0];
            List<Profile> result = new ArrayList<>();
            
            try {
                String urlStr = ((PersonalityTestApp) getApplication()).getApiBaseUrl() + 
                    "getProfiles.php?user_id=" + userId;
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
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject item = data.getJSONObject(i);
                            Profile profile = new Profile();
                            profile.setId(item.getInt("id"));
                            profile.setName(item.getString("name"));
                            
                            Date createdAt = dateFormat.parse(item.getString("created_at"));
                            profile.setCreatedAt(createdAt);
                            
                            result.add(profile);
                        }
                    }
                }
            } catch (IOException | JSONException | java.text.ParseException e) {
                errorMessage = e.getMessage();
            }
            return result;
        }
        
        @Override
        protected void onPostExecute(List<Profile> result) {
            progressBar.setVisibility(View.GONE);
            
            if (!result.isEmpty()) {
                profiles.clear();
                profiles.addAll(result);
                adapter.notifyDataSetChanged();
            } else if (errorMessage != null) {
                Toast.makeText(ProfileListActivity.this, 
                    "Fehler beim Laden: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        }
    }
    
    private class DeleteProfileTask extends AsyncTask<Integer, Void, Boolean> {
        private String errorMessage;
        private int deletedProfileId;
        
        @Override
        protected Boolean doInBackground(Integer... profileIds) {
            deletedProfileId = profileIds[0];
            
            try {
                String urlStr = ((PersonalityTestApp) getApplication()).getApiBaseUrl() + 
                    "deleteProfile.php?profile_id=" + deletedProfileId;
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("DELETE");
                
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
                    return jsonResponse.getString("status").equals("success");
                }
            } catch (IOException | JSONException e) {
                errorMessage = e.getMessage();
            }
            return false;
        }
        
        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                // Profil aus der Liste entfernen
                for (int i = 0; i < profiles.size(); i++) {
                    if (profiles.get(i).getId() == deletedProfileId) {
                        profiles.remove(i);
                        adapter.notifyItemRemoved(i);
                        break;
                    }
                }
                Toast.makeText(ProfileListActivity.this, 
                    "Profil gelöscht", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ProfileListActivity.this, 
                    errorMessage != null ? "Fehler: " + errorMessage : "Löschen fehlgeschlagen", 
                    Toast.LENGTH_LONG).show();
            }
        }
    }
}