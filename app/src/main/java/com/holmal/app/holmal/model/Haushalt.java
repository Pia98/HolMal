package com.holmal.app.holmal.model;

public class Haushalt {
    private String haushaltName;
    private String id;
    private Person[] personenImHaushalt;
    private Einkaufsliste[] einkaufslisten;

    public Haushalt(String haushaltName, String id, Person[] personenImHaushalt, Einkaufsliste[] einkaufslisten) {
        this.haushaltName = haushaltName;
        this.id = id;
        this.personenImHaushalt = personenImHaushalt;
        this.einkaufslisten = einkaufslisten;
    }

    public String getHaushaltName() {
        return haushaltName;
    }

    public void setHaushaltName(String haushaltName) {
        this.haushaltName = haushaltName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Person[] getPersonenImHaushalt() {
        return personenImHaushalt;
    }

    public void setPersonenImHaushalt(Person[] personenImHaushalt) {
        this.personenImHaushalt = personenImHaushalt;
    }

    public Einkaufsliste[] getEinkaufslisten() {
        return einkaufslisten;
    }

    public void setEinkaufslisten(Einkaufsliste[] einkaufslisten) {
        this.einkaufslisten = einkaufslisten;
    }
}
