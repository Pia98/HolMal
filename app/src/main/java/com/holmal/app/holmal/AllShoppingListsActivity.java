package com.holmal.app.holmal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.holmal.app.holmal.model.Household;
import com.holmal.app.holmal.model.Item;
import com.holmal.app.holmal.model.Person;
import com.holmal.app.holmal.model.ShoppingList;
import com.holmal.app.holmal.utils.PreferencesAccess;
import com.holmal.app.holmal.utils.ShoppingListsAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Overview over all the shopping lists of a household.
 * Includes the ability to add new shoppinglists
 */
public class AllShoppingListsActivity extends AppCompatActivity {

    private static final String TAG = AllShoppingListsActivity.class.getName();
    private DrawerLayout mDrawerLayout;
    private HashMap<String, ShoppingList> listsOfThisHousehold = new HashMap<>();
    private HashMap<String, Item> itemsOfThisHousehold = new HashMap<>();
    private ArrayList<String> itemIds = new ArrayList<>();
    private RecyclerView.LayoutManager layoutmanager;
    private RecyclerView listsView;
    private String householdId;
    PreferencesAccess preferencesAccess = new PreferencesAccess();


    /**
     * Method that initialises the class and its important features.
     * It establishes the connection to its xml file (activity_all_shopping_lists) and starts the listeners.
     *
     * @param savedInstanceState Bundle object that contains a saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_shopping_lists);
        ButterKnife.bind(this);
        listsView = findViewById(R.id.allShoppingLists);
        layoutmanager = new GridLayoutManager(this, 2);
        listsView.setLayoutManager(layoutmanager);

        householdId = preferencesAccess.readPreferences(this, getString(R.string.householdIDPreference));

        //menu that appears from the left
        menu();

        startShoppingListListener(householdId);
    }

    /**
     * Method that adds a navigation drawer to the class. This is used as a menu to enable navigation within the app.
     * Sets menu as a toolbar and tabs as tabs to navigate between open and done items.
     * Uses the drawer_layout as a layout for the drawer and specifies the behaviour if an item in the navigation menu is clicked.
     * This method also sets a listener to the navigation drawer so that it opens and closes.
     */
    private void menu() {
        Toolbar toolbar = findViewById(R.id.menu);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        setTitle(R.string.shoppingList);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        //fill header of the navigation view with the username and household of the user
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header);
        final TextView navUserName = headerView.findViewById(R.id.nav_user_name);
        final TextView navHousehold = headerView.findViewById(R.id.nav_household);
        String personId = preferencesAccess.readPreferences(this, getString(R.string.personIDPreference));
        FirebaseDatabase.getInstance().getReference().child("person").child(personId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Person thisPerson = dataSnapshot.getValue(Person.class);
                navUserName.setText(thisPerson.getPersonName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("household").child(householdId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Household thisHousehold = dataSnapshot.getValue(Household.class);
                navHousehold.setText(thisHousehold.getHouseholdName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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
                        if (menuItem.getItemId() == R.id.nav_my_tasks) {
                            Intent intentT = new Intent(AllShoppingListsActivity.this, MyTasksActivity.class);
                            startActivity(intentT);
                            finish();
                            return true;
                        } else if (menuItem.getItemId() == R.id.nav_settings) {
                            Intent intentLists = new Intent(AllShoppingListsActivity.this, SettingsActivity.class);
                            startActivity(intentLists);
                            finish();
                            return true;
                        }
                        //Logout
                        else if (menuItem.getItemId() == R.id.logout) {
                            Log.i(TAG, "Logout button clicked");
                            //delete preferences
                            preferencesAccess.storePreferences(AllShoppingListsActivity.this, getString(R.string.householdIDPreference), null);
                            preferencesAccess.storePreferences(AllShoppingListsActivity.this, getString(R.string.personIDPreference), null);
                            preferencesAccess.storePreferences(AllShoppingListsActivity.this, getString(R.string.recentShoppingListNamePreference), null);
                            FirebaseAuth.getInstance().signOut();
                            Intent intentout = new Intent(AllShoppingListsActivity.this, LoginActivity.class);
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
     * Method that starts the shopping list listener and gets a list of the shopping lists of this household from the
     * firebase database.
     */
    private void startShoppingListListener(final String householdId) {
        FirebaseDatabase.getInstance().getReference().child("shoppingList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listsOfThisHousehold.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String id = child.getKey();
                    ShoppingList value = child.getValue(ShoppingList.class);
                    if (value.getIdBelongingTo().equals(householdId)) {
                        listsOfThisHousehold.put(id, value);
                    }
                }

                // start itemListener when shoppingListListener is ready and set adapter when bot are ready
                startItemListener();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    /**
     * Method that starts the item listener and gets a list of the items of this household from the
     * firebase database.
     * This is needed for the display of the amount of open items on the lists.
     */
    private void startItemListener() {
        FirebaseDatabase.getInstance().getReference().child("item").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemsOfThisHousehold.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String id = child.getKey();
                    Item value = child.getValue(Item.class);
                    itemsOfThisHousehold.put(id, value);
                }

                //fill with lists with an adapter
                Log.i(TAG, "itemsOfHousehold" + +itemsOfThisHousehold.size() + itemsOfThisHousehold.toString());
                ShoppingListsAdapter adapter = new ShoppingListsAdapter(AllShoppingListsActivity.this, listsOfThisHousehold, itemsOfThisHousehold);
                listsView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    /**
     * Method that is called when the navigation drawer menu is opened to keep track on the selection of navigation items.
     *
     * @param item in the menu that is selected
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Button that leads to screen 13 where you can create a new shopping list
     */
    @OnClick(R.id.addShoppingList)
    public void addShoppingListClicked() {
        Intent intent = new Intent(this, CreateShoppingListActivity.class);
        startActivity(intent);
        finish();
    }
}
