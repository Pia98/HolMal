package com.holmal.app.holmal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.cancelButton)
    public void cancelButtonClicked(){
        //TODO
    }

    @OnClick(R.id.fromFavorites)
    public void fromFavoritesButtonClicked(){
        //TODO
    }

    @OnClick(R.id.add)
    public void addButtonClicked(){
        //TODO
    }
}
