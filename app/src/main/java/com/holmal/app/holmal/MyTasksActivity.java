package com.holmal.app.holmal;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.holmal.app.holmal.model.Item;
import com.holmal.app.holmal.model.Person;
import com.holmal.app.holmal.utils.ItemsAdapter;
import com.holmal.app.holmal.utils.PreferencesAccess;

import java.util.HashMap;

import butterknife.ButterKnife;

//class that shows the users assignments
public class MyTasksActivity extends AppCompatActivity {
    private static final String TAG = MyTasksActivity.class.getName();

    private DrawerLayout mDrawerLayout;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView list;

    HashMap<String, Item> myItems = new HashMap<>();
    private HashMap<String, Person> person = new HashMap<>();
    String householdId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tasks);
        ButterKnife.bind(this);

        list = findViewById(R.id.list);
        layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);


        PreferencesAccess preferencesAccess = new PreferencesAccess();
        String personID = preferencesAccess.readPreferences(this, getString(R.string.personIDPreference));
        householdId = preferencesAccess.readPreferences(this, getString(R.string.householdIDPreference));

        //menu that appears from the left
        menu();

        startPersonListener();

        FirebaseDatabase.getInstance().getReference().child("item").orderByChild("itsTask").equalTo(personID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myItems.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String id = child.getKey();
                    Item value = child.getValue(Item.class);
                    Log.i(TAG, "item: " + value);
                    myItems.put(id, value);
                }
                Log.i(TAG, "myItems List " + myItems);
                //adapter
                ItemsAdapter adapter = new ItemsAdapter(MyTasksActivity.this, myItems, person);
                RecyclerView list = findViewById(R.id.list);
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void menu() {
        Toolbar toolbar = findViewById(R.id.menu);
        setSupportActionBar(toolbar);
        setTitle(R.string.myJobs);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

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
                        //if all shopping lists is pressed in the menu you will be lead there
                        if (menuItem.getItemId() == R.id.nav_shopping_lists) {
                            Intent intentLists = new Intent(MyTasksActivity.this, AllShoppingListsActivity.class);
                            startActivity(intentLists);
                            return true;
                        } else if (menuItem.getItemId() == R.id.nav_settings) {
                            Intent intentLists = new Intent(MyTasksActivity.this, SettingsActivity.class);
                            startActivity(intentLists);
                            return true;
                        }
                        //Logout
                        else if (menuItem.getItemId() == R.id.logout) {
                            Log.i("TAG", "Logout button clicked");
                            //delete preferences
                            PreferencesAccess preferencesAccess = new PreferencesAccess();
                            preferencesAccess.storePreferences(MyTasksActivity.this, getString(R.string.householdIDPreference), null);
                            preferencesAccess.storePreferences(MyTasksActivity.this, getString(R.string.personIDPreference), null);
                            preferencesAccess.storePreferences(MyTasksActivity.this, getString(R.string.recentShoppingListNamePreference), null);
                            FirebaseAuth.getInstance().signOut();
                            Intent intentout = new Intent(MyTasksActivity.this, LoginActivity.class);
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
    }

    /**
     * Method that starts the person listener. Gets a list of the people in the active household from the firebase database.
     */
    private void startPersonListener() {
        FirebaseDatabase.getInstance().getReference().child("person").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                person.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String id = child.getKey();
                    Person value = child.getValue(Person.class);
                    if (value.getIdBelongingTo().equals(householdId)) {
                        person.put(id, value);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
}
