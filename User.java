package com.example.personalitytest.models;

import java.util.Date;

public class User {
    private int id;
    private String username;
    private String email;
    private String passwordHash;
    private boolean isAdmin;
    private Date createdAt;
    private Date lastLogin;
    
    // Konstruktor
    public User(int id, String username, String email, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.isAdmin = isAdmin;
        this.createdAt = new Date();
    }
    
    // Getter und Setter
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public boolean isAdmin() { return isAdmin; }
    public Date getCreatedAt() { return createdAt; }
    public Date getLastLogin() { return lastLogin; }
    
    public void setLastLogin(Date lastLogin) { this.lastLogin = lastLogin; }
    public void setPasswordHash(String hash) { this.passwordHash = hash; }
    
    // Weitere Methoden
	public boolean checkPassword(String password) {
	    return BCrypt.checkpw(password, this.passwordHash);
	}
}