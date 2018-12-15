package com.holmal.app.holmal.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.holmal.app.holmal.model.Household;
import com.holmal.app.holmal.model.Person;

import java.util.ArrayList;

//Class for handling references to the firebase database
//that are used in multiple other classes
public class FireBaseHandling {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public void storeNewHousehold(String name, Person person){
        ArrayList<Person> personen = new ArrayList<>();
        personen.add(person);
        Household household = new Household(name, personen);
        DatabaseReference householdRef = firebaseDatabase.getReference();
        householdRef.child("houshold").push().setValue(household);
    }

   /* public void storeNewTestHousehold(String name, Person person){
        ArrayList<Person> personen = new ArrayList<>();
        personen.add(person);
        TestHoushold household = new TestHoushold(name, personen);

        DatabaseReference myRef = firebaseDatabase.getReference();
        myRef.child("haushalt").push().setValue(household);
    }*/

    public void storePersonOnDatabase(String name, String color) {
        Person person = new Person(name, color);

        DatabaseReference personRef = firebaseDatabase.getReference();
        personRef.child("person").push().setValue(person);
    }
}
