package com.holmal.app.holmal.model;

import java.util.ArrayList;

public class Person {
    private String personName;

    /**@color */
    private int color;

    private String idBelongingTo;

    public Person(String personName, int color, String idBelongingTo) {
        this.personName = personName;
        this.color = color;
        this.idBelongingTo = idBelongingTo;
    }

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

    public String getIdBelongingTo() {
        return idBelongingTo;
    }

    public void setIdBelongingTo(String idBelongingTo) {
        this.idBelongingTo = idBelongingTo;
    }

    @Override
    public String toString() {
        return "Person{" +
                "personName='" + personName + '\'' +
                ", color=" + color +
                ", idBelongingTo='" + idBelongingTo + '\'' +
                '}';
    }
}
