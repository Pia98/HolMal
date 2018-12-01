package com.holmal.app.holmal;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class FragmentHandling {
    public void putFragment(Fragment toBeRemoved, Fragment toBePlaced, FragmentManager fragmentManager) {
        removeFragment(toBeRemoved, fragmentManager);
        toBeRemoved = toBePlaced;
        fragmentManager.beginTransaction()
                .add(R.id.fragmentContainerRegistration, toBeRemoved).commit();
    }

    private void removeFragment(Fragment fragment, FragmentManager fragmentManager){
        if(fragment != null){
            fragmentManager.beginTransaction().remove(fragment).commit();
        }
    }
}
