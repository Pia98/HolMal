package com.holmal.app.holmal.model;

public class Household {
    private String householdName;
    private String id;
    private Person[] personInHousehold;
    private ShoppingList[] shoppingLists;

    public Household(String householdName, String id, Person[] personInHousehold, ShoppingList[] shoppingLists) {
        this.householdName = householdName;
        this.id = id;
        this.personInHousehold = personInHousehold;
        this.shoppingLists = shoppingLists;
    }

    public String getHouseholdName() {
        return householdName;
    }

    public void setHouseholdName(String householdName) {
        this.householdName = householdName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
