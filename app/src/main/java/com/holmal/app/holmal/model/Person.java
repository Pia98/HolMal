package com.holmal.app.holmal.model;

import java.util.ArrayList;

public class Person {
    private String personName;

    /**@color */
    private String color;
    private ArrayList<Haushalt> haushalteEinerPerson;

    public Person(String personName, String color, ArrayList<Haushalt> haushalteEinerPerson) {
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

    public ArrayList<Haushalt> getHaushalteEinerPerson() {
        return haushalteEinerPerson;
    }

    public void setHaushalteEinerPerson(ArrayList<Haushalt> haushalteEinerPerson) {
        this.haushalteEinerPerson = haushalteEinerPerson;
    }

    public void addHaushalt(Haushalt haushalt){
        haushalteEinerPerson.add(haushalt);
    }
}
