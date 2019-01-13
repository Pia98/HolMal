package com.holmal.app.holmal.model;

public class User {

    public String getEmailAdresse() {
        return emailAdresse;
    }

    public void setEmailAdresse(String emailAdresse) {
        this.emailAdresse = emailAdresse;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    //Emailadresse des Users
    private String emailAdresse;

    //ID der Person im Haushalt
    private String personID;

    public User(String emailAdresse, String personID){
        this.emailAdresse = emailAdresse;
        this.personID = personID;
    }

    @Override
    public String toString() {
        return "User{" +
                "emailAdresse='" + emailAdresse + '\'' +
                ", personID=" + personID +
                '}';
    }
}
