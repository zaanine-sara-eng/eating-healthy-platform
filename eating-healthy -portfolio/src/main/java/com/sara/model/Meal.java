package com.sara.model;

import java.sql.Date;

public class Meal {
    private int mealId;
    private int coachId;
    private String mealType;
    private String mealName;
    private Date mealDate;

    public Meal() {}

    public Meal(int mealId, int coachId, String mealType, String mealName, Date mealDate) {
        this.mealId = mealId;
        this.coachId = coachId;
        this.mealType = mealType;
        this.mealName = mealName;
        this.mealDate = mealDate;
    }

    public int getMealId() { return mealId; }
    public void setMealId(int mealId) { this.mealId = mealId; }

    public int getCoachId() { return coachId; }
    public void setCoachId(int coachId) { this.coachId = coachId; }

    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }

    public String getMealName() { return mealName; }
    public void setMealName(String mealName) { this.mealName = mealName; }

    public Date getMealDate() { return mealDate; }
    public void setMealDate(Date mealDate) { this.mealDate = mealDate; }
}
