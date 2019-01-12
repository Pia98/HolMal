package com.holmal.app.holmal;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.holmal.app.holmal.model.Person;
import com.holmal.app.holmal.utils.FireBaseHandling;
import com.holmal.app.holmal.utils.PersonListener;
import com.holmal.app.holmal.utils.PreferencesAccess;
import com.holmal.app.holmal.utils.SettingsAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        //menu that appears from the left
        Toolbar toolbar = findViewById(R.id.menu);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        setTitle(R.string.settings);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        //if my assignments is pressed in the menu you will be lead there
                        if(menuItem.getItemId() == R.id.nav_my_tasks){
                            Intent intentT = new Intent(SettingsActivity.this, MyTasksActivity.class);
                            startActivity(intentT);
                            return true;
                        }
                        //if all shopping lists is pressed in the menu you will be lead there
                        else if (menuItem.getItemId()==R.id.nav_shopping_lists){
                            Intent intentLists = new Intent(SettingsActivity.this, AllShoppingListsActivity.class);
                            startActivity(intentLists);
                            return true;
                        }
                        //Logout
                        else if (menuItem.getItemId()==R.id.logout){
                            Log.i("TAG", "Logout button clicked");
                            FirebaseAuth.getInstance().signOut();
                            Intent intentout = new Intent(SettingsActivity.this, LoginActivity.class);
                            startActivity(intentout);
                            return true;
                        }

                        return true;
                    }
                });

        //listener so that the menu opens and closes
        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );


        //show members in household
        //gets the person listener from firebase
        List<Person> personInHousehold = FireBaseHandling.getInstance().getPersonListener().getPersonList();

        SettingsAdapter adapter = new SettingsAdapter(this, personInHousehold);
        ListView list = findViewById(R.id.listOfHouseholdMembers);
        list.setAdapter(adapter);
    }


    //Menu is opened
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }


    @OnClick(R.id.leaveHousehold)
    public void leaveHouseholdClicked(){

        //pop up that asks the user if they are sure
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Möchtest du wirklich den Haushalt verlassen?");
        builder.setCancelable(true);
        builder.setPositiveButton(
                "Ja",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //remove member from household
                        PreferencesAccess preferencesAccess = new PreferencesAccess();
                        String householdID = preferencesAccess.readPreferences(SettingsActivity.this, getString(R.string.householdIDPreference));
                        preferencesAccess.storePreferences(SettingsActivity.this, getString(R.string.householdIDPreference), null);

                        FireBaseHandling.getInstance().removePersonFromHousehold(householdID,"0");
                        //delete household if household is empty now
                        List<Person> personInHousehold = FireBaseHandling.getInstance().getPersonListener().getPersonList();

                        //hasn't synchronized by then so use former size - 1
                        if(personInHousehold.size() -1 == 0){
                            Log.e("deleteHousehold", householdID + "deleteHousehold");
                            FireBaseHandling.getInstance().deleteHousehold(householdID);
                        }
                        FirebaseAuth.getInstance().signOut();
                        Intent intentout = new Intent(SettingsActivity.this, LoginActivity.class);
                        startActivity(intentout);
                    }
                });
        builder.setNegativeButton(
                "Nein",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();



    }
}
