package com.fitnessapp.workout_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@SpringBootApplication
public class WorkoutApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkoutApiApplication.class, args);
    }

    @Bean
    CommandLineRunner start(WorkoutRepository repo) {
        return args -> {
            if(repo.count() == 0) { // Only add if table is empty

                repo.save(new Workout(
                        "Abs Crush",
                        "30s Plank, 15 Crunches",
                        "http://localhost:8081/vidios/vid_one_crunch.mp4"
                ));

                repo.save(new Workout(
                        "Core Burner",
                        "20 Russian Twists, 30s Mountain Climbers",
                        "http://localhost:8081/vidios/vid_one_crunch.mp4"
                ));

                repo.save(new Workout(
                        "Leg Blast",
                        "15 Squats, 12 Lunges",
                        "http://localhost:8081/vidios/squat.mp4"
                ));

                repo.save(new Workout(
                        "Glute Fire",
                        "20 Glute Bridges, 15 Donkey Kicks",
                        "http://localhost:8081/vidios/squat.mp4"
                ));

                repo.save(new Workout(
                        "Upper Power",
                        "10 Push-ups, 20 Arm Circles",
                        "https://example.com/upper-power.gif"
                ));

                repo.save(new Workout(
                        "Full Body Spark",
                        "15 Jumping Jacks, 10 Squats",
                        "http://localhost:8081/vidios/jumping_jacks.mp4"
                ));

                repo.save(new Workout(
                        "Cardio Rush",
                        "30s High Knees, 20 Jumping Jacks",
                        "http://localhost:8081/vidios/jumping_jacks.mp4"
                ));

                repo.save(new Workout(
                        "Balance Boost",
                        "30s Single-Leg Stand, 20 Standing Knee Raises",
                        "http://localhost:8081/vidios/squat.mp4"
                ));

                repo.save(new Workout(
                        "Stretch & Chill",
                        "30s Hamstring Stretch, 30s Shoulder Stretch",
                        "http://localhost:8081/vidios/squat.mp4"
                ));

                repo.save(new Workout(
                        "Quick Energy",
                        "20 March-in-Place, 15 Bodyweight Squats",
                        "http://localhost:8081/vidios/squat.mp4"
                ));

                System.out.println("Workouts Database Populated!");
           }
        };
    }

}