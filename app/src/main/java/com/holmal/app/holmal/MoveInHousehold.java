package com.holmal.app.holmal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoveInHousehold extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_in_household);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.moveInDone)
    public void moveInDoneClick() {
        Intent intent = new Intent(this, MainActivity.class); // decide if main (screen 11) or screen 5, then change here
        startActivity(intent);
    }
}
