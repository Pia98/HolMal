package com.holmal.app.holmal;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.holmal.app.holmal.ui.registrationfragment1.RegistrationFragment1;
import com.holmal.app.holmal.ui.registrationfragment1.RegistrationFragment2;

import butterknife.ButterKnife;

public class RegistrationActivity extends AppCompatActivity {

    Fragment currentFragment;
    FragmentHandling fragmentHandling = new FragmentHandling();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);
        ButterKnife.bind(this);

        // maybe not the best solution
        int fragmentNumber = getIntent().getExtras().getInt("fragmentNumber");
        switch (fragmentNumber){
            case 1:
                putRegistrationFragment1();
                break;
            case 2:
                putRegistrationFragment2();
                break;
        }

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.container, RegistrationFragment1.newInstance())
//                    .commitNow();
//        }
    }

    public void putRegistrationFragment1(){
        fragmentHandling.putFragment(currentFragment,
                RegistrationFragment1.newInstance(),
                getSupportFragmentManager());
    }

    public void putRegistrationFragment2(){
        fragmentHandling.putFragment(currentFragment,
                RegistrationFragment2.newInstance(),
                getSupportFragmentManager());
    }
}
