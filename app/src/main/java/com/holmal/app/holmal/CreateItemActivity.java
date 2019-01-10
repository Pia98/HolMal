package com.holmal.app.holmal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.holmal.app.holmal.model.Item;
import com.holmal.app.holmal.utils.FireBaseHandling;
import com.holmal.app.holmal.utils.PreferencesAccess;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateItemActivity extends AppCompatActivity {

    String itemName;
    String quantity;
    boolean important;
    boolean favorite;
    String additionalInfo;

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
    }

    //pressing this button takes you back to the general view of the shopping list
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
            //TODO implement functionality aka actually add item to list

            Item item = new Item(itemName, quantity, important, favorite, additionalInfo);
            FireBaseHandling.getInstance().storeShoppingListItem(householdId, shoppingListId, item);

            //maybe also check whether item is a favourite here and add to favourites

            //and lead back to general view of the shopping list
            Intent intent = new Intent(this, ShoppingListActivity.class);
            startActivity(intent);
        }
    }

    //Validation: add button can only be pressed if a name has been given
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
            return true;
        } else {
            Toast.makeText(getApplicationContext(), R.string.ErrorEnterItem, Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
