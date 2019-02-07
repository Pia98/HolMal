package com.holmal.app.holmal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.holmal.app.holmal.model.Household;
import com.holmal.app.holmal.model.Person;
import com.holmal.app.holmal.utils.PreferencesAccess;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    private static final String TAG = LoginActivity.class.getName();

    FirebaseAuth fireAuth;
    FirebaseDatabase database;

    @BindView(R.id.emailInput)
    EditText emailInput;

    @BindView(R.id.passwortInput)
    EditText passwordInput;

    @BindView(R.id.passwordInputWdh)
    EditText passwortInputWdh;

    @BindView(R.id.loginButton)
    Button loginButton;

    private Boolean isRegistration = false;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.error_message1)
    TextView errorMessage1;

    @BindView(R.id.error_message2)
    TextView errorMessage2;

    @BindView(R.id.error_message3)
    TextView errorMessage3;

    String email;
    PreferencesAccess preferences = new PreferencesAccess();
    private HashMap<String, Household> haushalte = new HashMap<>();
    private HashMap<String, Person> personen = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        fireAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        passwortInputWdh.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        startPersonListener();

        startHouseholdListener();
    }

    /**
     * this method starts an listener on "household" on the database
     */
    private void startHouseholdListener() {
        FirebaseDatabase.getInstance().getReference().child("household").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                haushalte.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String id = child.getKey();
                    Household value = child.getValue(Household.class);
                    Log.i(TAG, "Household: " + value.getHouseholdName());
                    haushalte.put(id, value);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * this method starts an listener on "person" on the database
     */
    private void startPersonListener() {
        FirebaseDatabase.getInstance().getReference().child("person").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                personen.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String id = child.getKey();
                    Person value = child.getValue(Person.class);
                    Log.i(TAG, "Person: " + value);
                    personen.put(id, value);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * This method handles the changing status of an user (logged in or not logged in)
     * @param firebaseAuth
     */
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            Log.i(TAG, "Logged in as: " + user.getEmail());
            Toast.makeText(getApplicationContext(), "Logged in as: " + user.getEmail(), Toast.LENGTH_SHORT).show();

            String householdID = preferences.readPreferences(this, getString(R.string.householdIDPreference));

            if (householdID != null) {

                Intent intent;
                /*if (preferences.readPreferences(this, getString(R.string.recentShoppingListNamePreference)) != null) {
                    intent = new Intent(this, ShoppingListActivity.class);
                } else {*/
                    intent = new Intent(this, AllShoppingListsActivity.class);
                //}
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(this, StartActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * starts authentication listener
     */
    protected void onResume() {
        super.onResume();
        fireAuth.addAuthStateListener(this);
    }

    /**
     * removes authentication listener
     */
    @Override
    protected void onPause() {
        super.onPause();
        fireAuth.removeAuthStateListener(this);
    }

    /**
     * OnClickListener for the "login" button
     */
    @OnClick(R.id.loginButton)
    public void login() {
        final String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        if (validate()) {
            progressBar.setVisibility(View.VISIBLE);
            String householdID = checkIfPersonIsInHousehold(email);


            fireAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "Login successful");
                        finish();
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);
                        errorMessage2.setText(R.string.ErrorLoginFailed);
                        Log.e(TAG, "Login failed");
                    }
                }
            });
        }
    }

    /**
     * OnClickListener for the "registration" button
     */
    @OnClick(R.id.registrationButton)
    public void register() {
        errorMessage2.setText("");
        if (isRegistration) {
            if (validate()) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
                progressBar.setVisibility(View.VISIBLE);

                fireAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "Registration successful");
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                errorMessage1.setText(R.string.ErrorLoginAlreadyExists);
                            }
                            Log.e(TAG, "Registration failed: " + task.getException().getMessage());
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        } else {
            passwortInputWdh.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.INVISIBLE);
            this.isRegistration = true;
        }
    }

    /**
     * Handles the Logout of an user
     * @param myAuth
     */
    public void logout(FirebaseAuth myAuth) {
        myAuth.signOut();
        Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_SHORT).show();
    }

    /**
     * validation of the editTexts for the 'login' and 'registration' button
     * @return
     */
    private boolean validate() {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String passwortWdh = passwortInputWdh.getText().toString();
        if (email.isEmpty() && password.isEmpty()) {
            errorMessage3.setText(R.string.ErrorLoginNotEntered);
            return false;
        } else if (password.length() < 6) {
            errorMessage2.setText(R.string.ErrorLoginShortPW);
            return false;
        } else if (isRegistration && passwortWdh.isEmpty()) {
            errorMessage3.setText(R.string.ErrorLoginRepeatPW);
            return false;
        } else if (isRegistration && !(passwortWdh.equals(password))) {
            errorMessage3.setText(R.string.ErrorLoginCheckPW);
            return false;
        } else {
            return true;
        }
    }

    /**
     * @param email checks if there is an householdId for a person with the given email exists
     *              navigates to it
     */
    private String checkIfPersonIsInHousehold(String email) {
        Log.i(TAG, "Searched email: " + email);
        String result = null;
        preferences.storePreferences(this, getString(R.string.householdIDPreference), null);
        preferences.storePreferences(this, getString(R.string.recentShoppingListNamePreference), null);
        preferences.storePreferences(this, getString(R.string.personIDPreference), null);

        Iterator it = personen.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Person person = (Person) entry.getValue();
            if (person.getEmail().equals(email)) {
                result = person.getIdBelongingTo();
                preferences.storePreferences(this, getString(R.string.householdIDPreference), result);
                preferences.storePreferences(this, getString(R.string.personIDPreference), (String) entry.getKey());
                break;
            }
        }
        return result;
    }
}
