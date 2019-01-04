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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shopping_list);
        ButterKnife.bind(this);

        PreferencesAccess preferences = new PreferencesAccess();
        householdId = preferences.readPreferences(this, getString(R.string.householdIDPreference));
    }

    /**
     * When 'close' button is clicked, do nothing and go back to the overview of all shopping lists
     */
    @OnClick(R.id.close)
    public void goBack() {
        Intent intent = new Intent(this, AllShoppingLists.class);
        startActivity(intent);
    }

    /**
     * When 'create' button is clicekd and all input is valid,
     * create a shoppingList and store this on database,
     * then go back to the overview of all shopping lists
     */
    @OnClick(R.id.createShoppingList)
    public void createShoppingListClicked() {
        if (validate()) {
            ShoppingList shoppingList = new ShoppingList(shoppingListNameString, shoppingListCategoryString);
            FireBaseHandling.getInstance().storeShoppingListInHousehold(householdId, shoppingList);
            Log.i("CreateShoppingList",
                    String.format("store shoppingList with name: '%s' and category: '%s'",
                            shoppingListNameString, shoppingListCategoryString));

            //go back to all shopping lists overview
            Intent intent = new Intent(this, AllShoppingLists.class);
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

        List<ShoppingList> shoppingLists =
                FireBaseHandling.getInstance().getShoppingListListener().getShoppingListList();

        if (!shoppingListNameString.isEmpty()) {
            return checkListNameTaken(shoppingLists);
        } else {
            Log.i("CreateShoppingList", "no shoppingListName");
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
    private boolean checkListNameTaken(List<ShoppingList> shoppingLists) {
        for (ShoppingList shoppingList : shoppingLists) {
            if (shoppingListNameString.equals(shoppingList.getListName())) {
                Log.i("CreateShoppingList", "name already taken");
                Toast.makeText(getApplicationContext(), R.string.ErrorListNameTaken, Toast.LENGTH_LONG).show();
                return false;
            } else {
                Log.i("CreateShoppingList",
                        String.format("shoppingList names: '%s', '%s' (entered list name)",
                                shoppingList.getListName(), shoppingListNameString));
            }
        }
        Log.i("CrateShoppingList", "all right");
        return true;
    }


}
