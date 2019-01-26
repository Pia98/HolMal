package com.holmal.app.holmal.model;

/**
 * Model for the household. Shoppinglists and their items are shared between members of a household.
 * A household has a name that can be changed in the settings.
 */
public class Household {

    private String householdName;

    /**
     * Constructor for the household
     * @param householdName the name of the household
     */
    public Household(String householdName) {
        this.householdName = householdName;
    }

    public Household(){
    }

    public String getHouseholdName() {
        return householdName;
    }

    public void setHouseholdName(String householdName) {
        this.householdName = householdName;
    }
}
