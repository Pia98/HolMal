package com.holmal.app.holmal.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.holmal.app.holmal.model.Household;
import com.holmal.app.holmal.model.Item;
import com.holmal.app.holmal.model.Person;
import com.holmal.app.holmal.model.ShoppingList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//Class for handling references to the firebase database
//that are used in multiple other classes
public class FireBaseHandling {
    //private static final String TAG = FireBaseHandling.class.getName();


    // to get access to a FireBaseHandling instance
    public static FireBaseHandling getInstance() {
        Log.i("FireBaseHandling", "getInstance() " + firebaseHandling);
        return firebaseHandling;
    }
    private static FireBaseHandling firebaseHandling = new FireBaseHandling();

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

    public void storeShoppingListItem(String householdId, String shoppingListId, Item item){
        reference.child(householdRubric + "/" + householdId + "/shoppingLists/" + shoppingListId + "/itemsOfThisList").push().setValue(item);
        //reference.child(householdRubric).child(householdId).child("shoppingLists").child(shoppingListId).child("itemsOfThisList").push().setValue(item);
    }


    // registriere listener unter household/id/personenInHousehold
    public void startPersonValueEventListener(String householdId) {
        Log.i("FireBaseHandling", "personListener started (householdId: " + householdId + ")");
        reference.child(householdRubric + "/" + householdId + "/personInHousehold")
                .addValueEventListener(personListener);
    }

    private void startShoppingListListener(String householdId){
        Log.i("FireBaseHandling", "shoppingListListener started (householdId: " + householdId + ")");
        reference.child(householdRubric + "/" + householdId + "/shoppingLists")
                .addValueEventListener(shoppingListListener);
    }

    public PersonListener getPersonListener() {
        return personListener;
    }

    public ShoppingListListener getShoppingListListener() {
        return shoppingListListener;
    }

    public void registerAllListeners(String householdId){
        Log.i("FireBaseHandling", "registerAllListeners called (householdId: " + householdId + ")");

        // Daten explizit noch mal neu laden
        startPersonValueEventListener(householdId);
        startShoppingListListener(householdId);
    }
/*
    public void initializeShoppingList(String householdId){
        Log.i("FirebaseHandling", String.format("initializeShoppingList called (householdId: %s)", householdId));

        final List<ShoppingList> shoppingListList = new ArrayList<>();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shoppingListList.clear();
                Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
                while (iterator.hasNext()) {
                    DataSnapshot snapshot = iterator.next();
                    ShoppingList value = snapshot.getValue(ShoppingList.class);
                    Log.i("firebaseHandling", "ListName: " + value.getListName());
                    Log.i("FirebaseHandling", "value: " + value);
                    value.setStoreId(snapshot.getKey());
                    shoppingListList.add(value);
                }
                Log.i("FirebaseHandling", "onDataChanged shoppingListList: " + shoppingListList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        String child = householdRubric + "/" + householdId + "/shoppingLists";
        Query query = reference.orderByChild(child);
        //query.addListenerForSingleValueEvent(valueEventListener);
        query.addValueEventListener(valueEventListener);
    }
    */
}
