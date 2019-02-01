package com.holmal.app.holmal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.holmal.app.holmal.model.Item;
import com.holmal.app.holmal.utils.FireBaseHandling;
import com.holmal.app.holmal.utils.PreferencesAccess;
import java.util.ArrayList;
import java.util.HashMap;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Class that handles adding an item to a shopping list. Accessed via the addItem button in ShoppingListActivity.
 * Uses the activity_create_item layout.
 */
public class CreateItemActivity extends AppCompatActivity {

    private static final String TAG = CreateItemActivity.class.getName();
    private HashMap<String, Item> itemsOfTheList = new HashMap<>();
    private ArrayList<String> itemIds = new ArrayList<>();

    private String itemName;
    private String quantity;
    private boolean important;
    private boolean favorite;
    private String additionalInfo;
    private String itsTask = "";
    private boolean done = false;
    private int timeDone = 0;

    private String householdId;
    private String shoppingListId;

    /**
     * Method that initialises the class and its important features and starts listeners.
     *
     * @param savedInstanceState Bundle object that contains a saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();
        shoppingListId = extras.getString("shoppingListId");
        PreferencesAccess preferences = new PreferencesAccess();
        householdId = preferences.readPreferences(this, getString(R.string.householdIDPreference));

        // Listener to get all IDs of the items that belongs to the list with id = shoppingListId
        startShoppingListItemListener();

        // Listener to get all items that are in the list
        startItemListener();
    }

    /**
     * Method that starts the item listener and gets a list of the items of this household from the
     * firebase database.
     * Is needed in the create item activity to validate if no other item with that name already
     * exists on the list.
     */
    private void startItemListener() {
        FirebaseDatabase.getInstance().getReference().child("item").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, "item listener in onCreate...");
                itemsOfTheList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String id = child.getKey();
                    Item value = child.getValue(Item.class);
                    Log.i(TAG, "item: " + value);
                    for (int i = 0; i < itemIds.size(); i++) {
                        if (id.equals(itemIds.get(i))) {
                            itemsOfTheList.put(id, value);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void startShoppingListItemListener() {
        FirebaseDatabase.getInstance().getReference().child("shoppingList").child(shoppingListId).child("itemsOfThisList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemIds.clear();
                Log.i(TAG, "list listener in onCreate...");
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String id = child.getKey();
                    String value = (String) child.getValue();
                    Log.i(TAG, "id: " + value);
                    itemIds.add(value);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    /**
     * Method that handles the press of the cancel button.
     * Pressing this button takes you back to the general view of the shopping list. Nothing happens.
     */
    @OnClick(R.id.cancelButton)
    public void cancelButtonClicked() {
        Intent intent = new Intent(this, ShoppingListActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Method that handles the press of the favourites button.
     * Leads you to favourites activity where you can choose an item from your favourites to add to the list.
     * //TODO remove this or not? S: no because only invisible not deleted
     */
    @OnClick(R.id.fromFavorites)
    public void fromFavoritesButtonClicked() {
        Intent intent = new Intent(this, FavoritesActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Method that handles the press of the add button.
     * If the validation is successful this method creates a new item, stores it in the database
     * and takes you back to the ShoppingListActivity.
     */
    @OnClick(R.id.add)
    public void itemCreationOnClicked() {
        if (validate()) {
            Item item = new Item(itemName, quantity, important, favorite, itsTask, additionalInfo, done, timeDone, shoppingListId);
            FireBaseHandling.getInstance().storeItem(shoppingListId, item);

            //maybe also check whether item is a favourite here and add to favourites

            //and lead back to general view of the shopping list
            Intent intent = new Intent(this, ShoppingListActivity.class);
            startActivity(intent);
            finish();
        }
    }


    /**
     * Validation for the add button.
     * A name needs to be given to an item.
     * Also reads the input for the new item from the edit texts and checkbox.
     * @return if input is valid
     */
    private boolean validate() {
        EditText itemInput = findViewById(R.id.whatInput);
        itemName = itemInput.getText().toString();

        EditText amountInput = findViewById(R.id.amountInput);
        quantity = amountInput.getText().toString();
        EditText infoInput = findViewById(R.id.infoInput);
        additionalInfo = infoInput.getText().toString();
        CheckBox urgentInput = findViewById(R.id.urgentChecked);
        if (urgentInput.isChecked()) {
            important = true;
        }
        CheckBox saveAsFavoriteInput = findViewById(R.id.saveAsFavoriteChecked);
        if (saveAsFavoriteInput.isChecked()) {
            favorite = true;
        }

        // Bezeichnung ist eingegeben
        if (!itemName.isEmpty()) {
            return checkItemNameTaken(itemName);
        } else {
            Toast.makeText(getApplicationContext(), R.string.ErrorEnterItem, Toast.LENGTH_LONG).show();
            return false;
        }
    }

    /**
     * Method that checks if an item name has been given. This is necessary for the validation.
     * @param itemName input for the name
     * @return true or false
     */
    private boolean checkItemNameTaken(String itemName) {
        for (int i = 0; i < itemsOfTheList.size(); i++) {
            String[] keys = itemsOfTheList.keySet().toArray(new String[itemsOfTheList.size()]);
            String name = itemsOfTheList.get(keys[i]).getItemName();
            if (itemName.equals(name)) {
                Log.i(TAG, "name already taken");
                Toast.makeText(getApplicationContext(), R.string.ErrorItemTaken, Toast.LENGTH_LONG).show();
                return false;
            } else {
                Log.i(TAG, String.format("item names: '%s', '%s' (entered item name)",
                        name, itemName));
            }
        }
        Log.i(TAG, "all right");
        return true;
    }
}
