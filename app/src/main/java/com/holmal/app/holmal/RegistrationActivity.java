package com.holmal.app.holmal;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.holmal.app.holmal.ui.registrationfragment1.RegistrationFragment1;
import com.holmal.app.holmal.ui.registrationfragment1.RegistrationFragment2;

import butterknife.ButterKnife;

public class RegistrationActivity extends AppCompatActivity {

    Fragment currentFragment;

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
        putFragment(currentFragment, RegistrationFragment1.newInstance());
    }

    public void putRegistrationFragment2(){
        putFragment(currentFragment, RegistrationFragment2.newInstance());
    }



    private void putFragment(Fragment toBeRemoved, Fragment toBePlaced) {
        removeFragment(toBeRemoved);
        toBeRemoved = toBePlaced;
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainerRegistration, toBeRemoved).commit();
    }

    private void removeFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }
}
