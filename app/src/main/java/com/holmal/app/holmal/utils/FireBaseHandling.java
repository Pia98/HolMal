package com.holmal.app.holmal.utils;

import android.app.Application;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.holmal.app.holmal.model.Household;
import com.holmal.app.holmal.model.Person;
import com.holmal.app.holmal.model.TestHoushold;

import java.util.ArrayList;

//Class for handling references to the firebase database
//that are used in multiple other classes
public class FireBaseHandling extends Application {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @Override
    public void onCreate() {
        super.onCreate();

       // firebaseDatabase = FirebaseDatabase.getInstance();

    }

    public void storeNewHousehold(Household household){
        String name = household.getHouseholdName();
        DatabaseReference householdRef = firebaseDatabase.getReference("haushalt");
        householdRef.setValue(household);
    }

    public void storeNewTestHousehold(String name, Person person){
        ArrayList<Person> personen = new ArrayList<>();
        personen.add(person);
        TestHoushold household = new TestHoushold(name, personen);

        DatabaseReference myRef = firebaseDatabase.getReference();
        myRef.child("haushalt").push().setValue(household);
    }

    public void storePersonOnDatabase(String name, String color) {
        Person person = new Person(name, color);

        DatabaseReference personRef = firebaseDatabase.getReference();
        personRef.child("person").push().setValue(person);
    }
}
