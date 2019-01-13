package com.holmal.app.holmal.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.holmal.app.holmal.model.User;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * listens to the cild "user"
 * needed to check if a certain user is already a person in an household
 */
public class UserListener  implements ValueEventListener {

    /**
     * listens to the person ids in household
     */
    //hier werden die User gespeichert (emailAdresse, personID)
    ArrayList<User> userHash = new ArrayList<>();

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        userHash.clear();
        for(DataSnapshot child : dataSnapshot.getChildren()) {
            User value = child.getValue(User.class);
            userHash.add(value);
        }
        Log.i("PersonListener", "personList: " + userHash.toString());
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }


    public ArrayList<User> getUserHash() {
        return userHash;
    }
}
