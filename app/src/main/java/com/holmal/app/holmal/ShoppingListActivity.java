package com.holmal.app.holmal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.holmal.app.holmal.model.Household;
import com.holmal.app.holmal.model.Item;
import com.holmal.app.holmal.model.Person;
import com.holmal.app.holmal.model.ShoppingList;
import com.holmal.app.holmal.utils.ItemsAdapter;
import com.holmal.app.holmal.utils.PreferencesAccess;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Class that displays a single shopping list with its items.
 * Contains a list of the items that the ItemsAdapter is set on, a button to add items to the list and access to the menu.
 */
public class ShoppingListActivity extends AppCompatActivity {

    private static final String TAG = ShoppingListActivity.class.getName();
    private HashMap<String, Item> openItemsOfTheList = new HashMap<>();
    private HashMap<String, Item> doneItemsOfTheList = new HashMap<>();
    private ArrayList<String> itemIds = new ArrayList<>();
    private HashMap<String, Person> person = new HashMap<>();
    private RecyclerView.LayoutManager layoutManager;
    private DrawerLayout mDrawerLayout;
    private ShoppingList currentShoppingList;
    private PreferencesAccess preferences = new PreferencesAccess();
    private String householdId;
    private String recentShoppingListName;
    private RecyclerView list;
    private String shoppingListId;

