package com.holmal.app.holmal.utils;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.holmal.app.holmal.model.Household;
import com.holmal.app.holmal.model.Person;
import com.holmal.app.holmal.model.ShoppingList;

//Class for handling references to the firebase database
//that are used in multiple other classes
public class FireBaseHandling {

    public static FireBaseHandling getInstance() {
        return firebaseHandling;
    }

    static FireBaseHandling firebaseHandling = new FireBaseHandling();

    private PersonListener personListener = new PersonListener();
    private ShoppingListListener shoppingListListener = new ShoppingListListener();

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference reference = firebaseDatabase.getReference();

    private String householdRubric = "household";

    public String storeNewHousehold(Household household) {
        String storeId = reference.push().getKey();
        reference.child(householdRubric).child(storeId).setValue(household);

        //listener fuer personen und einkaufsliste gleich starten, wenn Haushalt erstellt wird
        startPersonValueEventListener(storeId);
        startShoppingListListener(storeId);

        return storeId;
    }

    public void storePersonOnDatabase(String name, int color) {
        Person person = new Person(name, color);
        reference.child("person").push().setValue(person);
    }
    
    public void storeMoveInPersonInHousehold(String householdId, Person person){
        reference.child(householdRubric + "/" + householdId + "/personInHousehold").push().setValue(person);
        // listener fuer einkaufsliste starten, wenn beitretende Person erfolgreich gespeichert wurde
        startShoppingListListener(householdId);
    }

    public void storeShoppingListInHousehold(String householdId, ShoppingList shoppingList){
        reference.child(householdRubric + "/" + householdId + "/shoppingLists").push().setValue(shoppingList);
    }


    // registriere listener unter household/id/personenInHousehold
    public void startPersonValueEventListener(String householdId) {
        reference.child(householdRubric + "/" + householdId + "/personInHousehold")
                .addValueEventListener(personListener);
        Log.i("FirebaseHandling", "personListener started");
    }

    private void startShoppingListListener(String householdId){
        // ich glaube hier kommt shoppingLists hin, aender ich aber notfalls, falls doch nicht,
        // wenn ich rausfinde, dass es nach dem speichern doch anders ist - ME
        reference.child(householdRubric + "/" + householdId + "/shoppingLists")
                .addValueEventListener(shoppingListListener);
        Log.i("FirebaseHandling", "shoppingListListener started");
    }

    public PersonListener getPersonListener() {
        return personListener;
    }

    public ShoppingListListener getShoppingListListener() {
        return shoppingListListener;
    }

    public void registerAllListeners(String householdId){
        startPersonValueEventListener(householdId);
        startShoppingListListener(householdId);
    }
}
