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
        chooseFragment(fragmentNumber);

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.container, RegistrationFragment1.newInstance())
//                    .commitNow();
//        }
    }

    /**
     * Places a fragment depending on the app context
     *
     * @param fragmentNumber The number that should be placed
     */
    private void chooseFragment(int fragmentNumber) {
        switch (fragmentNumber) {
            // RegistrationFragment1 because it's from context 'create'
            case 1:
                fragmentHandling.putFragment(currentFragment,
                        RegistrationFragment1.newInstance(),
                        getSupportFragmentManager(),
                        R.id.fragmentContainerRegistration);
                break;
            // RegistrationFragment2 because it's from context 'move in'
            case 2:
                fragmentHandling.putFragment(currentFragment,
                        RegistrationFragment2.newInstance(),
                        getSupportFragmentManager(),
                        R.id.fragmentContainerRegistration);
                ;
                break;
        }
    }
}
