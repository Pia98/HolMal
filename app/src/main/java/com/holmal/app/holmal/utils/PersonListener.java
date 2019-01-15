package com.holmal.app.holmal.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.holmal.app.holmal.model.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class PersonListener implements ValueEventListener {

    /**
     * listens to the person ids in household
     */
    //hier werden nur die Personen mit ihren von der Firebase zugeordneten ids gespeichert
    HashMap<String, Person> personHash = new HashMap<>();

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        personHash.clear();
        for(DataSnapshot child : dataSnapshot.getChildren()) {
            String id = child.getKey();
            Person value = child.getValue(Person.class);
            personHash.put(id, value);
        }
        Log.i("PersonListener", "personList: " + personHash.toString());
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }


    public HashMap<String, Person> getPersonHash() {
        return personHash;
    }
}
