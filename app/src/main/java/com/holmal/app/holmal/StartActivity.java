package com.holmal.app.holmal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class StartActivity extends AppCompatActivity {
    private static final String TAG = StartActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);

        Log.e(TAG, "opened onCreate method");
    }

    @OnClick(R.id.createHousehold)
    public void createHouseholdButtonClick() {
        Log.i(TAG, "CreateHousehold button clicked");
        Intent intent = new Intent(this, CreateHouseholdActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.moveIn)
    public void moveInButtonClick() {
        Log.i(TAG, "MoveIn button clicked");
        Intent intent = new Intent(this, RegistrationActivity.class);
        // RegistrationFragment2 has to be drawn
        intent.putExtra("fragmentNumber", 2);
        startActivity(intent);
    }

    @OnClick({R.id.logoutButton})
    public void logoutStart() {
        Log.i(TAG, "Logout button clicked");
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(StartActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
