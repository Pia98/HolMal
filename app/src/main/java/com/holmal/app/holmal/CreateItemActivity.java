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

public class CreateItemActivity extends AppCompatActivity {
    private static final String TAG = CreateItemActivity.class.getName();

    private HashMap<String, Item> itemsOfTheList = new HashMap<>();
    private ArrayList<String> itemIds = new ArrayList<>();

    String itemName;
    String quantity;
    boolean important;
    boolean favorite;
    String additionalInfo;
    String itsTask = "";
    boolean done = false;
    int timeDone = 0;

    String householdId;
    String shoppingListId;

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

    //pressing this button takes you back to the general view of the shopping list -> nothing happens
    @OnClick(R.id.cancelButton)
    public void cancelButtonClicked() {
        Intent intent = new Intent(this, ShoppingListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.fromFavorites)
    public void fromFavoritesButtonClicked() {
        Intent intent = new Intent(this, FavoritesActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.add)
    public void itemCreationOnClicked() {
        if (validate()) {
            Item item = new Item(itemName, quantity, important, favorite, itsTask, additionalInfo, done, timeDone);
            FireBaseHandling.getInstance().storeItem(shoppingListId, item);

            //maybe also check whether item is a favourite here and add to favourites

            //and lead back to general view of the shopping list
            Intent intent = new Intent(this, ShoppingListActivity.class);
            startActivity(intent);
        }
    }

    // Validation: add button can only be pressed if a name has been given
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
