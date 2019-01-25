package com.holmal.app.holmal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
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
import com.holmal.app.holmal.utils.FireBaseHandling;
import com.holmal.app.holmal.utils.FragmentHandling;
import com.holmal.app.holmal.utils.PreferencesAccess;
import com.holmal.app.holmal.utils.SettingsAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = SettingsActivity.class.getName();


    // TODO set text, so that it is not hardcoded anymore!
    @BindView(R.id.householdName)
    TextView householdNameText;

    // TODO set text, so that it is not hardcoded anymore!
    @BindView(R.id.thisHouseholdId)
    TextView householdIdText;

    @BindView(R.id.editHouseholdNameText)
    EditText editHousholdNameText;

    @BindView(R.id.editHouseholdNameDone)
    ImageButton editHouseholdNameDone;

    String householdId;
    private DrawerLayout mDrawerLayout;

    private HashMap<String, Person> joiningPerson = new HashMap<>();
    private HashMap<String, ShoppingList> listsOfThisHousehold = new HashMap<>();
    private HashMap<String, Item> itemsOfThisHousehold = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        editHousholdNameText.setVisibility(View.INVISIBLE);
        editHouseholdNameDone.setVisibility(View.INVISIBLE);

        PreferencesAccess preferencesAccess = new PreferencesAccess();
        householdId = preferencesAccess.readPreferences(this, getString(R.string.householdIDPreference));

        startHouseholdListener();

        //menu that appears from the left
        menu();

        startPersonListener();

        //Listener for shopping list
        startShoppingListListener();

        //Listener for items
        startItemListener();
    }

    private void startItemListener() {
        FirebaseDatabase.getInstance().getReference().child("item").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, "listener in onCreate...");
                itemsOfThisHousehold.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.i(TAG, "alle Listen durchgehen");
                    String id = child.getKey();
                    Item value = child.getValue(Item.class);
                    Log.i(TAG, "Item: " + value);
                    itemsOfThisHousehold.put(id, value);
                    Log.i(TAG, "listsOfThisHousehold in for Schleife bei listener: " + itemsOfThisHousehold);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void startPersonListener() {
        FirebaseDatabase.getInstance().getReference().child("person").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, "listener in onCreate...");
                joiningPerson.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.i(TAG, "alle Personen durchgehen");
                    String id = child.getKey();
                    Person value = child.getValue(Person.class);
                    Log.i(TAG, "Person: " + value + "householdId: " + householdId);
                    if (value.getIdBelongingTo().equals(householdId)) {
                        Log.i(TAG, "Person gehört zu diesem Haushalt (householdId: " + householdId + ")");
                        joiningPerson.put(id, value);
                    }

                    Log.i(TAG, "joiningPerson in for Schleife bei listener: " + joiningPerson);
                }
                SettingsAdapter adapter = new SettingsAdapter(SettingsActivity.this, joiningPerson);
                ListView list = findViewById(R.id.listOfHouseholdMembers);
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void menu() {
        Toolbar toolbar = findViewById(R.id.menu);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        setTitle(R.string.settings);

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
                            Intent intentT = new Intent(SettingsActivity.this, MyTasksActivity.class);
                            startActivity(intentT);
                            finish();
                            return true;
                        }
                        //if all shopping lists is pressed in the menu you will be lead there
                        else if (menuItem.getItemId() == R.id.nav_shopping_lists) {
                            Intent intentLists = new Intent(SettingsActivity.this, AllShoppingListsActivity.class);
                            startActivity(intentLists);
                            finish();
                            return true;
                        }
                        //Logout
                        else if (menuItem.getItemId() == R.id.logout) {
                            Log.i("TAG", "Logout button clicked");
                            //delete preferences
                            PreferencesAccess preferencesAccess = new PreferencesAccess();
                            preferencesAccess.storePreferences(SettingsActivity.this, getString(R.string.householdIDPreference), null);
                            preferencesAccess.storePreferences(SettingsActivity.this, getString(R.string.personIDPreference), null);
                            preferencesAccess.storePreferences(SettingsActivity.this, getString(R.string.recentShoppingListNamePreference), null);
                            FirebaseAuth.getInstance().signOut();
                            Intent intentout = new Intent(SettingsActivity.this, LoginActivity.class);
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

    private void startHouseholdListener() {
        FirebaseDatabase.getInstance().getReference().child("household").child(householdId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Household thisHousehold = dataSnapshot.getValue(Household.class);
                householdNameText.setText(thisHousehold.getHouseholdName());
                householdIdText.setText(dataSnapshot.getKey());
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

    @OnClick(R.id.editHouseholdName)
    public void editHouseholdNameClicked(){
        editHousholdNameText.setVisibility(View.VISIBLE);
        editHouseholdNameDone.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.editHouseholdNameDone)
    public void setEditHouseholdNameDone(){
        String newName = editHousholdNameText.getText().toString();
        String id = householdIdText.getText().toString();
        if(!newName.isEmpty()){
            FireBaseHandling.getInstance().editHousholdName(newName, id);
            editHousholdNameText.setVisibility(View.INVISIBLE);
            editHouseholdNameDone.setVisibility(View.INVISIBLE);
            householdNameText.setText(newName);
        }else{
            editHousholdNameText.setVisibility(View.INVISIBLE);
            editHouseholdNameDone.setVisibility(View.INVISIBLE);
        }
    }


    @OnClick(R.id.leaveHousehold)
    public void leaveHouseholdClicked() {

        //pop up that asks the user if they are sure
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.leaveConfirmation);
        builder.setCancelable(true);
        builder.setPositiveButton(
                R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intentout = new Intent(SettingsActivity.this, StartActivity.class);
                        startActivity(intentout);

                        //remove member from household
                        PreferencesAccess preferencesAccess = new PreferencesAccess();
                        String householdID = preferencesAccess.readPreferences(SettingsActivity.this, getString(R.string.householdIDPreference));
                        String personID = preferencesAccess.readPreferences(SettingsActivity.this, getString(R.string.personIDPreference));

                        //delete household if household is empty now
                        //hasn't synchronized by then so use former size - 1
                        if (joiningPerson.size() - 1 == 0) {
                            //delete all shopping Lists belonging to this household
                            //and corresponding items
                            for (int i = 0; i < listsOfThisHousehold.size(); i++) {
                                String[] keys = listsOfThisHousehold.keySet().toArray(new String[listsOfThisHousehold.size()]);
                                ShoppingList shoppingList = listsOfThisHousehold.get(keys[i]);
                                //list of items (id) that belong to household
                                HashMap<String, String> itemsToDelete = shoppingList.getItemsOfThisList();
                                //iterates over all items
                                String[] allItemsKeys = itemsOfThisHousehold.keySet().toArray(new String[itemsOfThisHousehold.size()]);
                                String[] itemDeleteKeys = itemsToDelete.keySet().toArray(new String[itemsToDelete.size()]);
                                for (int j = 0; j < itemsOfThisHousehold.size(); j++) {
                                    for (int k = 0; k < itemsToDelete.size(); k++) {
                                        if (allItemsKeys[j].equals(itemsToDelete.get(itemDeleteKeys[k]))) {
                                            FireBaseHandling.getInstance().deleteItem(allItemsKeys[j]);
                                        }
                                    }
                                }
                                //delete shoppingList
                                FireBaseHandling.getInstance().deleteShoppingList(keys[i]);
                            }
                            //deletes the household
                            Log.e("deleteHousehold", householdID + " has been deleted");
                            FireBaseHandling.getInstance().deleteHousehold(householdID);

                        }
                        else{
                            //unassign tasks that this user has taken on
                            for (int i = 0; i<itemsOfThisHousehold.size(); i++){
                                String[] allItemsKeys = itemsOfThisHousehold.keySet().toArray(new String[itemsOfThisHousehold.size()]);
                                if(personID.equals(itemsOfThisHousehold.get(allItemsKeys[i]).getItsTask())){
                                    itemsOfThisHousehold.get(allItemsKeys[i]).setItsTask("");
                                }
                            }
                        }

                        //delete preferences
                        preferencesAccess.storePreferences(SettingsActivity.this, getString(R.string.householdIDPreference), null);
                        preferencesAccess.storePreferences(SettingsActivity.this, getString(R.string.personIDPreference), null);
                        preferencesAccess.storePreferences(SettingsActivity.this, getString(R.string.recentShoppingListNamePreference), null);
                        //delete person
                        FireBaseHandling.getInstance().deletePerson(personID);

                       /* FirebaseAuth.getInstance().signOut();
                        Intent intentout = new Intent(SettingsActivity.this, LoginActivity.class);
                        startActivity(intentout);*/
                    }
                });
        builder.setNegativeButton(
                R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();



    }
}
