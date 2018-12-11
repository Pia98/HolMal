package com.holmal.app.holmal.model;

public class Person {
    private String personName;

    /**@color */
    private String color;
    private Household[] personsHousehold;

    public Person(String personName, String color, Household[] personsHousehold) {
        this.personName = personName;
        this.color = color;
        this.personsHousehold = personsHousehold;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Household[] getPersonsHousehold() {
        return personsHousehold;
    }

    public void setPersonsHousehold(Household[] personsHousehold) {
        this.personsHousehold = personsHousehold;
    }
}
