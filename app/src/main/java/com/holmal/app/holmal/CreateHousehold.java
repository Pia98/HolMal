package com.holmal.app.holmal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateHousehold extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_household);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.createHouseholdDone)
    public void createHouseholdDoneClick() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        // RegistrationFragment1 has to be drawn
        intent.putExtra("fragmentNumber", 1);
        startActivity(intent);
    }
}
