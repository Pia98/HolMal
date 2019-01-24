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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    private HashMap<String, ShoppingList> listsOfThisHousehold = new HashMap<>();
    private HashMap<String, Item> itemsOfTheList = new HashMap<>();
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
        startShoppingListListener();

        // Listener to get all items that are in the list
        startItemListener();

        //menu that appears from the left
        menu();

        /*navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.openItemsTab) {
                //check items that are on list and not done
                //give this list to adapter and set adapter
                    getCurrentShoppingList();
                } else if (id == R.id.doneItemsTab) {
                //check items that are on list and done
                //give resulting list to adapter
                }
                return true;
            }});*/
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

        //tab layout for open and done items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        NavigationView navigationView = findViewById(R.id.nav_view);
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
                            return true;
                        }
                        //if all shopping lists is pressed in the menu you will be lead there
                        else if (menuItem.getItemId() == R.id.nav_shopping_lists) {
                            Intent intentLists = new Intent(ShoppingListActivity.this, AllShoppingListsActivity.class);
                            startActivity(intentLists);
                            return true;
                        } else if (menuItem.getItemId() == R.id.nav_settings) {
                            Intent intentnav = new Intent(ShoppingListActivity.this, SettingsActivity.class);
                            startActivity(intentnav);
                            return true;
                        }
                        //Logout
                        else if (menuItem.getItemId() == R.id.logout) {
                            Log.i(TAG, "Logout button clicked");
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
     * Method that starts the item listener. It gets the item data from the firebase database.
     * Sets the items adapter to the list.
     */
    private void startItemListener() {
        // Listener to get all items that are in the list
        FirebaseDatabase.getInstance().getReference().child("item").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, "item listener in onCreate...");
                itemsOfTheList.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String id = child.getKey();
                    Item value = child.getValue(Item.class);
                    Log.i(TAG, "item: " + value);
                    for (int i = 0; i < itemIds.size(); i++) {
                        if (id.equals(itemIds.get(i))) {
                            itemsOfTheList.put(id, value);
                        }
                    }
                }
                //adapter
                ItemsAdapter adapter = new ItemsAdapter(ShoppingListActivity.this, itemsOfTheList, person);
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
     * firebase database. It also sets the title of the window to the name of the displayed shopping list.
     */
    private void startShoppingListListener() {
        FirebaseDatabase.getInstance().getReference().child("shoppingList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, "listener in onCreate...");
                listsOfThisHousehold.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.i(TAG, "alle Listen durchgehen");
                    String id = child.getKey();
                    ShoppingList value = child.getValue(ShoppingList.class);
                    Log.i(TAG, "ShoppingList: " + value);
                    if (value.getIdBelongingTo().equals(householdId)) {
                        Log.i(TAG, "Liste gehört zu diesem Haushalt.");
                        listsOfThisHousehold.put(id, value);
                    }

                    Log.i(TAG, "listsOfThisHousehold in for Schleife bei listener: " + listsOfThisHousehold);
                }
                getCurrentShoppingList();
                if (recentShoppingListName != null) {
                    setTitle(recentShoppingListName);
                } else {
                    setTitle(R.string.shoppingList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
    private void getCurrentShoppingList() {

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
    }

    /**
     * Method that is called when the navigation drawer menu is opened to keep track on the selection of navigation items.
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
