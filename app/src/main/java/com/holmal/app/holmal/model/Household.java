package com.holmal.app.holmal.model;

import java.util.ArrayList;

public class Household {
    private String householdName;
    private ArrayList<String> personInHousehold;
    private ArrayList<ShoppingList> shoppingLists;

    /**
     * Used for creating a new household
     * @param householdName
     * @param personInHousehold
     */
    public Household(String householdName, ArrayList<String> personInHousehold){
        this.householdName = householdName;
        this.personInHousehold = personInHousehold;
        this.shoppingLists = new ArrayList<ShoppingList>();
    }

    public Household(String householdName, ArrayList<String> personInHousehold, ArrayList<ShoppingList> shoppingLists) {
        this.householdName = householdName;
        this.personInHousehold = personInHousehold;
        this.shoppingLists = shoppingLists;
    }

    public Household() {
    }

    public String getHouseholdName() {
        return householdName;
    }

    public void setHouseholdName(String householdName) {
        this.householdName = householdName;
    }

    public ArrayList<String> getPersonInHousehold() {
        return personInHousehold;
    }

    public void setPersonInHousehold(ArrayList<String> personInHousehold) {
        this.personInHousehold = personInHousehold;
    }

    public ArrayList<ShoppingList> getShoppingLists() {
        return shoppingLists;
    }

    public void setShoppingListsn(ArrayList<ShoppingList> shoppingLists) {
        this.shoppingLists = shoppingLists;
    }
}
