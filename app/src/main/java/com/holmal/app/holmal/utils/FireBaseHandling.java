package com.holmal.app.holmal.utils;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.holmal.app.holmal.model.Household;
import com.holmal.app.holmal.model.Item;
import com.holmal.app.holmal.model.Person;
import com.holmal.app.holmal.model.ShoppingList;

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

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference reference = firebaseDatabase.getReference();

    private String householdRubric = "household";
    private String personRubric = "person";
    private String shoppingListRubric = "shoppingList";
    private String itemRubric = "item";


    public String storeNewHousehold(Household household) {
        String storeId = reference.push().getKey();
        reference.child(householdRubric).child(storeId).setValue(household);

        return storeId;
    }

    //Method to delete data from firebase when the person leaves the household in settings
    public void deleteHousehold(String householdId){
        reference.child(householdRubric).child(householdId).removeValue();
    }
    public void removePersonFromHousehold(String personId){
        reference.child(personRubric).child(personId).removeValue();
    }
    public void deleteAllShoppingLists(String shoppingListId){
        reference.child(shoppingListRubric).child(shoppingListId).removeValue();
    }
   public void deleteAllItems(String itemId){
        reference.child(itemRubric).child(itemId);
    }
  /** public void deleteAllItems(String shoppingListId){
       reference.child(shoppingListRubric).child(shoppingListId).child("itemsOfThisList").removeValue();
   }*/




    public String storePerson(String householdId, Person person) {
        String storeId = reference.push().getKey();
        reference.child(personRubric).child(storeId).setValue(person);
        return storeId;
    }

    public void storeShoppingList(String householdId, ShoppingList shoppingList){
        String storeId = reference.push().getKey();
        reference.child(shoppingListRubric).child(storeId).setValue(shoppingList);
    }


    public String storeItem(String shoppingListId, Item item){
        String storeId = reference.push().getKey();
        reference.child(itemRubric).child(storeId).setValue(item);
        reference.child(shoppingListRubric + "/" + shoppingListId + "/itemsOfThisList").push().setValue(storeId);
        return storeId;
    }
}
