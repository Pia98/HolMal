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

/**
 * Models storing and removing data from the database
 */
public class FireBaseHandling {
    private static final String TAG = FireBaseHandling.class.getName();


    // to get access to a FireBaseHandling instance
    public static FireBaseHandling getInstance() {
        Log.i(TAG, "getInstance() " + firebaseHandling);
        return firebaseHandling;
    }

    private static FireBaseHandling firebaseHandling = new FireBaseHandling();

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference reference = firebaseDatabase.getReference();

    private String householdRubric = "household";
    private String personRubric = "person";
    private String shoppingListRubric = "shoppingList";
    private String itemRubric = "item";


    /**
     * Stores a household on the database
     *
     * @param household The Household object that has to be stored
     * @return ID of the stored household
     */
    public String storeNewHousehold(Household household) {
        String storeId = reference.push().getKey();
        reference.child(householdRubric).child(storeId).setValue(household);

        return storeId;
    }

    //Method to delete data from firebase when the person leaves the household in settings

    /**
     * Removes an household from the database
     *
     * @param householdId ID of the household that has to be removed
     */
    public void deleteHousehold(String householdId) {
        reference.child(householdRubric).child(householdId).removeValue();
    }

    /**
     * Removes a person from the database
     *
     * @param personId ID of the person that has to be removed
     */
    public void deletePerson(String personId) {
        reference.child(personRubric).child(personId).removeValue();
    }

    /**
     * Removes a shoppingList from the database
     *
     * @param shoppingListId ID of the shoppinglist that has to be removed
     */
    public void deleteShoppingList(String shoppingListId) {
        reference.child(shoppingListRubric).child(shoppingListId).removeValue();
    }

    /**
     * Removes an item from the database
     *
     * @param itemId ID of the item that has to be removed
     */
    public void deleteItem(String itemId) {
        reference.child(itemRubric).child(itemId).removeValue();
    }

    /**
     * public void deleteAllItems(String shoppingListId){
     * reference.child(shoppingListRubric).child(shoppingListId).child("itemsOfThisList").removeValue();
     * }
     */

    /**
     * Stores a person on the database
     *
     * @param person The Person object that has to be stored
     * @return ID of the stored person
     */
    public String storePerson(Person person) {
        String storeId = reference.push().getKey();
        reference.child(personRubric).child(storeId).setValue(person);
        return storeId;
    }

    /**
     * Stores a shoppinglist on the database
     *
     * @param shoppingList The ShoppingList object that has to be stored
     */
    public void storeShoppingList(ShoppingList shoppingList) {
        String storeId = reference.push().getKey();
        reference.child(shoppingListRubric).child(storeId).setValue(shoppingList);
    }

    /**
     * Stores an item on the database
     *
     * @param shoppingListId The ID of the shoppinglist where the item belongs to
     * @param item           The Item object that has to be stored
     * @return ID of the stored item
     */
    public String storeItem(String shoppingListId, Item item) {
        String storeId = reference.push().getKey();
        reference.child(itemRubric).child(storeId).setValue(item);
        reference.child(shoppingListRubric + "/" + shoppingListId + "/itemsOfThisList").push().setValue(storeId);
        return storeId;
    }

    /**
     * edits the name of an household on the database
     * @param newName   The new Name
     * @param householdId The id of the household that shell be edited
     */
    public void editHousholdName(String newName, String householdId){
        reference.child(householdRubric + "/" + householdId + "/householdName").setValue(newName);
    }
}
