package com.holmal.app.holmal.model;

/**
 * Model for the person.
 */
public class Person {

    private String personName;
    private int color;
    private String idBelongingTo;
    private String email;

    /**
     * Constructor for the person
     * @param personName every user chooses a name (that is unique within the household)
     * @param color every person chooses one of eight colours
     * @param idBelongingTo every person belongs to a household
     * @param email a person needs an email address to log in
     */
    public Person(String personName, int color, String idBelongingTo, String email) {
        this.personName = personName;
        this.color = color;
        this.idBelongingTo = idBelongingTo;
        this.email = email;
    }

    public Person(){
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
                ", email='" + email + '\'' +
                '}';
    }
}
