package com.example.personalitytest.models;

public class NormTableItem {
    private int id;
    private String competenceName;
    private double p1, p2, p3, p4, p5;
    private boolean modified;
    
    public NormTableItem(int id, String competenceName, double p1, double p2, double p3, double p4, double p5) {
        this.id = id;
        this.competenceName = competenceName;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
        this.p5 = p5;
        this.modified = false;
    }
    
    // Getter
    public int getId() { return id; }
    public String getCompetenceName() { return competenceName; }
    public double getP1() { return p1; }
    public double getP2() { return p2; }
    public double getP3() { return p3; }
    public double getP4() { return p4; }
    public double getP5() { return p5; }
    public boolean isModified() { return modified; }
    
    // Setter mit modified-Flag
    public void setP1(double p1) { 
        if (this.p1 != p1) modified = true;
        this.p1 = p1; 
    }
    // ... gleiches für p2 bis p5
}