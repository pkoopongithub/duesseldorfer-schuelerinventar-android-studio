package com.example.personalitytest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.personalitytest.utils.PreferencesHelper;

public abstract class BaseActivity extends AppCompatActivity {
    protected PreferencesHelper preferencesHelper;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesHelper = new PreferencesHelper(this);
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        checkAuthentication();
    }
    
    protected void checkAuthentication() {
        if (!(this instanceof LoginActivity || this instanceof RegisterActivity)) {
            if (!preferencesHelper.isLoggedIn()) {
                redirectToLogin();
            }
        }
    }
    
    protected void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    
    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    
    protected void showLongToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}