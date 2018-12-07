package com.holmal.app.holmal.model;

import java.util.ArrayList;
import java.util.List;

public class Haushalt {
    private String haushaltName;
    private String id;
    private ArrayList<Person> personenImHaushalt = new ArrayList<Person>();
    private Einkaufsliste[] einkaufslisten;

    public Haushalt(String haushaltName, Person ersteller){
        this.haushaltName = haushaltName;
        personenImHaushalt.add(ersteller);
    }

    public Haushalt(String haushaltName, String id, ArrayList<Person> personenImHaushalt, Einkaufsliste[] einkaufslisten) {
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

    public List<Person> getPersonenImHaushalt() {

        return personenImHaushalt;
    }


    public Einkaufsliste[] getEinkaufslisten() {
        return einkaufslisten;
    }

    public void setEinkaufslisten(Einkaufsliste[] einkaufslisten) {
        this.einkaufslisten = einkaufslisten;
    }

    public void addPerson(Person person){
        personenImHaushalt.add(person);
    }
}
