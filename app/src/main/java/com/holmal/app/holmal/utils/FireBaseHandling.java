package com.holmal.app.holmal.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//Class for handling references to the firebase database
//that are used in multiple other classes
public class FireBaseHandling {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseRef = database.getReference();

}
