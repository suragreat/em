package com.suragreat.biz.ai.model;

public class Dish {
    private String probability;
    private boolean hasCalorie;
    private String calorie;
    private String name;

    public String getProbability() {
        return probability;
    }

    public void setProbability(String probability) {
        this.probability = probability;
    }

    public boolean isHasCalorie() {
        return hasCalorie;
    }

    public void setHasCalorie(boolean has_calorie) {
        this.hasCalorie = has_calorie;
    }

    public String getCalorie() {
        return calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
