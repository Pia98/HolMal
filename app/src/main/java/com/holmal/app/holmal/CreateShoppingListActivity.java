package com.holmal.app.holmal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.holmal.app.holmal.model.ShoppingList;
import com.holmal.app.holmal.utils.FireBaseHandling;
import com.holmal.app.holmal.utils.PreferencesAccess;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateShoppingListActivity extends AppCompatActivity {
//screen 13

    private static final String TAG = CreateShoppingListActivity.class.getName();

    private HashMap<String, ShoppingList> listsOfThisHousehold = new HashMap<>();

    String shoppingListNameString;
    String shoppingListCategoryString;
    String householdId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shopping_list);
        ButterKnife.bind(this);

        PreferencesAccess preferences = new PreferencesAccess();
        householdId = preferences.readPreferences(this, getString(R.string.householdIDPreference));

        startShoppingListListener();
    }

    private void startShoppingListListener() {
        FirebaseDatabase.getInstance().getReference().child("shoppingList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, "listener in onCreate...");
                listsOfThisHousehold.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.i(TAG, "alle Listen durchgehen");
                    String id = child.getKey();
                    ShoppingList value = child.getValue(ShoppingList.class);
                    Log.i(TAG, "ShoppingList: " + value);
                    if (value.getIdBelongingTo().equals(householdId)) {
                        Log.i(TAG, "Liste gehört zu diesem Haushalt.");
                        listsOfThisHousehold.put(id, value);
                    }

                    Log.i(TAG, "listsOfThisHousehold in for Schleife bei listener: " + listsOfThisHousehold);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // When 'close' button is clicked, do nothing and go back to the overview of all shopping lists
    @OnClick(R.id.close)
    public void goBack() {
        Intent intent = new Intent(this, AllShoppingListsActivity.class);
        startActivity(intent);
    }

    /* When 'create' button is clicekd and all input is valid,
    create a shoppingList and store this on database,
    then go back to the overview of all shopping lists */
    @OnClick(R.id.createShoppingList)
    public void createShoppingListClicked() {
        if (validate()) {
            ShoppingList shoppingList = new ShoppingList(shoppingListNameString, shoppingListCategoryString, householdId);
            FireBaseHandling.getInstance().storeShoppingList(shoppingList);
            Log.i(TAG, String.format("store shoppingList with name: '%s' and category: '%s'",
                    shoppingListNameString, shoppingListCategoryString));

            //go back to all shopping lists overview
            Intent intent = new Intent(this, AllShoppingListsActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Check if all input fields are valid
     *
     * @return if all inputs are valid
     */
    private boolean validate() {
        EditText shoppingList = (EditText) findViewById(R.id.shoppingListNameInput);
        shoppingListNameString = shoppingList.getText().toString();
        Spinner category = (Spinner) findViewById(R.id.shoppingListCategoryDropDown);
        shoppingListCategoryString = category.getSelectedItem().toString();

        if (!shoppingListNameString.isEmpty()) {
            return checkListNameTaken(listsOfThisHousehold);
        } else {
            Log.i(TAG, "no shoppingListName");
            Toast.makeText(getApplicationContext(), R.string.ErrorEnterShoppingListName, Toast.LENGTH_LONG).show();
            return false;
        }
    }

    /**
     * Check if there exists a shopping list with the same name in the household
     *
     * @param shoppingLists List of all shoppingLists
     * @return if the shoppingList name is already taken
     */
    private boolean checkListNameTaken(HashMap<String, ShoppingList> shoppingLists) {
        Log.i("CreateShoppingList", "Liste: " + shoppingLists);

        for (int i = 0; i < shoppingLists.size(); i++) {
            String[] keys = shoppingLists.keySet().toArray(new String[shoppingLists.size()]);
            String name = shoppingLists.get(keys[i]).getListName();
            if (shoppingListNameString.equals(name)) {
                Log.i(TAG, "name already taken");
                Toast.makeText(getApplicationContext(), R.string.ErrorListNameTaken, Toast.LENGTH_LONG).show();
                return false;
            } else {
                Log.i(TAG, String.format("shoppingList names: '%s', '%s' (entered list name)",
                        name, shoppingListNameString));
            }
        }
        Log.i("CrateShoppingList", "all right");
        return true;
    }
}
