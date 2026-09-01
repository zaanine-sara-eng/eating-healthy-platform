package com.fitnessapp.workout_api;
import jakarta.persistence.*;
@Entity
public class Workout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String exercises; 
    private String videoUrl;
    public Workout() {} 

    public Workout(String title, String exercises, String videoUrl) {
        this.title = title;
        this.exercises = exercises;
        this.videoUrl=videoUrl;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getExercises() { return exercises; }
    public void setExercises(String exercises) { this.exercises = exercises; }
    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    
    
}