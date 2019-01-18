package com.holmal.app.holmal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.holmal.app.holmal.utils.FireBaseHandling;
import com.holmal.app.holmal.utils.PreferencesAccess;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class StartActivity extends AppCompatActivity {
    private static final String TAG = StartActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);

        Log.i(TAG,"opened");

        // TODO auskommentieren, wenn man nicht gleich weiter geleitet werden will in die App, sondern create und moveIn Moeglichkeit haben will
        // wenn householdID schon in preferences (also ich schon in einem Haushalt bin) dann gehe gleich weiter in MainActivity
        PreferencesAccess preferences = new PreferencesAccess();
        String householdID = preferences.readPreferences(this, getString(R.string.householdIDPreference));
        if(householdID != null){
            Log.i(TAG, "already registered in an household");
            Intent intent = new Intent(this, ShoppingListActivity.class);
            startActivity(intent);
        }
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
    public void logoutStart(){
        Log.i(TAG, "Logout button clicked");
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(StartActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
