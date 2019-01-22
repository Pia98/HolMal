package com.holmal.app.holmal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.holmal.app.holmal.model.Household;
import com.holmal.app.holmal.model.Person;
import com.holmal.app.holmal.utils.FireBaseHandling;
import com.holmal.app.holmal.utils.FragmentHandling;
import com.holmal.app.holmal.utils.PreferencesAccess;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoveInHouseholdActivity extends AppCompatActivity implements PersonalInputFragment.OnFragmentInteractionListener {

    private static final String TAG = MoveInHouseholdActivity.class.getName();

    Fragment currentFragment;
    FragmentHandling fragmentHandling = new FragmentHandling();
    FirebaseAuth fireAuth;

    private ArrayList<Person> joiningPerson = new ArrayList<>();

    @BindView(R.id.householdIDAsText)
    TextView householdIdAsText;

    @BindView(R.id.householdNameFromIDAsText)
    TextView householdNameAsText;

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

        FirebaseDatabase.getInstance().getReference().child("person").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, "listener in onCreate...");
                joiningPerson.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.i(TAG, "alle Personen durchgehen");
                    String id = child.getKey();
                    Person value = child.getValue(Person.class);
                    Log.i(TAG, "Person: " + value);
                    if (value.getIdBelongingTo().equals(householdId)) {
                        Log.i(TAG, "Person gehört zu diesem Haushalt.");
                        joiningPerson.add(value);
                    }

                    Log.i(TAG, "joiningPerson in for Schleife bei listener: " + joiningPerson);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("household").child(householdId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Household thisHousehold = dataSnapshot.getValue(Household.class);
                householdNameAsText.setText(thisHousehold.getHouseholdName());
                householdIdAsText.setText(dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fireAuth = FirebaseAuth.getInstance();
    }

    /**
     * When 'done' button is clicekd and all input is valid,
     * create a person and store this on database in the household,
     */
    @OnClick(R.id.moveInDone)
    public void moveInDoneClick() {
        if (validate()) {
            PreferencesAccess preferences = new PreferencesAccess();

            String currentEmail = fireAuth.getCurrentUser().getEmail();
            Person person = new Person(userNameString, chosenColorId, householdId, currentEmail);

            Log.i(TAG, String.format("'%s' (color: %s) wants to move in '%s'", userNameString, chosenColorId, householdId));
            String personId = FireBaseHandling.getInstance().storePerson(householdId, person);
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
        Log.i(TAG, "validate called");
        if (checkUserName(joiningPerson)) {
            return checkColours(joiningPerson);
        } else return false;
    }

    /**
     * Check if userName input is valid
     *
     * @param personList List of all members
     * @return if input is valid
     */
    private boolean checkUserName(ArrayList<Person> personList) {
        Log.i(TAG, "uebergebene Liste (sollte vom Listener befuellt worden sein): " + personList);
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