    /**
     * Method that initialises the class and its important features.
     * It establishes the connection to its xml file (activity_shopping_list) and starts the listeners.
     *
     * @param savedInstanceState Bundle object that contains a saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        ButterKnife.bind(this);
        list = findViewById(R.id.list);
        layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
        householdId = preferences.readPreferences(this, getString(R.string.householdIDPreference));

        // Listener for person
        startPersonListener();

        //Listener for the shopping lists of this household
        startShoppingListListener(true);

        //menu that appears from the left
        menu();
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

        mDrawerLayout = findViewById(R.id.drawer_layout);

        //fill header of the navigation view with the username and household of the user
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header);
        final TextView navUserName = headerView.findViewById(R.id.nav_user_name);
        final TextView navHousehold = headerView.findViewById(R.id.nav_household);
        String personId = preferences.readPreferences(this, getString(R.string.personIDPreference));

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

        //tab layout for open and done items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // items open
                if (tab.getPosition() == 0) {
                    // Listener to get all open items that are in the list
                    startShoppingListListener(true);
                    
                }
                // items done
                else {
                    // Listener to get all done items that are in the list
                    startShoppingListListener(false);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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

                        //if my assignments is pressed in the menu you will be lead there
                        if (menuItem.getItemId() == R.id.nav_my_tasks) {
                            Intent intentT = new Intent(ShoppingListActivity.this, MyTasksActivity.class);
                            startActivity(intentT);
                            finish();
                            return true;
                        }
                        //if all shopping lists is pressed in the menu you will be lead there
                        else if (menuItem.getItemId() == R.id.nav_shopping_lists) {
                            Intent intentLists = new Intent(ShoppingListActivity.this, AllShoppingListsActivity.class);
                            startActivity(intentLists);
                            finish();
                            return true;
                        } else if (menuItem.getItemId() == R.id.nav_settings) {
                            Intent intentnav = new Intent(ShoppingListActivity.this, SettingsActivity.class);
                            startActivity(intentnav);
                            finish();
                            return true;
                        }
                        //Logout
                        else if (menuItem.getItemId() == R.id.logout) {
                            Log.i(TAG, "Logout button clicked");
                            //delete preferences
                            PreferencesAccess preferencesAccess = new PreferencesAccess();
                            preferencesAccess.storePreferences(ShoppingListActivity.this, getString(R.string.householdIDPreference), null);
                            preferencesAccess.storePreferences(ShoppingListActivity.this, getString(R.string.personIDPreference), null);
                            preferencesAccess.storePreferences(ShoppingListActivity.this, getString(R.string.recentShoppingListNamePreference), null);
                            FirebaseAuth.getInstance().signOut();
                            Intent intentout = new Intent(ShoppingListActivity.this, LoginActivity.class);
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
     * Method that starts the listener for items that are already done. It gets the item data from the firebase database.
     * Sets the items adapter to the list.
     */
    private void startDoneItemsListener() {
        FirebaseDatabase.getInstance().getReference().child("item").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, "done item listener in onCreate...");
                doneItemsOfTheList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String id = child.getKey();
                    Item value = child.getValue(Item.class);
                    Log.i(TAG, "item: " + value);
                    for (int i = 0; i < itemIds.size(); i++) {
                        if (id.equals(itemIds.get(i)) && value.isDone()) {
                            doneItemsOfTheList.put(id, value);
                        }
                    }
                }
                //adapter
                ItemsAdapter adapter = new ItemsAdapter(ShoppingListActivity.this, doneItemsOfTheList, person);
                RecyclerView list = findViewById(R.id.list);
                list.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Method that starts the listener for items that are not done. It gets the item data from the firebase database.
     * Sets the items adapter to the list.
     */
    private void startOpenItemsListener() {
        FirebaseDatabase.getInstance().getReference().child("item").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, "open item listener in onCreate...");
                openItemsOfTheList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String id = child.getKey();
                    Item value = child.getValue(Item.class);
                    Log.i(TAG, "item: " + value);
                    for (int i = 0; i < itemIds.size(); i++) {
                        if (id.equals(itemIds.get(i)) && !value.isDone()) {
                            openItemsOfTheList.put(id, value);
                        }
                    }
                }
                //adapter
                ItemsAdapter adapter = new ItemsAdapter(ShoppingListActivity.this, openItemsOfTheList, person);
                RecyclerView list = findViewById(R.id.list);
                list.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Method that starts the shopping list listener and gets a list of the shopping lists of this household from the
     * firebase database.
     * It also sets the title of the window to the name of the displayed shopping list.
     */
    private void startShoppingListListener(final boolean open) {
        recentShoppingListName = preferences.readPreferences(this, getString(R.string.recentShoppingListNamePreference));
        if (recentShoppingListName != null) {
            FirebaseDatabase.getInstance().getReference().child("shoppingList").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.i(TAG, "started listener on shoppingList");
                    boolean exists = false;
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        String id = child.getKey();
                        ShoppingList value = child.getValue(ShoppingList.class);
                        if (value.getIdBelongingTo().equals(householdId) && value.getListName().equals(recentShoppingListName)) {
                            exists = true;
                            setTitle(recentShoppingListName);
                            Log.i(TAG, "FOUND!");
                            currentShoppingList = value;
                            shoppingListId = id;
                            Log.i(TAG, "CurrentShoppingList Name: " + currentShoppingList.getListName());

                            HashMap<String, String> ids = currentShoppingList.getItemsOfThisList();
                            if (ids != null) {
                                String[] idsKeys = ids.keySet().toArray(new String[ids.size()]);
                                for (int i = 0; i < ids.size(); i++) {
                                    itemIds.add(ids.get(idsKeys[i]));
                                }
                            }

                            if (open) {
                                startOpenItemsListener();
                            } else {
                                startDoneItemsListener();
                            }
                            break;
                        }
                    }
                    if(!exists){
                        preferences.storePreferences(ShoppingListActivity.this, getString(R.string.recentShoppingListNamePreference), null);
                        Intent intent = new Intent(ShoppingListActivity.this, AllShoppingListsActivity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            Log.i(TAG, "no recentShoppingListName found!");
            Toast.makeText(getApplicationContext(), "No recent shopping list found!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, AllShoppingListsActivity.class);
            startActivity(intent);

        }
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

    /**
     * Getter for the current shopping list. The most recent shopping list name is stored in the preferences.
     */
   /* private void getCurrentShoppingList() {

        Log.i(TAG, "getCurrentShoppingList called");
        Log.i(TAG, "shoppingLists " + listsOfThisHousehold);

        // von Haushalt -> Listen -> Liste mit namen aus Preferences
        recentShoppingListName = preferences.readPreferences(this, getString(R.string.recentShoppingListNamePreference));

        // get object of the recent ShoppingList
        if (recentShoppingListName != null) {
            Log.i(TAG, "recentShoppingListName: " + recentShoppingListName);
            String[] keys = listsOfThisHousehold.keySet().toArray(new String[listsOfThisHousehold.size()]);
            for (int i = 0; i < keys.length; i++) {
                ShoppingList list = listsOfThisHousehold.get(keys[i]);
                if (recentShoppingListName.equals(list.getListName())) {
                    currentShoppingList = list;
                    shoppingListId = keys[i];
                    Log.i(TAG, "currentShoppingList: " + currentShoppingList);
                    break;
                }
            }
        }
        HashMap<String, String> ids = currentShoppingList.getItemsOfThisList();
        if (ids != null) {
            String[] idsKeys = ids.keySet().toArray(new String[ids.size()]);
            for (int i = 0; i < ids.size(); i++) {
                itemIds.add(ids.get(idsKeys[i]));
            }
        }
    }*/

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
     * Button that lets you add an item to the shopping list. References addItem Button in activity_shopping_list.
     * Pressing this button starts the CreateItemActivity.
     */
    @OnClick(R.id.addItem)
    public void addItemOnClicked() {
        Intent intent = new Intent(this, CreateItemActivity.class);
        intent.putExtra("shoppingListId", shoppingListId);
        startActivity(intent);
    }
}
