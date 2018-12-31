package com.holmal.app.holmal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.holmal.app.holmal.model.Household;
import com.holmal.app.holmal.model.Person;
import com.holmal.app.holmal.model.ShoppingList;
import com.holmal.app.holmal.utils.FireBaseHandling;
import com.holmal.app.holmal.utils.FragmentHandling;
import com.holmal.app.holmal.utils.PreferencesAccess;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateHousehold extends AppCompatActivity implements PersonalInput.OnFragmentInteractionListener {

    /**
     * LOG_TAG
     */
    private static final String TAG = CreateHousehold.class.getName();

    /**
     * fragments
     */
    Fragment currentFragment;

    /**
     * Strings
     */
    String userNameString;
    String houseHoldNameString;
    int chosenColorId;
    String householdId;

    /**
     * Handling classes
     */
    //StorePersonHandling fireBaseHandling = new StorePersonHandling();
    FireBaseHandling fireBaseHandling = new FireBaseHandling();
    FragmentHandling fragmentHandling = new FragmentHandling();

    PreferencesAccess preferences = new PreferencesAccess();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_household);
        ButterKnife.bind(this);

        fragmentHandling.putFragment(currentFragment,
                PersonalInput.newInstance(),
                getSupportFragmentManager(),
                R.id.fragmentContainerCreateHousehold);

    }

    @OnClick(R.id.createHouseholdDone)
    public void createHouseholdDoneClick() {
        if (validate()) {
            // create list to store all household members. Started with person that has created
            Person admin = new Person(userNameString, chosenColorId);
            ArrayList<Person> personList = new ArrayList<>();
            personList.add(admin);
            // create default ShoppingList when household created
            String category = null;
            ShoppingList defaultList = new ShoppingList(getString(R.string.defaultShoppingList), category);
            ArrayList<ShoppingList> shoppingLists = new ArrayList<>();
            shoppingLists.add(defaultList);
            // create household with name, persons, defaultShoppingList
            Household household = new Household(houseHoldNameString, personList, shoppingLists);
            householdId = fireBaseHandling.storeNewHousehold(household);
            // HaushaltID in preferences speichern
            preferences.storePreferences(this, getString(R.string.householdIDPreference), householdId);


            Log.i(TAG, "householdID: " + householdId);


            Log.i(TAG, "store person: name - " + userNameString + ", color - " + chosenColorId);
            // speichert eine Person mit Username und Farbe auf Datenbank
            // todo not needed anymore
            fireBaseHandling.storePersonOnDatabase(userNameString, chosenColorId);

            Intent intent = new Intent(this, RegistrationActivity.class);
            // RegistrationFragment1 has to be drawn
            intent.putExtra("fragmentNumber", 1);
            intent.putExtra("userName", userNameString);
            intent.putExtra("householdName", houseHoldNameString);
            intent.putExtra("householdId", householdId);
            startActivity(intent);
        }
    }

    //checks to see if a household name was chosen, a name was chosen that is unique and if a colour was chosen
    public Boolean validate() {
        EditText userName = (EditText) findViewById(R.id.userNameInput);
        EditText householdName = (EditText) findViewById(R.id.householdNameInput);
        userNameString = userName.getText().toString();
        houseHoldNameString = householdName.getText().toString();
        if (houseHoldNameString.isEmpty()) {
            Toast.makeText(this, R.string.ErrorEnterHouseholdName, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (userNameString.isEmpty()) {
            Toast.makeText(this, R.string.ErrorEnterName, Toast.LENGTH_SHORT).show();
            return false;
        }
        //TODO check if name is not taken;
        /*
        if (userName.getText.toString() is in List of Names in Household)
         Toast.makeText(this, ErrorNameTaken, Toast.LENGTH_SHORT).show();
         return false;
         */
        else return checkColours();

    }

    /*
    Method that checks if a colour button has been chosen. Users must choose a colour before entering a household.
     */
    public Boolean checkColours() {

        RadioGroup colourChooser = findViewById(R.id.colorChoice);
        //check if a button was chosen
        if (colourChooser.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, R.string.ErrorChoseColor, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            chosenColorId = colourChooser.getCheckedRadioButtonId();

            Log.i(TAG, "Chosen color ID: " + chosenColorId);
            // just to get the id of the color
//            switch(chosenColorId){
//                case R.id.color1:
//                    Log.i(TAG, "Color to ID color1: " + chosenColorId);
//                    break;
//                case R.id.color2:
//                    Log.i(TAG, "Color to ID color2: " + chosenColorId);
//                    break;
//                case R.id.color3:
//                    Log.i(TAG, "Color to ID color3: " + chosenColorId);
//                    break;
//                case R.id.color4:
//                    Log.i(TAG, "Color to ID color4: " + chosenColorId);
//                    break;
//                case R.id.color5:
//                    Log.i(TAG, "Color to ID color5: " + chosenColorId);
//                    break;
//                case R.id.color6:
//                    Log.i(TAG, "Color to ID color6: " + chosenColorId);
//                    break;
//                case R.id.color7:
//                    Log.i(TAG, "Color to ID color7: " + chosenColorId);
//                    break;
//                case R.id.color8:
//                    Log.i(TAG, "Color to ID color8: " + chosenColorId);
//                    break;
//            }
            return true;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
