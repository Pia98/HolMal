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

import com.google.firebase.auth.FirebaseAuth;
import com.holmal.app.holmal.model.Household;
import com.holmal.app.holmal.model.Person;
import com.holmal.app.holmal.model.ShoppingList;
import com.holmal.app.holmal.utils.FireBaseHandling;
import com.holmal.app.holmal.utils.FragmentHandling;
import com.holmal.app.holmal.utils.PreferencesAccess;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateHouseholdActivity extends AppCompatActivity implements PersonalInputFragment.OnFragmentInteractionListener {

    private static final String TAG = CreateHouseholdActivity.class.getName();

    Fragment currentFragment;

    String userNameString;
    String houseHoldNameString;
    int chosenColorId;
    String householdId;

    FragmentHandling fragmentHandling = new FragmentHandling();

    PreferencesAccess preferences = new PreferencesAccess();

    FirebaseAuth fireAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_household);
        ButterKnife.bind(this);

        fragmentHandling.putFragment(currentFragment,
                PersonalInputFragment.newInstance(),
                getSupportFragmentManager(),
                R.id.fragmentContainerCreateHousehold);

        fireAuth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.createHouseholdDone)
    public void createHouseholdDoneClick() {
        if (validate()) {
            Household household = new Household(houseHoldNameString);
            householdId = FireBaseHandling.getInstance().storeNewHousehold(household);

            String currentEmail = fireAuth.getCurrentUser().getEmail();
            Person newPerson = new Person(userNameString, chosenColorId, householdId, currentEmail);
            String personId = FireBaseHandling.getInstance().storePerson(newPerson);

            // create default ShoppingListActivity when household created
            String category = null;
            ShoppingList defaultList = new ShoppingList(getString(R.string.defaultShoppingList), category, householdId);
            FireBaseHandling.getInstance().storeShoppingList(defaultList);

            // HaushaltID in preferences speichern
            preferences.storePreferences(this, getString(R.string.householdIDPreference), householdId);
            preferences.storePreferences(this, getString(R.string.recentShoppingListNamePreference), defaultList.getListName());
            preferences.storePreferences(this, getString(R.string.personIDPreference), personId);

            Log.i(TAG, "householdID: " + householdId);


            Log.i(TAG, "store person: name - " + userNameString + ", color - " + chosenColorId + ", email - " + currentEmail);

            Intent intent = new Intent(this, RegistrationActivity.class);
            // RegistrationFragment1 has to be drawn
            intent.putExtra("fragmentNumber", 1);
            intent.putExtra("userName", userNameString);
            intent.putExtra("householdName", houseHoldNameString);
            intent.putExtra("householdId", householdId);
            startActivity(intent);
        }
    }

    private boolean validate() {
        EditText userName = (EditText) findViewById(R.id.userNameInput);
        EditText householdName = (EditText) findViewById(R.id.householdNameInput);
        userNameString = userName.getText().toString();
        houseHoldNameString = householdName.getText().toString();
        if (houseHoldNameString.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.ErrorEnterHouseholdName, Toast.LENGTH_LONG).show();
            return false;
        }
        if (userNameString.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.ErrorEnterName, Toast.LENGTH_LONG).show();
            return false;
        } else return checkColours();

    }

    // Method that checks if a colour button has been chosen. Users must choose a colour before entering a household.
    private boolean checkColours() {

        RadioGroup colourChooser = findViewById(R.id.colorChoice);
        //check if a button was chosen
        if (colourChooser.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getApplicationContext(), R.string.ErrorChoseColor, Toast.LENGTH_LONG).show();
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
