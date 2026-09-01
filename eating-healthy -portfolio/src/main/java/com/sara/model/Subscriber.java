package com.sara.model;

public class Subscriber {
    
    // Primary Key Field
    private int subscriberId; 
    
    private String name;
    private String email;
    private String password;
    private int age;
    private String goal;
    
    // Coach Identification
    private int coachId;     // For database lookup (integer)
    private String coach;    // Legacy field for coach name/string ID

    // 💥 NEW: Profile Fields for the Dashboard List View
    private double weight;
    private int height;
    private String allergies;

    public Subscriber() {}

    // Constructor
    public Subscriber(String name, String email, String password, int age, String goal, String coach) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
        this.goal = goal;
        this.coach = coach;
    }

    // ------------------------------------
    // ID GETTERS AND SETTERS
    // ------------------------------------
    
    public int getSubscriberId() { 
        return subscriberId; 
    }
    public void setSubscriberId(int subscriberId) { 
        this.subscriberId = subscriberId; 
    }

    public int getCoachId() {
        return coachId;
    }
    public void setCoachId(int coachId) {
        this.coachId = coachId;
    }

    // ------------------------------------
    // 💥 NEW PROFILE GETTERS AND SETTERS
    // ------------------------------------

    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }

    public String getAllergies() {
        return allergies;
    }
    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    // ------------------------------------
    // BASIC INFO GETTERS AND SETTERS
    // ------------------------------------
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGoal() { return goal; }
    public void setGoal(String goal) { this.goal = goal; }

    public String getCoach() { return coach; }
    public void setCoach(String coach) { this.coach = coach; }
}