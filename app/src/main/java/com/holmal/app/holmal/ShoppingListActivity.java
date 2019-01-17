package com.holmal.app.holmal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.holmal.app.holmal.model.Item;
import com.holmal.app.holmal.model.ShoppingList;
import com.holmal.app.holmal.utils.FireBaseHandling;
import com.holmal.app.holmal.utils.ItemsAdapter;
import com.holmal.app.holmal.utils.PreferencesAccess;
import com.holmal.app.holmal.utils.ShoppingListListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShoppingListActivity extends AppCompatActivity {

    private static final String TAG = ShoppingListActivity.class.getName();

    private DrawerLayout mDrawerLayout;
    com.holmal.app.holmal.model.ShoppingList currentShoppingList;
    List<ShoppingList> shoppingListList = new ArrayList<>();
    Item[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        ButterKnife.bind(this);


        try {
            getCurrentShoppingList();
            PreferencesAccess preferences = new PreferencesAccess();
            setTitle(preferences.readPreferences(this, getString(R.string.recentShoppingListNamePreference)));
        } catch (Throwable e) {
            Log.e(TAG, "Error " + e);
            e.printStackTrace();
            setTitle(R.string.shoppingList);
        }



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

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
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
            }});


        Log.i("fürSvenja", FireBaseHandling.getInstance().getShoppingListListener().getShoppingListList().toString());
        Log.i("fürSvenja", "interner Listener: " + shoppingListList);

     /**   ShoppingListListener newListener = FireBaseHandling.getInstance().getShoppingListListener();

        List<ShoppingList> filledList = FireBaseHandling.getInstance().getShoppingListListener().getShoppingListList();
        if(filledList.isEmpty()){
            Log.i("fürSvenja", "nothing yet");
        }
        else{
        //fill List with the items with an adapter
       items = FireBaseHandling.getInstance().getShoppingListListener().getShoppingListList().get(0).getItemsOfThisList();
        //TODO abfrage welche shopping list man erhält!! wichtig
       //TODO auskommentieren um items anzuzeigen, wenn liste der listen nicht [] ist
        ItemsAdapter adapter = new ItemsAdapter(this, items);
        ListView list = findViewById(R.id.list);
        list.setAdapter(adapter);}*/

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
         */
    }

    private void getCurrentShoppingList() {
        Log.i(TAG, "getCurrentShoppingList called");


        // von Haushalt -> Listen -> Liste mit namen aus Preferences
        PreferencesAccess preferences = new PreferencesAccess();
        String recentShoppingListName = preferences.readPreferences(this, getString(R.string.recentShoppingListNamePreference));
        List<com.holmal.app.holmal.model.ShoppingList> shoppingLists =
                FireBaseHandling.getInstance().getShoppingListListener().getShoppingListList();
        Log.i(TAG, "shoppingLists " + shoppingLists);

        if(recentShoppingListName == null){
            Log.i(TAG, "no recent shoppingListName");
            //recentShoppingListName = shoppingLists.get(0).getListName();
            if(shoppingLists.isEmpty()){
                Log.i(TAG, "shoppingList is empty");
            }
        }
        Log.i(TAG, "recentShoppingListName: " + recentShoppingListName);

        for (com.holmal.app.holmal.model.ShoppingList shoppingList : shoppingLists) {
            if (recentShoppingListName.equals(shoppingList.getListName())) {
                currentShoppingList = shoppingList;
                Log.i(TAG, "currentShoppingList: " + currentShoppingList);
                break;
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
        startActivity(intent);
    }
    


}
