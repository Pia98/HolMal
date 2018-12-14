package com.holmal.app.holmal.model;

public class Household {
    private String householdName;
    private Person[] personInHousehold;
    private ShoppingList[] shoppingLists;

    /**
     * Used for creating a new household
     * @param householdName
     * @param personInHousehold
     */
    public Household(String householdName, Person[] personInHousehold){
        this.householdName = householdName;
        this.personInHousehold = personInHousehold;
        this.shoppingLists = new ShoppingList[] {};
    }

    public Household(String householdName, Person[] personInHousehold, ShoppingList[] shoppingLists) {
        this.householdName = householdName;
        this.personInHousehold = personInHousehold;
        this.shoppingLists = shoppingLists;
    }

    public String getHouseholdName() {
        return householdName;
    }

    public void setHouseholdName(String householdName) {
        this.householdName = householdName;
    }

    public Person[] getPersonInHousehold() {
        return personInHousehold;
    }

    public void setPersonInHousehold(Person[] personInHousehold) {
        this.personInHousehold = personInHousehold;
    }

    public ShoppingList[] getShoppingLists() {
        return shoppingLists;
    }

    public void setShoppingListsn(ShoppingList[] shoppingLists) {
        this.shoppingLists = shoppingLists;
    }
}
