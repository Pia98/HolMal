package com.holmal.app.holmal.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.holmal.app.holmal.model.Household;
import com.holmal.app.holmal.model.Person;

//Class for handling references to the firebase database
//that are used in multiple other classes
public class FireBaseHandling {

    private PersonListener personListener = new PersonListener();

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference reference = firebaseDatabase.getReference();

    private String householdRubric = "household";

    public String storeNewHousehold(Household household) {
        String storeId = reference.push().getKey();
        reference.child(householdRubric).child(storeId).setValue(household);

        //listener fuer personen und einkaufsliste gleich starten, wenn Haushalt erstellt wird

        startPersonValueEventListener(storeId);

        return storeId;
    }

   /* public void storeNewTestHousehold(String name, Person person){
        ArrayList<Person> personen = new ArrayList<>();
        personen.add(person);
        TestHoushold household = new TestHoushold(name, personen);

        DatabaseReference myRef = firebaseDatabase.getReference();
        myRef.child("haushalt").push().setValue(household);
    }*/

    public void storePersonOnDatabase(String name, int color) {
        Person person = new Person(name, color);
        reference.child("person").push().setValue(person);
    }
    
    public void storeMoveInPersonInHousehold(String id, Person person){
        DatabaseReference personRef = firebaseDatabase.getReference();
        personRef.child(householdRubric + "/" + id + "/personInHousehold").push().setValue(person);
        // listener fuer einkaufsliste starten, wenn beitretende Person erfolgreich gespeichert wurde
    }

    // registriere listener unter household/id/personenInHousehold
    public void startPersonValueEventListener(String householdId) {
        reference.child(householdRubric + "/" + householdId + "/personInHousehold")
                .addValueEventListener(personListener);
    }

    public PersonListener getPersonListener() {
        return personListener;
    }
}
