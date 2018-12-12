package com.holmal.app.holmal.model;

import java.util.ArrayList;

public class TestHoushold {

    private String householdName;
    private ArrayList<Person> personInHousehold;

    public TestHoushold(String householdName ,ArrayList<Person> personInHousehold) {
        this.householdName = householdName;
        this.personInHousehold = personInHousehold;
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

}
