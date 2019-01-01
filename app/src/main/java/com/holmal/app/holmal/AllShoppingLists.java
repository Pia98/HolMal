package com.holmal.app.holmal;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.holmal.app.holmal.utils.ShoppingListsAdapter;

import butterknife.ButterKnife;
import butterknife.OnClick;
//Overview over all the shopping lists
public class AllShoppingLists extends AppCompatActivity {

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
                            Intent intentT = new Intent(AllShoppingLists.this, MyTasks.class);
                            startActivity(intentT);
                            return true;
                        }
                        else if (menuItem.getItemId()==R.id.nav_settings){
                            Intent intentLists = new Intent(AllShoppingLists.this, Settings.class);
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

        //fill with lists with an adapter
        ShoppingListsAdapter adapter = new ShoppingListsAdapter(this);
        GridView lists = findViewById(R.id.allShoppingLists);
        lists.setAdapter(adapter);
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
        Intent intent = new Intent(this, CreateShoppingList.class);
        startActivity(intent);
    }

}
