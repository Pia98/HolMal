package com.holmal.app.holmal.model;

import java.util.ArrayList;

public class Person {
    private String personName;

    /**@color */
    private int color;

    private String email;

    public Person(String personName, int color, String email){
        this.personName = personName;
        this.color = color;
        this.email = email;
    }

    public Person() {
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getEmail(){return email;}

    public void setEmail(String email){this.email = email;}

    @Override
    public String toString() {
        return "Person{" +
                "personName='" + personName + '\'' +
                ", color=" + color +
                '}';
    }
}
