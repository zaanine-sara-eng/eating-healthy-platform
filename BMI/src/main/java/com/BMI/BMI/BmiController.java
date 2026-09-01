package com.BMI.BMI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/api/bmi")
@CrossOrigin(origins = "*") // Allows your JSP to call it
public class BmiController {

    @GetMapping("/calculate")
    public Map<String, Object> calculate(@RequestParam double weight, @RequestParam double height) {
        // height is expected in meters (e.g., 1.75)
        double bmi = weight / (height * height);
        String category;

        if (bmi < 18.5) category = "Underweight";
        else if (bmi < 25) category = "Normal weight";
        else if (bmi < 30) category = "Overweight";
        else category = "Obese";

        Map<String, Object> response = new HashMap<>();
        response.put("bmi", Math.round(bmi * 10) / 10.0);
        response.put("category", category);
        response.put("tip", getTip(category));
        
        return response;
    }

    private String getTip(String category) {
        switch (category) {
            case "Underweight": return "Focus on nutrient-dense foods and strength training.";
            case "Normal weight": return "Great job! Maintain your current activity level.";
            case "Overweight": return "Try increasing your daily steps and monitoring portions.";
            default: return "Consult with a health professional for a tailored plan.";
        }
    }
}