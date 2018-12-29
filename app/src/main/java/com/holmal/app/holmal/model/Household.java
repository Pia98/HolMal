package com.holmal.app.holmal.model;

import java.util.ArrayList;

public class Household {
    private String householdName;
    private ArrayList<Person> personInHousehold;
    private ArrayList<ShoppingList> shoppingLists;

    /**
     * Used for creating a new household
     * @param householdName
     * @param personInHousehold
     */
    public Household(String householdName, ArrayList<Person> personInHousehold){
        this.householdName = householdName;
        this.personInHousehold = personInHousehold;
        this.shoppingLists = new ArrayList<ShoppingList>();
    }

    public Household(String householdName, ArrayList<Person> personInHousehold, ArrayList<ShoppingList> shoppingLists) {
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

    public ArrayList<Person> getPersonInHousehold() {
        return personInHousehold;
    }

    public void setPersonInHousehold(ArrayList<Person> personInHousehold) {
        this.personInHousehold = personInHousehold;
    }

    public ArrayList<ShoppingList> getShoppingLists() {
        return shoppingLists;
    }

    public void setShoppingListsn(ArrayList<ShoppingList> shoppingLists) {
        this.shoppingLists = shoppingLists;
    }
}
