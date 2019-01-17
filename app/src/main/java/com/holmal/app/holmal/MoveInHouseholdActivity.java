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
import com.holmal.app.holmal.utils.ReferencesHandling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoveInHouseholdActivity extends AppCompatActivity implements PersonalInputFragment.OnFragmentInteractionListener {

    private static final String TAG = MoveInHouseholdActivity.class.getName();

    Fragment currentFragment;
    FragmentHandling fragmentHandling = new FragmentHandling();
    ReferencesHandling referencesHandling = new ReferencesHandling();

    @BindView(R.id.householdIDAsText)
    TextView householdIdAsText;

    String householdId;

    String userNameString;
    int chosenColorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_in_household);
        ButterKnife.bind(this);

        fragmentHandling.putFragment(currentFragment,
                PersonalInputFragment.newInstance(),
                getSupportFragmentManager(),
                R.id.fragmentContainerMoveIn);

        Bundle extras = getIntent().getExtras();
        householdId = extras.getString("inputId");

        // listener fuer personen starten, gleich bei erzeugen, bevor Person gespeichert, wegen Abfragen ob bereits in Haushalt vorhanden
        FireBaseHandling.getInstance().startPersonIDValueEventListener(householdId);

        householdIdAsText.setText(householdId);

    }

    /**
     * When 'done' button is clicekd and all input is valid,
     * create a person and store this on database in the household,
     */
    @OnClick(R.id.moveInDone)
    public void moveInDoneClick() {
        if (validate()) {
            PreferencesAccess preferences = new PreferencesAccess();

            Person person = new Person(userNameString, chosenColorId, householdId);

            // TODO check first if not taken yet in the household
            Log.i(TAG, String.format("'%s' (color: %s) wants to move in '%s'", userNameString, chosenColorId, householdId));
            String personId = FireBaseHandling.getInstance().storeMoveInPersonInHousehold(householdId, person);
            // HaushaltID in preferences speichern
            preferences.storePreferences(this, getString(R.string.householdIDPreference), householdId);
            preferences.storePreferences(this, getString(R.string.personIDPreference), personId);

            // activity erst starten wenn Speicherung vorgenommen wurde
            Intent intent = new Intent(this, AllShoppingListsActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Check if all input fields are valid
     *
     * @return if all inputs are valid
     */
    private boolean validate() {
        List<String> personIDList = FireBaseHandling.getInstance().getPersonIDListener().getPersonList();
        HashMap<String, Person> personHash = FireBaseHandling.getInstance().getPersonListener().getPersonHash();
        //ArrayList<Person> personenInCurrentHousehold = referencesHandling.getAllMembersOfOneHousehold(personIDList, personHash);
        ArrayList<Person> personenInCurrentHousehold = new ArrayList<>();
        String[] keys = personHash.keySet().toArray(new String[personHash.size()]);
        Log.i(TAG, "personenInCurrentHousehold: " + personenInCurrentHousehold);

        for(int i = 0; i < personHash.size(); i++){
            Log.i(TAG, "alle Personen durchgehen...");
            Person person = personHash.get(keys[i]);
            Log.i(TAG, "person: " + person);
            // TODO das aeussere if statement raus schmeissen sobald alle Personen mit idBelongingTo gespeichert werden
            if(person.getIdBelongingTo() != null){
                if(person.getIdBelongingTo().equals(householdId)){
                    Log.i(TAG, "idBelongingTo = householdId");
                    personenInCurrentHousehold.add(person);
                    Log.i(TAG, "personenInCurrentHousehold add: " + personenInCurrentHousehold);
                }
            }
        }

        Log.i(TAG, "personenInCurrentHousehold wird dann so uebergeben: " + personenInCurrentHousehold);


        if (checkUserName(personenInCurrentHousehold)) {
            return checkColours(personenInCurrentHousehold);
        } else return false;
    }

    /**
     * Check if userName input is valid
     * @param personList List of all members
     * @return if input is valid
     */
    private boolean checkUserName(ArrayList<Person> personList) {
        //TODO validate Button 5
        EditText userName = (EditText) findViewById(R.id.userNameInput);
        userNameString = userName.getText().toString();
        if (!userNameString.isEmpty()) {
            return checkUserNameTaken(personList);
        } else {
            Toast.makeText(getApplicationContext(), R.string.ErrorEnterName, Toast.LENGTH_LONG).show();
            return false;
        }
    }

    /**
     * TODO: check if id is in outher list
     * Check if there is a household member with the same userName
     *
     * @param personList List of all household members
     * @return if the userName is already taken
     */
    private boolean checkUserNameTaken(ArrayList<Person> personList) {
        for (Person person : personList) {
            if (userNameString.equals(person.getPersonName())) {
                Toast.makeText(getApplicationContext(), R.string.ErrorNameTaken, Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    /**
     * Check if the color input is valid
     *
     * @param personList List of all household members
     * @return if input is valid
     */
    private boolean checkColours(List<Person> personList) {
        RadioGroup colourChooser = findViewById(R.id.colorChoice);
        //check if a button was chosen
        if (colourChooser.getCheckedRadioButtonId() != -1) {
            chosenColorId = colourChooser.getCheckedRadioButtonId();
            return checkColourTaken(personList);
        } else {
            Toast.makeText(getApplicationContext(), R.string.ErrorChoseColor, Toast.LENGTH_LONG).show();
            return false;
        }
    }

    /**
     * Check if there is a household member with the same color
     *
     * @param personList List of all household members
     * @return if the color is already taken
     */
    private boolean checkColourTaken(List<Person> personList) {
        for (Person person : personList) {
            if (chosenColorId == person.getColor()) {
                Toast.makeText(getApplicationContext(), R.string.ErrorColorTaken, Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
