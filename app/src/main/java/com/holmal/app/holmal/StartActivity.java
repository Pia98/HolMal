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

    /**
     * Is called when StartActivity is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
    }

    /**
     * OnClickListener on the create Button
     * Navigates to the CreateHousholdActivity
     */
    @OnClick(R.id.createHousehold)
    public void createHouseholdButtonClick() {
        Log.i(TAG, "CreateHousehold button clicked");
        Intent intent = new Intent(this, CreateHouseholdActivity.class);
        startActivity(intent);
    }

    /**
     * OnClickListener on the moveIn Button
     * Navigates to the RegistrationActivity
     */
    @OnClick(R.id.moveIn)
    public void moveInButtonClick() {
        Log.i(TAG, "MoveIn button clicked");
        Intent intent = new Intent(this, RegistrationActivity.class);
        // RegistrationFragment2 has to be drawn
        intent.putExtra("fragmentNumber", 2);
        startActivity(intent);
    }

    /**
     * OnClickListener on the logout button
     * logs the user out
     */
    @OnClick({R.id.logoutButton})
    public void logoutStart() {
        Log.i(TAG, "Logout button clicked");
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(StartActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
