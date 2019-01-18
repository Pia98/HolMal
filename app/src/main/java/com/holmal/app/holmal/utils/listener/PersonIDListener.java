package com.holmal.app.holmal.utils.listener;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.holmal.app.holmal.model.Person;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PersonIDListener implements ValueEventListener {

    /**
     * listens to the person ids in household
     */
    //hier werden nur die IDs der Personen gespeichert
    List<String> personList = new ArrayList<>();

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        personList.clear();
        Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();
        Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
        while (iterator.hasNext()) {
            String value = (String) iterator.next().getValue();
            personList.add(value);
        }
        Log.i("PersonIDListener", "personList: " + personList);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    public List<String> getPersonList() {
        return personList;
    }
}
