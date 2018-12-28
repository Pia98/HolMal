package com.holmal.app.holmal.utils;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.holmal.app.holmal.model.Household;
import com.holmal.app.holmal.model.Person;

import java.util.ArrayList;

//Class for handling references to the firebase database
//that are used in multiple other classes
public class FireBaseHandling {

    PersonListener personListener = new PersonListener();

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    String householdRubric = "household";

    public String storeNewHousehold(Household household) {
        DatabaseReference householdRef = firebaseDatabase.getReference();

        String storeId = householdRef.push().getKey();
        householdRef.child(householdRubric).child(storeId).setValue(household);

        // registriere listener unter household/id/personenInHousehold
        householdRef.child(householdRubric + "/" + storeId + "/personInHousehold").addValueEventListener(personListener);

        return storeId;

        //listener fuer personen (und einkaufsliste)
    }


    // registriere Listener fuer Personen bei Beitritt


   /* public void storeNewTestHousehold(String name, Person person){
        ArrayList<Person> personen = new ArrayList<>();
        personen.add(person);
        TestHoushold household = new TestHoushold(name, personen);

        DatabaseReference myRef = firebaseDatabase.getReference();
        myRef.child("haushalt").push().setValue(household);
    }*/

    public void storePersonOnDatabase(String name, int color) {
        Person person = new Person(name, color);


        DatabaseReference personRef = firebaseDatabase.getReference();
        personRef.child("person").push().setValue(person);
    }

    /*public void getHouseholdFromID(String id) {
        DatabaseReference householdRef = firebaseDatabase.getReference(id);


        // wie bekomm ich hier Daten zurueck, bzw weiter?
        householdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Household household = dataSnapshot.getValue(Household.class);
                household.getPersonInHousehold();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
/*
        householdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Household household = dataSnapshot.getValue(Household.class);
                System.out.println(household);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
*/

    //}

    public void storeMoveInPersonInHousehold(String id, Person person){
        DatabaseReference personRef = firebaseDatabase.getReference();
        personRef.child(householdRubric + "/" + id + "/personInHousehold").push().setValue(person);
        // listener fuer einkaufsliste starten
    }
}
