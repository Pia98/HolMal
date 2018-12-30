package com.holmal.app.holmal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.holmal.app.holmal.model.Person;
import com.holmal.app.holmal.utils.FireBaseHandling;
import com.holmal.app.holmal.utils.FragmentHandling;
import com.holmal.app.holmal.utils.PreferencesAccess;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoveInHousehold extends AppCompatActivity implements PersonalInput.OnFragmentInteractionListener {

    private static final String TAG = MoveInHousehold.class.getName();


    //to check validation
    Boolean validationSuccessful = true;

    Fragment currentFragment;
    FragmentHandling fragmentHandling = new FragmentHandling();

    @BindView(R.id.householdIDAsText)
    TextView householdIdAsText;

    String householdId;

    String userNameString;
    int chosenColorId;

    FireBaseHandling fireBaseHandling = new FireBaseHandling();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_in_household);
        ButterKnife.bind(this);

        fragmentHandling.putFragment(currentFragment,
                PersonalInput.newInstance(),
                getSupportFragmentManager(),
                R.id.fragmentContainerMoveIn);

        Bundle extras = getIntent().getExtras();
        householdId = extras.getString("inputId");

        // listener fuer personen starten, gleich bei erzeugen, bevor Person gespeichert, wegen Abfragen ob bereits in Haushalt vorhanden
        fireBaseHandling.startPersonValueEventListener(householdId);

        householdIdAsText.setText(householdId);

    }

    @OnClick(R.id.moveInDone)
    public void moveInDoneClick() {
        if (validate()) {
            PreferencesAccess preferences = new PreferencesAccess();

            Person person = new Person(userNameString, chosenColorId);
            Intent intent = new Intent(this, ShoppingList.class); // decide if main (screen 11) or screen 5 (shoppingList), then change here
            startActivity(intent);

            // TODO check first if not taken yet in the household
            //fireBaseHandling.storePersonOnDatabase(userNameString, chosenColorId);
            Log.i(TAG, userNameString + " (color: " + chosenColorId + ") wants to move in " + householdId);
            fireBaseHandling.storeMoveInPersonInHousehold(householdId, person);
            // HaushaltID in storage speichern
            preferences.storePreferences(this, "householdID", householdId);
        }
    }

    /*
    Method that checks whether the input is valid/there before proceeding to the next screen.
     */
    private boolean validate() {
        List<Person> personList = fireBaseHandling.getPersonListener().getPersonList();
        if (checkUserName(personList)) {
            return checkColours(personList);
        } else return false;
    }

    private boolean checkUserName(List<Person> personList) {
        //TODO validate Button 5
        EditText userName = (EditText) findViewById(R.id.userNameInput);
        userNameString = userName.getText().toString();
        if (!userNameString.isEmpty()) {
            return checkUserNameTaken(personList);
        } else {
            Toast.makeText(this, R.string.ErrorEnterName, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean checkUserNameTaken(List<Person> personList) {
        for (Person person : personList) {
            if (userNameString.equals(person.getPersonName())) {
                Toast.makeText(this, R.string.ErrorNameTaken, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    /*
    Method that checks if a colour button has been chosen. Users must choose a colour before entering a household.
     */
    private boolean checkColours(List<Person> personList) {
        RadioGroup colourChooser = findViewById(R.id.colorChoice);
        //check if a button was chosen
        if (colourChooser.getCheckedRadioButtonId() != -1) {
            chosenColorId = colourChooser.getCheckedRadioButtonId();
            // int id = colourChooser.getCheckedRadioButtonId();
            // chosenColorId = ...;
            return checkColourTaken(personList);
        } else {
            Toast.makeText(this, R.string.ErrorChoseColor, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean checkColourTaken(List<Person> personList) {
        for (Person person : personList) {
            if (chosenColorId == person.getColor()) {
                Toast.makeText(this, R.string.ErrorColorTaken, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
