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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.holmal.app.holmal.model.Item;
import com.holmal.app.holmal.model.ShoppingList;
import com.holmal.app.holmal.utils.FireBaseHandling;
import com.holmal.app.holmal.utils.ItemsAdapter;
import com.holmal.app.holmal.utils.PreferencesAccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShoppingListActivity extends AppCompatActivity {

    private static final String TAG = ShoppingListActivity.class.getName();

    private HashMap<String, ShoppingList> listsOfThisHousehold = new HashMap<>();
    private HashMap<String, Item> itemsOfTheList = new HashMap<>();
    private ArrayList<String> itemIds = new ArrayList<>();

    private DrawerLayout mDrawerLayout;
    ShoppingList currentShoppingList;

    PreferencesAccess preferences = new PreferencesAccess();
    String householdId;
    String recentShoppingListName;

    String shoppingListId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        ButterKnife.bind(this);

        householdId = preferences.readPreferences(this, getString(R.string.householdIDPreference));

        //Listener for the shopping lists of this household
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
                    // TODO das aeussere if statement raus schmeissen sobald alle Personen mit idBelongingTo gespeichert werden
                    if (value.getIdBelongingTo() != null) {
                        if (value.getIdBelongingTo().equals(householdId)) {
                            Log.i(TAG, "Liste gehört zu diesem Haushalt.");
                            listsOfThisHousehold.put(id, value);
                        }
                    }
                    Log.i(TAG, "listsOfThisHousehold in for Schleife bei listener: " + listsOfThisHousehold);
                }
                getCurrentShoppingList();
                if(recentShoppingListName != null){
                    setTitle(recentShoppingListName);
                }
                else{
                    setTitle(R.string.shoppingList);
                }

                Log.i("fürSvenja", ":" + listsOfThisHousehold);


                /**
                 * //handles click on item to see detailed information
                 * list.setOnClickListener(new AdapterView.OnItemClickListener() {
                 *              @Override
                 *              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 *              if(item an der stelle hat info){
                 *              starte ItemInformationFragment
                 * });}
                 */
                /**
                 * //handles double click on item -> makes item assigned to self
                 * list.setOnTouchListener(new OnTouchListener() {
                 *     private GestureDetector gestureDetector = new GestureDetector(ShoppingListActivity.this, new GestureDetector.SimpleOnGestureListener() {
                 *         @Override
                 *         public boolean onDoubleTap(MotionEvent e) {
                 *         if(item an der stelle ist nicht assigned){
                 *         assined add person
                 *         }else{
                 *         assigned remove person
                 *         }
                 *             return super.onDoubleTap(e);
                 *         }
                 *     });
                 *     onSwipeRight()
                 *
                 */
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
            ItemsAdapter adapter = new ItemsAdapter(ShoppingListActivity.this, itemsOfTheList);
            ListView list = findViewById(R.id.list);
            list.setAdapter(adapter);

            }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        //menu that appears from the left
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

                            // Add code here to update the UI based on the item selected
                            //if my assignments is pressed in the menu you will be lead there
                            if(menuItem.getItemId() == R.id.nav_my_tasks){
                                Intent intentT = new Intent(ShoppingListActivity.this, MyTasksActivity.class);
                                startActivity(intentT);
                                return true;
                            }
                            //if all shopping lists is pressed in the menu you will be lead there
                            else if (menuItem.getItemId()==R.id.nav_shopping_lists){
                                Intent intentLists = new Intent(ShoppingListActivity.this, AllShoppingListsActivity.class);
                                startActivity(intentLists);
                                return true;
                            }
                            else if (menuItem.getItemId()==R.id.nav_settings){
                                Intent intentnav = new Intent(ShoppingListActivity.this, SettingsActivity.class);
                                startActivity(intentnav);
                                return true;
                            }
                            //Logout
                            else if (menuItem.getItemId()==R.id.logout){
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

    private void getCurrentShoppingList() {
        Log.i(TAG, "getCurrentShoppingList called");

        Log.i(TAG, "shoppingLists " + listsOfThisHousehold);
        // TODO kann dann auch wieder raus, ist aber gut zum testen
        if (listsOfThisHousehold.isEmpty()) {
            Log.i(TAG, "shoppingList is empty");
        }
        // von Haushalt -> Listen -> Liste mit namen aus Preferences
        recentShoppingListName = preferences.readPreferences(this, getString(R.string.recentShoppingListNamePreference));

        // TODO kann dann auch wieder raus, ist aber gut zum testen
        if (recentShoppingListName == null) {
            Log.i(TAG, "no recent shoppingListName");
        }
        // get object of the recent ShoppingList
        else{
            Log.i(TAG, "recentShoppingListName: " + recentShoppingListName);
            String[] keys = listsOfThisHousehold.keySet().toArray(new String[listsOfThisHousehold.size()]);
            for(int i=0; i < keys.length; i++){
                ShoppingList list = listsOfThisHousehold.get(keys[i]);
                if(recentShoppingListName.equals(list.getListName())){
                    currentShoppingList = list;
                    shoppingListId = keys[i];
                    Log.i(TAG, "currentShoppingList: " + currentShoppingList);
                    break;
                }
            }
        }

        //Log.i("FürSvenja", "current" + currentShoppingList.toString());
        HashMap<String, String> ids = currentShoppingList.getItemsOfThisList();
        if(ids != null) {
            String[] idsKeys = ids.keySet().toArray(new String[ids.size()]);
            for (int i = 0; i < ids.size(); i++) {
                itemIds.add(ids.get(idsKeys[i]));
            }
        }
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

    //Button that lets you add an item to the shopping list
    @OnClick (R.id.addItem)
    public void addItemOnClicked(){
        //TODO this does nothing so I did something wrong. Needs to be done correctly (but it doesn't hurt the program so I left it)
        Intent intent = new Intent(this, CreateItemActivity.class);
        intent.putExtra("shoppingListId", shoppingListId);
        startActivity(intent);
    }
}
