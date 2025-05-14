package com.example.personalitytest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button btnNewTest = findViewById(R.id.btnNewTest);
        Button btnViewProfiles = findViewById(R.id.btnViewProfiles);
        Button btnAdmin = findViewById(R.id.btnAdmin);
        Button btnLogout = findViewById(R.id.btnLogout);

        // Admin-Button nur anzeigen, wenn Benutzer Admin ist
        btnAdmin.setVisibility(
            ((PersonalityTestApp) getApplication()).getCurrentUser().isAdmin() 
            ? View.VISIBLE : View.GONE
        );

        btnNewTest.setOnClickListener(v -> startNewQuestionnaire());
        btnViewProfiles.setOnClickListener(v -> viewProfiles());
        btnAdmin.setOnClickListener(v -> startAdminPanel());
        btnLogout.setOnClickListener(v -> logout());
    }

    private void startNewQuestionnaire() {
        Profile newProfile = new Profile();
        newProfile.setUserId(((PersonalityTestApp) getApplication()).getCurrentUser().getId());
        ((PersonalityTestApp) getApplication()).setCurrentProfile(newProfile);
        
        Intent intent = new Intent(this, QuestionnaireActivity.class);
        intent.putExtra("is_new_profile", true);
        startActivity(intent);
    }

    private void viewProfiles() {
        startActivity(new Intent(this, ProfileListActivity.class));
    }

    private void startAdminPanel() {
        startActivity(new Intent(this, AdminActivity.class));
    }

    private void logout() {
        ((PersonalityTestApp) getApplication()).setCurrentUser(null);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}