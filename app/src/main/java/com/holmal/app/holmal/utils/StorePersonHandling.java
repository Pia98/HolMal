package com.holmal.app.holmal.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.holmal.app.holmal.model.Person;

public class StorePersonHandling {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public void storePersonOnDatabase(String name, String color) {
        Person person = new Person(name, color);

        DatabaseReference personRef = firebaseDatabase.getReference("person");
        personRef.push().setValue(person);
    }
}
