package com.holmal.app.holmal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.holmal.app.holmal.model.ShoppingList;
import com.holmal.app.holmal.utils.PreferencesAccess;
import com.holmal.app.holmal.utils.ShoppingListsAdapter;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;

//Overview over all the shopping lists
public class AllShoppingListsActivity extends AppCompatActivity {
    private static final String TAG = AllShoppingListsActivity.class.getName();

    private DrawerLayout mDrawerLayout;
    private HashMap<String, ShoppingList> listsOfThisHousehold = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_shopping_lists);
        ButterKnife.bind(this);

        //menu that appears from the left
        menu();

        PreferencesAccess preferencesAccess = new PreferencesAccess();
        final String householdId = preferencesAccess.readPreferences(this, getString(R.string.householdIDPreference));


        startShoppingListListener(householdId);
    }

    private void menu() {
        Toolbar toolbar = findViewById(R.id.menu);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        setTitle(R.string.shoppingLists);

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
                        if (menuItem.getItemId() == R.id.nav_my_tasks) {
                            Intent intentT = new Intent(AllShoppingListsActivity.this, MyTasksActivity.class);
                            startActivity(intentT);
                            return true;
                        } else if (menuItem.getItemId() == R.id.nav_settings) {
                            Intent intentLists = new Intent(AllShoppingListsActivity.this, SettingsActivity.class);
                            startActivity(intentLists);
                            return true;
                        }
                        //Logout
                        else if (menuItem.getItemId() == R.id.logout) {
                            Log.i("TAG", "Logout button clicked");
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

    private void startShoppingListListener(final String householdId) {
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
                //fill with lists with an adapter
                ShoppingListsAdapter adapter = new ShoppingListsAdapter(AllShoppingListsActivity.this, listsOfThisHousehold);
                GridView lists = findViewById(R.id.allShoppingLists);
                lists.setAdapter(adapter);

                //handle clicks on the lists
                lists.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.i("FürSvenja", "Open list from allshoppinglists");
                        //TODO open correct shopping list (-> set activeShoppingList to that one)
                        //Intent intent = new Intent(AllShoppingLists.this, ShoppingList.class);
                        //startActivity(intent);
                    }
                });
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


    //Button that leads to screen 13
    @OnClick(R.id.addShoppingList)
    public void addShoppingListClicked() {
        Intent intent = new Intent(this, CreateShoppingListActivity.class);
        startActivity(intent);
    }


}
