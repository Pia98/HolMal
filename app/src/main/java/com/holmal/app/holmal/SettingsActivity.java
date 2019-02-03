package com.holmal.app.holmal;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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
import com.holmal.app.holmal.ui.registrationfragment1.RadioGridGroup;
import com.holmal.app.holmal.utils.FireBaseHandling;
import com.holmal.app.holmal.utils.PreferencesAccess;
import com.holmal.app.holmal.utils.SettingsAdapter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * In the settings a user can change the household name and has access to the household id in case
 * they want to invite someone to the household. There is an overview of the people in the
 * household including their colours. Additionally the settings activity provides a possibility to
 * leave the household for good.
 * Is accessed via the menu. Uses the activity_settings layout and the SettingsAdapter.
 */
public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = SettingsActivity.class.getName();

    @BindView(R.id.householdName)
    TextView householdNameText;

    @BindView(R.id.thisHouseholdId)
    TextView householdIdText;

    @BindView(R.id.personEmail)
    TextView personEmail;

    //----------- editing stuff
    @BindView(R.id.editHouseholdNameText)
    EditText editHouseholdNameText;

    @BindView(R.id.editNameText)
    EditText editNameText;

    @BindView(R.id.colorChoice)
    RadioGridGroup colorChoice;

    @BindView(R.id.editHouseholdNameDone)
    ImageButton editDone;

    @BindView(R.id.settingsEditAble)
    ConstraintLayout settingsEditable;

    String householdId;
    private DrawerLayout mDrawerLayout;

    private HashMap<String, Person> joiningPerson = new HashMap<>();
    private HashMap<String, ShoppingList> listsOfThisHousehold = new HashMap<>();
    private HashMap<String, Item> itemsOfThisHousehold = new HashMap<>();
    private Person myPerson = new Person();
    private String myPersonId;

    PreferencesAccess preferencesAccess = new PreferencesAccess();


    /**
     * Method that initialises the class and its important features.
     * Starts all listeners and sets menu.
     *
     * @param savedInstanceState Bundle object that contains a saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        settingsEditable.setVisibility(View.GONE);

        householdId = preferencesAccess.readPreferences(this, getString(R.string.householdIDPreference));

        //Listener for items
        startItemListener();

        //Listener for shopping list
        startShoppingListListener();

        //Listener for person
        startPersonListener();

        //Listener for household
        startHouseholdListener();

        //menu that appears from the left
        menu();
    }

    /**
     * Method that starts the item listener and gets a list of the items of this household from the
     * firebase database.
     * Is needed in the settings to delete all items in case the last member leaves the household.
     */
    private void startItemListener() {
        FirebaseDatabase.getInstance().getReference().child("item").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemsOfThisHousehold.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String id = child.getKey();
                    Item value = child.getValue(Item.class);
                    itemsOfThisHousehold.put(id, value);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Method that starts the shopping list listener and gets a list of the shopping lists of this
     * household from the firebase database.
     * Is needed in the settings to delete all shopping lists in case the last member leaves
     * the household.
     */
    private void startShoppingListListener() {
        FirebaseDatabase.getInstance().getReference().child("shoppingList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listsOfThisHousehold.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String id = child.getKey();
                    ShoppingList value = child.getValue(ShoppingList.class);
                    if (value.getIdBelongingTo().equals(householdId)) {
                        listsOfThisHousehold.put(id, value);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Method that starts the person listener and gets a list of the members of this
     * household from the firebase database.
     * Is needed in the settings to delete a person and unassign their tasks in case they leave the
     * household.
     */
    private void startPersonListener() {
        FirebaseDatabase.getInstance().getReference().child("person").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                joiningPerson.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String id = child.getKey();
                    Person value = child.getValue(Person.class);
                    if (value.getIdBelongingTo().equals(householdId)) {
                        joiningPerson.put(id, value);
                        if(id.equals(preferencesAccess.readPreferences(SettingsActivity.this, getString(R.string.personIDPreference)))){
                            myPerson = value;
                            myPersonId = id;
                            personEmail.setText(value.getEmail());
                        }
                    }
                }
                SettingsAdapter adapter = new SettingsAdapter(SettingsActivity.this, joiningPerson);
                ListView list = findViewById(R.id.listOfHouseholdMembers);
                list.setAdapter(adapter);
                int height = 0;

                int items = adapter.getCount();
                for(int i = 0; i <items; i ++){
                    View childView = adapter.getView(i,null, list);
                    childView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    height += childView.getMeasuredHeight();
                }
                Log.i(TAG, "Height of List: " + height);
                ConstraintLayout.LayoutParams listParams = (ConstraintLayout.LayoutParams) list.getLayoutParams();
                listParams.height = height;
                list.setLayoutParams(listParams);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Method that starts the household listener and gets the household name from the firebase database.
     */
    private void startHouseholdListener() {
        FirebaseDatabase.getInstance().getReference().child("household").child(householdId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Household thisHousehold = dataSnapshot.getValue(Household.class);
                householdNameText.setText(thisHousehold.getHouseholdName());
                editHouseholdNameText.setText(thisHousehold.getHouseholdName());
                householdIdText.setText(dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Method that adds a navigation drawer to the class. This is used as a menu to enable
     * navigation within the app. Sets menu as a toolbar.
     * Uses the drawer_layout as a layout for the drawer and specifies the behaviour if an item
     * in the navigation menu is clicked.
     * This method also sets a listener to the navigation drawer so that it opens and closes.
     */
    private void menu() {
        Toolbar toolbar = findViewById(R.id.menu);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        setTitle(R.string.settings);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        //fill header of the navigation view with the username and household of the user
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header);
        final TextView navUserName = headerView.findViewById(R.id.nav_user_name);
        final TextView navHousehold = headerView.findViewById(R.id.nav_household);
        String personId = preferencesAccess.readPreferences(this, getString(R.string.personIDPreference));

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
     * Button that lets you change the name of the household.
     * Sets edit text for this visible.
     */
    @OnClick(R.id.editHouseholdName)
    public void editHouseholdNameClicked(){
        settingsEditable.setVisibility(View.VISIBLE);
        editNameText.setText(myPerson.getPersonName());
    }

    /**
     * Button that indicates that the user is done changing the household name.
     * Changes are saved and displayed and the edit text is set invisible again.
     */
    @OnClick(R.id.editHouseholdNameDone)
    public void setEditDone(){
        final String householdId = householdIdText.getText().toString();

        final String newHouseholdName = editHouseholdNameText.getText().toString();
        final String newPersonName = editNameText.getText().toString();
        final int newColor = colorChoice.getCheckedRadioButtonId();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String message = getString(R.string.editSettingsConfirmation) + "\n";
        if(!newHouseholdName.isEmpty() && (!newHouseholdName.equals(householdNameText.getText().toString()))) {
            message = message + getString(R.string.householdName) + ": " + newHouseholdName + "\n";
        }
        if(!newPersonName.isEmpty() &&(!newPersonName.equals(myPerson.getPersonName()))){
            message = message + getString(R.string.username) + ": " + newPersonName + "\n";
        }
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton(
                R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if(!newHouseholdName.isEmpty() && (!newHouseholdName.equals(householdNameText.getText().toString()))) {
                            FireBaseHandling.getInstance().editHousholdName(newHouseholdName, householdId);
                        }
                        if(!newPersonName.isEmpty() &&(!newPersonName.equals(myPerson.getPersonName()))){
                            boolean checked = checkNameTaken(newPersonName);
                            if(checked) {
                                FireBaseHandling.getInstance().editPersonName(newPersonName, myPersonId);
                            }
                        }
                        if(newColor != -1){
                            boolean checked = checkColourTaken(newColor);
                            if(checked){
                                FireBaseHandling.getInstance().editPersonFarbe(newColor, myPersonId);
                            }
                        }

                        settingsEditable.setVisibility(View.GONE);
                    }
                });
        builder.setNegativeButton(
                R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        settingsEditable.setVisibility(View.GONE);
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Button that indicates that the user is done changing the account settings
     * Changes are saved and displayed and the edit section is set gone again.
     */
    @OnClick(R.id.accountEditAble)
    public void editAccount(){

        Toast.makeText(getApplicationContext(), "test worked", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, EditAccountActivity.class);
        startActivity(intent);
        finish();


       /* if(!newEmail.isEmpty() && !newEmail.equals(myPerson.getEmail())){
            firebaseAuth.signInWithEmailAndPassword().updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.i(LoginActivity.class.getName(), "EmailUpdate successful");
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(getApplicationContext(), getString(R.string.ErrorLoginAlreadyExists), Toast.LENGTH_SHORT).show();
                        }
                        Log.e(LoginActivity.class.getName(), "Registration failed: " + task.getException().getMessage());
                    }
                }
            });
        }*/
    }

    /**
     * Method that puts the household ID into the ClipData. This makes it easier to invite people.
     */
    @OnClick(R.id.copyHouseholdId)
    public void copyHouseholdId() {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Hol Mal household id", householdId);
        clipboardManager.setPrimaryClip(clipData);
    }

    /**
     * Button that lets a user leave the household.
     * Includes a pop up that asks for verification. If the user is sure they are deleted from the
     * household and their tasks are unassigned.
     * In case the household is empty then the household, its lists and its items are deleted as well.
     */
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

                        startActivity(intentout);
                       finish();
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

    private boolean checkColourTaken(int chosenColour){
        Iterator iterator = joiningPerson.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Person person = (Person) entry.getValue();
            if (chosenColour == person.getColor()) {
                Toast.makeText(getApplicationContext(), R.string.ErrorColorTaken, Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    private boolean checkNameTaken(String name){
        Iterator iterator = joiningPerson.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Person person = (Person) entry.getValue();
            if (name.equals(person.getPersonName())) {
                Toast.makeText(getApplicationContext(), R.string.ErrorNameTaken, Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }
}
