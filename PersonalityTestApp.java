package com.example.personalitytest;

import android.app.Application;

import com.example.personalitytest.models.Profile;
import com.example.personalitytest.models.User;

public class PersonalityTestApp extends Application {
    private User currentUser;
    private Profile currentProfile;
    private double[][] normTable;
    private String apiBaseUrl = "https://mein-duesk.org/";
    
    @Override
    public void onCreate() {
        super.onCreate();
        // Hier könnten Sie Initialisierungen durchführen
    }
    
    // Getter und Setter
    public User getCurrentUser() { return currentUser; }
    public void setCurrentUser(User user) { this.currentUser = user; }
    
    public Profile getCurrentProfile() { return currentProfile; }
    public void setCurrentProfile(Profile profile) { this.currentProfile = profile; }
    
    public double[][] getNormTable() { return normTable; }
    public void setNormTable(double[][] normTable) { this.normTable = normTable; }
    
    public String getApiBaseUrl() { return apiBaseUrl; }
}