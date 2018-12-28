package com.holmal.app.holmal.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.holmal.app.holmal.model.Person;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PersonListener implements ValueEventListener {
    List<Person> personList = new ArrayList<>();

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        personList.clear();
        Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();
        Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
        while(iterator.hasNext()){
            Person value = iterator.next().getValue(Person.class);
            personList.add(value);
        }
        Log.i("PersonListener", "personList: " + personList);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
