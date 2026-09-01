package com.sara.model;

public class Coach {
    private int coachId;
    private String name;
    private String email;
    private String password;

    public Coach() {}

    public Coach(int coachId, String name, String email, String password) {
        this.coachId = coachId;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public int getCoachId() {
        return coachId;
    }

    public void setCoachId(int coachId) {
        this.coachId = coachId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
