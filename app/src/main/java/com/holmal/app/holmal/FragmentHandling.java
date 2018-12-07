package com.holmal.app.holmal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Models the handling if a fragment has to be placed
 * @author manuela
 */
public class FragmentHandling {
    /**
     * Replaces the toBeRemoved fragment with that one that has to be placed
     *
     * @param toBeRemoved The fragment that should be replaced
     * @param toBePlaced The fragment that should be drawn
     * @param fragmentManager The {@link FragmentManager}
     */
    public void putFragment(Fragment toBeRemoved, Fragment toBePlaced, FragmentManager fragmentManager, int containerId) {
        removeFragment(toBeRemoved, fragmentManager);
        toBeRemoved = toBePlaced;
        fragmentManager.beginTransaction()
                .add(containerId, toBeRemoved).commit();
    }

    /**
     * Replaces the toBeRemoved fragment with that one that has to be placed
     *
     * @param toBeRemoved The fragment that should be replaced
     * @param toBePlaced The fragment that should be drawn
     * @param fragmentManager The {@link FragmentManager}
     * @param extras Parameters that need to be passed to the Fragment
     */
    public void putFragmentwithExtras(Fragment toBeRemoved, Fragment toBePlaced, FragmentManager fragmentManager, int containerId, Bundle extras) {
        removeFragment(toBeRemoved, fragmentManager);
        toBeRemoved = toBePlaced;
        if(!extras.isEmpty()){
            toBePlaced.setArguments(extras);
        }
        fragmentManager.beginTransaction()
                .add(containerId, toBeRemoved).commit();
    }

    /**
     * Removes a fragment
     *
     * @param fragment Fragment that should be removed
     * @param fragmentManager The {@link FragmentManager}
     */
    private void removeFragment(Fragment fragment, FragmentManager fragmentManager){
        if(fragment != null){
            fragmentManager.beginTransaction().remove(fragment).commit();
        }
    }
}
