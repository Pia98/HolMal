package com.holmal.app.holmal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.holmal.app.holmal.model.ShoppingList;
import com.holmal.app.holmal.utils.FireBaseHandling;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateShoppingList extends AppCompatActivity {
//screen 13

    String shoppingListNameString;
    // wie bekomm ich die id richtig mit uebergeben?
    // Oder muss ich das immer als extra an eine Activity mit uebergeben?
    String householdId = "-LUvbEXcfXzNHg7x0jQm";

    FireBaseHandling fireBaseHandling = new FireBaseHandling();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shopping_list);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.createShoppingList)
    public void createShoppingListClicked(){
        if(validate()){
            // TODO change category
            ShoppingList shoppingList = new ShoppingList(shoppingListNameString, "null");
            fireBaseHandling.storeShoppingListInHousehold(householdId, shoppingList);
        }
    }

    /*
    Method that checks whether the input is valid/there before proceeding to the next screen.
     */
    private boolean validate() {
        EditText shoppingList = (EditText) findViewById(R.id.shoppingListNameInput);
        shoppingListNameString = shoppingList.getText().toString();
        if(!shoppingListNameString.isEmpty()){
            return true;
        }
        else return false;
    }



}
