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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.holmal.app.holmal.model.Item;
import com.holmal.app.holmal.utils.FireBaseHandling;
import com.holmal.app.holmal.utils.ItemsAdapter;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShoppingList extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        ButterKnife.bind(this);

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
                                Intent intentT = new Intent(ShoppingList.this, MyTasks.class);
                                startActivity(intentT);
                                return true;
                            }
                            //if all shopping lists is pressed in the menu you will be lead there
                            else if (menuItem.getItemId()==R.id.nav_shopping_lists){
                            Intent intentLists = new Intent(ShoppingList.this, AllShoppingLists.class);
                            startActivity(intentLists);
                            return true;
                        }
                            else if (menuItem.getItemId()==R.id.nav_settings){
                                Intent intentLists = new Intent(ShoppingList.this, Settings.class);
                                startActivity(intentLists);
                                return true;
                            }
                            // For example, swap UI fragments here

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
        /**com.holmal.app.holmal.model.ShoppingList shoppingList = FireBaseHandling.getInstance()
                .getShoppingListListener().
                        getShoppingListList().
                        get(0);

        //Item[] items = FireBaseHandling.getInstance().getShoppingListListener().getShoppingListList().get(0).getItemsOfThisList();
        //TODO abfrage welche shopping list man erhält!! wichtig
        ItemsAdapter adapter = new ItemsAdapter(this, items);
        ListView list = findViewById(R.id.list);
        list.setAdapter(adapter);*/
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
        Intent intent = new Intent(this, CreateItem.class);
        startActivity(intent);
    }
}
