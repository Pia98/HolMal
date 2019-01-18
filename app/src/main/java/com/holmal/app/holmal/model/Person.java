package com.holmal.app.holmal.model;

import java.util.ArrayList;

public class Person {
    private String personName;

    /**@color */
    private int color;

    private String idBelongingTo;
    private String email;

    public Person(String personName, int color, String idBelongingTo, String email) {
        this.personName = personName;
        this.color = color;
        this.idBelongingTo = idBelongingTo;
        this.email = email;
    }

    public Person(String personName, int color){}

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
                ", color=" + color + '\'' +
                ", email=" + email +
                '}';
    }
}
