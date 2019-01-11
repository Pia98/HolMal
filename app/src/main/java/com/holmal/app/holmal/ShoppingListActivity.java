package com.holmal.app.holmal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.holmal.app.holmal.model.ShoppingList;
import com.holmal.app.holmal.utils.FireBaseHandling;
import com.holmal.app.holmal.utils.PreferencesAccess;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShoppingListActivity extends AppCompatActivity {

    private static final String TAG = ShoppingListActivity.class.getName();

    private DrawerLayout mDrawerLayout;
    ShoppingList currentShoppingList;
    // TODO change hardcoded id
    String shoppingListId = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        ButterKnife.bind(this);
/*

        try {
            getCurrentShoppingList();
        } catch (Throwable e) {
            Log.e(TAG, "Error " + e);
            e.printStackTrace();
        }

*/
        //menu that appears from the left
        Toolbar toolbar = findViewById(R.id.menu);
        setSupportActionBar(toolbar);
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


        Log.i("fürSvenja", FireBaseHandling.getInstance().getShoppingListListener().getShoppingListList().toString());
        //fill List with the items with an adapter
      /**  Item[] items = FireBaseHandling.getInstance().getShoppingListListener().getShoppingListList().get(0).getItemsOfThisList();
        //TODO abfrage welche shopping list man erhält!! wichtig
       //TODO auskommentieren um items anzuzeigen, wenn liste der listen nicht [] ist
        ItemsAdapter adapter = new ItemsAdapter(this, items);
        ListView list = findViewById(R.id.list);
        list.setAdapter(adapter);*/
        }

    private void getCurrentShoppingList() {
        Log.i(TAG, "getCurrentShoppingList called");


        // von Haushalt -> Listen -> Liste mit namen aus Preferences
        PreferencesAccess preferences = new PreferencesAccess();
        String householdId = preferences.readPreferences(this, getString(R.string.householdIDPreference));
        String recentShoppingListName = preferences.readPreferences(this, getString(R.string.recentShoppingListNamePreference));
        List<ShoppingList> shoppingLists =
                FireBaseHandling.getInstance().getShoppingListListener().getShoppingListList();
        Log.i(TAG, "shoppingLists " + shoppingLists);
        if (shoppingLists.isEmpty()) {
            Log.i(TAG, "shoppingList is empty");
            //shoppingLists = FireBaseHandling.getInstance().initializeShoppingList(householdId);
        }
        if (recentShoppingListName == null) {
            Log.i(TAG, "no recent shoppingListName");
            //recentShoppingListName = shoppingLists.get(0).getListName();
        }
        Log.i(TAG, "recentShoppingListName: " + recentShoppingListName);
        for (ShoppingList shoppingList : shoppingLists) {
            if (recentShoppingListName.equals(shoppingList.getListName())) {
                currentShoppingList = shoppingList;
                shoppingListId = currentShoppingList.getStoreId();
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
        intent.putExtra("shoppingListId", shoppingListId);
        startActivity(intent);
    }
}
