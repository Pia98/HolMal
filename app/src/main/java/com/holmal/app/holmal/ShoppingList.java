package com.holmal.app.holmal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShoppingList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.addItem)
    public void addItemClicked(){
        Intent intent = new Intent(this, CreateItem.class);
        startActivity(intent);
    }
}
