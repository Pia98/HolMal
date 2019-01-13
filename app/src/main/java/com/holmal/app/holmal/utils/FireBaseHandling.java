package com.holmal.app.holmal.utils;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.holmal.app.holmal.model.Household;
import com.holmal.app.holmal.model.Item;
import com.holmal.app.holmal.model.Person;
import com.holmal.app.holmal.model.ShoppingList;
import com.holmal.app.holmal.model.User;

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


    public void storeUserOnDatabase(User user) {
        reference.child("user").push().setValue(user);
    }

    public void deleteHousehold(String householdId){
        reference.child(householdRubric).child(householdId).removeValue();
    }
    public void removePersonFromHousehold(String householdId, String person){
        reference.child(householdRubric).child(householdId).child("personInHousehold").child(person).removeValue();
    }

    public String storePersonOnDatabase(Person person) {
        String personId = reference.push().getKey();
        reference.child("person").push().setValue(person);
        return personId;
    }
    
    public String storeMoveInPersonInHousehold(String householdId, Person person){
        String personID = storePersonOnDatabase(person);
        reference.child(householdRubric + "/" + householdId + "/personInHousehold").push().setValue(personID);
        // listener fuer einkaufsliste starten, wenn beitretende Person erfolgreich gespeichert wurde
        startShoppingListListener(householdId);
        return personID;
    }

    public void storeShoppingListInHousehold(String householdId, ShoppingList shoppingList){
        reference.child(householdRubric + "/" + householdId + "/shoppingLists").push().setValue(shoppingList);
    }

    public void storeShoppingListItem(String householdId, String shoppingListId, Item item){
        reference.child(householdRubric + "/" + householdId + "/shoppingLists" + shoppingListId + "/itemsOfThisList").push().setValue(item);
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
}
