package com.holmal.app.holmal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.createHousehold)
    public void createHouseholdButtonClick() {
        Intent intent = new Intent(this, CreateHousehold.class);
        startActivity(intent);
    }

    @OnClick(R.id.moveIn)
    public void moveInButtonClick() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        // RegistrationFragment2 has to be drawn
        intent.putExtra("fragmentNumber", 2);
        startActivity(intent);
    }

    @OnClick({R.id.logoutButton})
    public void logoutStart(){
        FirebaseAuth.getInstance().signOut();
    }
}
