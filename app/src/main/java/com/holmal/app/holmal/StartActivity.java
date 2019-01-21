package com.holmal.app.holmal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.holmal.app.holmal.model.Household;
import com.holmal.app.holmal.model.Person;
import com.holmal.app.holmal.utils.PreferencesAccess;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class  StartActivity extends AppCompatActivity {
    private static final String TAG = StartActivity.class.getName();

    private ArrayList<Person> personen = new ArrayList<>();
    private HashMap<String, Household> haushalte= new HashMap<>();
    PreferencesAccess preferences = new PreferencesAccess();

    FirebaseAuth fireAuth;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);

        Log.i(TAG,"opened");

        fireAuth = FirebaseAuth.getInstance();

        FirebaseDatabase.getInstance().getReference().child("person").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, "person listener in onCreate: StartActivity");
                personen.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.i(TAG, "alle Personen durchgehen");
                    Person value = child.getValue(Person.class);
                    Log.i(TAG, "Person: " + value);
                    personen.add(value);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("household").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, "household listener in onCreate: StartActivity");
                haushalte.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.i(TAG, "alle Haushalte durchgehen");
                    String id = child.getKey();
                    Household value = child.getValue(Household.class);
                    Log.i(TAG, "Haushalt: " + value);
                    haushalte.put(id, value);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        email = fireAuth.getCurrentUser().getEmail();
        String householdID = navigateToHousehold(email);
    }

    @OnClick(R.id.createHousehold)
    public void createHouseholdButtonClick() {
        Log.i(TAG, "CreateHousehold button clicked");
        Intent intent = new Intent(this, CreateHouseholdActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.moveIn)
    public void moveInButtonClick() {
        Log.i(TAG, "MoveIn button clicked");
        Intent intent = new Intent(this, RegistrationActivity.class);
        // RegistrationFragment2 has to be drawn
        intent.putExtra("fragmentNumber", 2);
        startActivity(intent);
    }

    @OnClick({R.id.logoutButton})
    public void logoutStart(){
        Log.i(TAG, "Logout button clicked");
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(StartActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     *
     * @param email
     * @return the householdId if a person with the given email exists
     */
    public String navigateToHousehold(String email){
        Log.i(TAG, "Searched email: " +  email);
        String result = null;
        preferences.storePreferences(this, getString(R.string.householdIDPreference), null);
        for(Person entry : personen){
            Log.i(TAG, "persons email: " + entry.getEmail());
            if(entry.getEmail().equals(email)){
                result = entry.getIdBelongingTo();
                Log.i(TAG, "Person found! Belongs to Household: " + result + "; Storing in preferences");
                preferences.storePreferences(this, getString(R.string.householdIDPreference), result);

                String haushaltname = haushalte.get(result).getHouseholdName();
                Toast.makeText(getApplicationContext(), "Navigiere zu Haushalt: " + haushaltname, Toast.LENGTH_SHORT).show();
                Log.i(TAG, "already registered in an household");
                Intent intent = new Intent(this, ShoppingListActivity.class);
                startActivity(intent);
                break;
            }
        }
        return result;
    }
}
