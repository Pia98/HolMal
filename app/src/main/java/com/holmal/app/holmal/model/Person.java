package com.holmal.app.holmal.model;

public class Person {
    private String personName;

    /**@color */
    private String color;
    private Haushalt[] haushalteEinerPerson;

    public Person(String personName, String color, Haushalt[] haushalteEinerPerson) {
        this.personName = personName;
        this.color = color;
        this.haushalteEinerPerson = haushalteEinerPerson;
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

    public Haushalt[] getHaushalteEinerPerson() {
        return haushalteEinerPerson;
    }

    public void setHaushalteEinerPerson(Haushalt[] haushalteEinerPerson) {
        this.haushalteEinerPerson = haushalteEinerPerson;
    }
}
