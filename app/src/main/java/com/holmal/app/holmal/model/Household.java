package com.holmal.app.holmal.model;


public class Household {
    private String householdName;

    public Household(String householdName) {
        this.householdName = householdName;
    }

    public Household() {
    }

    public String getHouseholdName() {
        return householdName;
    }

    public void setHouseholdName(String householdName) {
        this.householdName = householdName;
    }
}
