package com.holmal.app.holmal;

import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.firebase.auth.FirebaseAuth;
import com.holmal.app.holmal.model.ShoppingList;
import com.holmal.app.holmal.utils.FireBaseHandling;
import com.holmal.app.holmal.utils.ShoppingListsAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
//Overview over all the shopping lists
public class AllShoppingListsActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_shopping_lists);
        ButterKnife.bind(this);

        //menu that appears from the left
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
                        if(menuItem.getItemId() == R.id.nav_my_tasks){
                            Intent intentT = new Intent(AllShoppingListsActivity.this, MyTasksActivity.class);
                            startActivity(intentT);
                            return true;
                        }
                        else if (menuItem.getItemId()==R.id.nav_settings){
                            Intent intentLists = new Intent(AllShoppingListsActivity.this, SettingsActivity.class);
                            startActivity(intentLists);
                            return true;
                        }
                        //Logout
                        else if (menuItem.getItemId()==R.id.logout){
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

        //fill with lists with an adapter
        List<ShoppingList> shoppingLists = FireBaseHandling.getInstance().getShoppingListListener().getShoppingListList();
        ShoppingListsAdapter adapter = new ShoppingListsAdapter(this, shoppingLists);
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
    public void addShoppingListClicked(){
        Intent intent = new Intent(this, CreateShoppingListActivity.class);
        startActivity(intent);
    }



}
