package com.holmal.app.holmal;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AllShoppingLists extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_shopping_lists);
        ButterKnife.bind(this);
    }

    //Button that leads to screen 13
    @OnClick(R.id.addShoppingList)
    public void addShoppingListClicked(){
        Intent intent = new Intent(this, CreateShoppingList.class);
        startActivity(intent);
    }



}
