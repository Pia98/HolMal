package com.holmal.app.holmal.model;

public class User {

    //Emailadresse des Users
    private String emailAdresse;

    //ID der Person im Haushalt
    private String personID;

    /**
     * Constructor
     * @param emailAdresse
     * @param personID
     */
    public User(String emailAdresse, String personID){
        this.emailAdresse = emailAdresse;
        this.personID = personID;
    }

    /**
     * Getter and Setter
     */
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

    @Override
    public String toString() {
        return "User{" +
                "emailAdresse='" + emailAdresse + '\'' +
                ", personID=" + personID +
                '}';
    }
}
