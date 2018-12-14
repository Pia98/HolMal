package com.holmal.app.holmal.model;

import java.util.ArrayList;

public class Person {
    private String personName;

    /**@color */
    private String color;

    public Person(String personName, String color) {
        this.personName = personName;
        this.color = color;
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
}
