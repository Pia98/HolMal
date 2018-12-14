package com.holmal.app.holmal.utils;

import android.app.Application;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.holmal.app.holmal.model.Household;

//Class for handling references to the firebase database
//that are used in multiple other classes
public class FireBaseHandling extends Application {

    /*FirebaseDatabase database;


    @Override
    public void onCreate() {
        super.onCreate();

        database = FirebaseDatabase.getInstance();

    }


    public void createNewHousehold(Household household){
        String name = household.getHouseholdName();
        DatabaseReference householdRef = database.getReference("haushalt");
        householdRef.setValue(household);
    }*/

}
