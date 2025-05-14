package com.example.personalitytest.models;

import java.util.Date;

public class Profile {
    private int id;
    private int userId;
    private String name;
    private Date createdAt;
    private Date modifiedAt;
    private int[] answers;
    private int[] competenceScores;
    
    public Profile() {
        this.answers = new int[36];
        this.competenceScores = new int[6];
    }
    
    // Getter und Setter
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getName() { return name; }
    public Date getCreatedAt() { return createdAt; }
    public Date getModifiedAt() { return modifiedAt; }
    public int[] getAnswers() { return answers; }
    public int[] getCompetenceScores() { return competenceScores; }
    
    public void setId(int id) { this.id = id; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setName(String name) { this.name = name; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public void setModifiedAt(Date modifiedAt) { this.modifiedAt = modifiedAt; }
    
    // Methoden zur Berechnung der Kompetenzwerte
    public void calculateScores(double[][] normTable) {
        // Arbeitsverhalten (Items 1-10)
        competenceScores[0] = calculateCompetence(0, new int[]{0,1,2,3,4,5,6,7,8,9}, normTable[0]);
        
        // Lernverhalten (Items 11-20)
        competenceScores[1] = calculateCompetence(1, new int[]{10,11,12,13,14,15,16,17,18,19}, normTable[1]);
        
        // Sozialverhalten (Items 21-28 + 8,9)
        competenceScores[2] = calculateCompetence(2, new int[]{20,21,22,23,24,25,26,27,8,9}, normTable[2]);
        
        // Fachkompetenz (Items 29-36)
        competenceScores[3] = calculateCompetence(3, new int[]{28,29,30,31,32,33,34,35}, normTable[3]);
        
        // Personale Kompetenz
        competenceScores[4] = calculateCompetence(4, new int[]{0,1,5,6,7,8,9,10,11,13,14}, normTable[4]);
        
        // Methodenkompetenz
        competenceScores[5] = calculateCompetence(5, new int[]{2,3,4,8,9,10,16,17}, normTable[5]);
    }
    
    private int calculateCompetence(int index, int[] itemIndices, double[] normValues) {
        int sum = 0;
        for (int i : itemIndices) {
            sum += answers[i];
        }
        
        for (int i = 0; i < normValues.length; i++) {
            if (sum < normValues[i]) {
                return i + 1;
            }
        }
        return normValues.length;
    }
}