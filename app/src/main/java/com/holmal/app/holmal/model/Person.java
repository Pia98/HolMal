package com.holmal.app.holmal.model;

public class Person {
    private String personName;

    /**@color */
    private int color;

    public Person(String personName, int color){
        this.personName = personName;
        this.color = color;
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

    @Override
    public String toString() {
        return "Person{" +
                "personName='" + personName + '\'' +
                ", color=" + color +
                '}';
    }
}
