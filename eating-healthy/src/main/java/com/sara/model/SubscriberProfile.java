package com.sara.model;

public class SubscriberProfile {
    
    private int subscriberId;
    private double weight;
    private int height;
    private String allergies;

    // Default Constructor
    public SubscriberProfile() {
    }

    // Getters
    public int getSubscriberId() {
        return subscriberId;
    }

    public double getWeight() {
        return weight;
    }

    public int getHeight() {
        return height;
    }

    public String getAllergies() {
        return allergies;
    }

    // Setters
    public void setSubscriberId(int subscriberId) {
        this.subscriberId = subscriberId;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    // Optional: toString for easy debugging
    @Override
    public String toString() {
        return "SubscriberProfile{" +
               "subscriberId=" + subscriberId +
               ", weight=" + weight +
               ", height=" + height +
               ", allergies='" + allergies + '\'' +
               '}';
    }
}