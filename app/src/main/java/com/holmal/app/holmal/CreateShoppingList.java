package com.holmal.app.holmal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;

import com.holmal.app.holmal.model.ShoppingList;
import com.holmal.app.holmal.utils.FireBaseHandling;
import com.holmal.app.holmal.utils.PreferencesAccess;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateShoppingList extends AppCompatActivity {
//screen 13

    String shoppingListNameString;
    String shoppingListCategoryString;
    String householdId;

    FireBaseHandling fireBaseHandling = new FireBaseHandling();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shopping_list);
        ButterKnife.bind(this);

        PreferencesAccess preferences = new PreferencesAccess();
        householdId = preferences.readPreferences(this, getString(R.string.householdIDPreference));
    }

    //if clicked one is lead back to the overview of all shopping lists
    @OnClick(R.id.close)
    public void goBack(){
        Intent intent = new Intent(this, AllShoppingLists.class);
        startActivity(intent);
    }
    
    @OnClick(R.id.createShoppingList)
    public void createShoppingListClicked() {
        if (validate()) {
            // TODO change category
            ShoppingList shoppingList = new ShoppingList(shoppingListNameString, shoppingListCategoryString);
            fireBaseHandling.storeShoppingListInHousehold(householdId, shoppingList);

            //go back to all shopping lists overview
            Intent intent = new Intent(this, AllShoppingLists.class);
            startActivity(intent);
        }
    }

    /*
    Method that checks whether the input is valid/there before proceeding to the next screen.
     */
    private boolean validate() {
        EditText shoppingList = (EditText) findViewById(R.id.shoppingListNameInput);
        shoppingListNameString = shoppingList.getText().toString();

        Spinner category = (Spinner) findViewById(R.id.shoppingListCategoryDropDown);
        //shoppingListCategoryString = category.getSelectedItem().toString();
        Log.i("CreateShoppingList", "category: " + shoppingListCategoryString);

        if (!shoppingListNameString.isEmpty()) {
            return true;
        } else return false;
    }


}
