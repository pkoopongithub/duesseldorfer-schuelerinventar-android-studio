package com.example.personalitytest.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.personalitytest.models.Profile;
import com.example.personalitytest.models.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "personality_test.db";
    private static final int DATABASE_VERSION = 1;
    
    // Tabellennamen
    private static final String TABLE_USERS = "users";
    private static final String TABLE_PROFILES = "profiles";
    
    // Spalten für users
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD_HASH = "password_hash";
    private static final String KEY_IS_ADMIN = "is_admin";
    private static final String KEY_LAST_LOGIN = "last_login";
    
    // Spalten für profiles
    private static final String KEY_PROFILE_ID = "id";
    private static final String KEY_USER_ID_FK = "user_id";
    private static final String KEY_PROFILE_NAME = "name";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_MODIFIED_AT = "modified_at";
    
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USERNAME + " TEXT UNIQUE,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_PASSWORD_HASH + " TEXT,"
                + KEY_IS_ADMIN + " INTEGER DEFAULT 0,"
                + KEY_LAST_LOGIN + " DATETIME"
                + ")";
        
        String CREATE_PROFILES_TABLE = "CREATE TABLE " + TABLE_PROFILES + "("
                + KEY_PROFILE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USER_ID_FK + " INTEGER,"
                + KEY_PROFILE_NAME + " TEXT,"
                + KEY_CREATED_AT + " DATETIME,"
                + KEY_MODIFIED_AT + " DATETIME,"
                + "FOREIGN KEY(" + KEY_USER_ID_FK + ") REFERENCES " + TABLE_USERS + "(" + KEY_USER_ID + ")"
                + ")";
        
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_PROFILES_TABLE);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }
    
    // User-Methoden
    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_PASSWORD_HASH, user.getPasswordHash());
        values.put(KEY_IS_ADMIN, user.isAdmin() ? 1 : 0);
        
        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }
    
    public User getUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{KEY_USER_ID, KEY_USERNAME, KEY_EMAIL, KEY_IS_ADMIN, KEY_LAST_LOGIN},
                KEY_USERNAME + "=?",
                new String[]{username}, null, null, null, null);
        
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3) == 1
            );
            cursor.close();
            db.close();
            return user;
        }
        db.close();
        return null;
    }
    
    // Profile-Methoden
    public long addProfile(Profile profile) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID_FK, profile.getUserId());
        values.put(KEY_PROFILE_NAME, profile.getName());
        values.put(KEY_CREATED_AT, System.currentTimeMillis());
        values.put(KEY_MODIFIED_AT, System.currentTimeMillis());
        
        long id = db.insert(TABLE_PROFILES, null, values);
        db.close();
        return id;
    }
    
    public List<Profile> getUserProfiles(int userId) {
        List<Profile> profileList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.query(TABLE_PROFILES,
                new String[]{KEY_PROFILE_ID, KEY_PROFILE_NAME, KEY_CREATED_AT, KEY_MODIFIED_AT},
                KEY_USER_ID_FK + "=?",
                new String[]{String.valueOf(userId)}, null, null, KEY_CREATED_AT + " DESC", null);
        
        if (cursor.moveToFirst()) {
            do {
                Profile profile = new Profile();
                profile.setId(cursor.getInt(0));
                profile.setName(cursor.getString(1));
                profile.setCreatedAt(new java.util.Date(cursor.getLong(2)));
                profile.setModifiedAt(new java.util.Date(cursor.getLong(3)));
                profile.setUserId(userId);
                
                profileList.add(profile);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        return profileList;
    }
    
    // Weitere Methoden für Update, Delete, etc.
}