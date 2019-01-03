package com.holmal.app.holmal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.holmal.app.holmal.model.ShoppingList;
import com.holmal.app.holmal.utils.FireBaseHandling;
import com.holmal.app.holmal.utils.PreferencesAccess;

import java.util.List;

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
            ShoppingList shoppingList = new ShoppingList(shoppingListNameString, shoppingListCategoryString);
            fireBaseHandling.storeShoppingListInHousehold(householdId, shoppingList);
            Log.i("CreateShoppingList", "shoppingList name: " + shoppingListNameString);

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
        shoppingListCategoryString = category.getSelectedItem().toString();

        List<ShoppingList> shoppingLists = fireBaseHandling.getShoppingListListener().getShoppingListList();

        if (!shoppingListNameString.isEmpty()) {
            return checkListNameTaken(shoppingLists);
        } else {
            Log.i("CreateShoppingList", "no shoppingListName");
            Toast.makeText(this, R.string.ErrorEnterShoppingListName, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean checkListNameTaken(List<ShoppingList> shoppingLists){
        for(ShoppingList shoppingList : shoppingLists){
            if(shoppingListNameString.equals(shoppingList.getListName())){
                Log.i("CreateShoppingList", "name already taken");
                Toast.makeText(this, R.string.ErrorListNameTaken, Toast.LENGTH_SHORT).show();
                return false;
            }
            else {
                Log.i("CreateShoppingList", String.format("shoppingList names: '%s', '%s' (entered list name)", shoppingList.getListName(), shoppingListNameString));
            }
        }
        Log.i("CrateShoppingList", "all right");
        return true;
    }


}
