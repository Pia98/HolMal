package com.holmal.app.holmal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.holmal.app.holmal.ui.registrationfragment1.RegistrationFragment1;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, RegistrationFragment1.newInstance())
                    .commitNow();
        }
    }
}
