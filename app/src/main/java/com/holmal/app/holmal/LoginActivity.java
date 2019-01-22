package com.holmal.app.holmal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener{

    private static final String TAG = LoginActivity.class.getName();

    FirebaseAuth fireAuth;
    FirebaseDatabase database;

    @BindView(R.id.emailInput)
    EditText emailInput;

    @BindView(R.id.passwortInput)
    EditText passwordInput;

    @BindView(R.id.passwortInputWdh)
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
    private HashMap<String, Household> haushalte= new HashMap<>();
    private ArrayList<Person> personen = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        fireAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        passwortInputWdh.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        FirebaseDatabase.getInstance().getReference().child("person").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, "person listener in onCreate: LoginActivity");
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
                Log.i(TAG, "household listener in onCreate: LoginActivity");
                haushalte.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.i(TAG, "alle Haushalte durchgehen");
                    String id = child.getKey();
                    Household value = child.getValue(Household.class);
                    Log.i(TAG, "Haushalt: " + value.getHouseholdName());
                    haushalte.put(id, value);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        Log.i(TAG, "called onAuthStateChange");
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null) {
            Log.i(TAG, "Logged in as: " + user.getEmail());
            Toast.makeText(getApplicationContext(), "Logged in as: " + user.getEmail(), Toast.LENGTH_SHORT).show();

            String householdID = preferences.readPreferences(this, getString(R.string.householdIDPreference));

            if(householdID != null){
           //     String haushaltname = haushalte.get(householdID).getHouseholdName();
           //     Toast.makeText(getApplicationContext(), "Navigiere zu Haushalt: " + haushaltname, Toast.LENGTH_SHORT).show();
                Log.i(TAG, "already registered in an household");

                Intent intent;
                if(preferences.readPreferences(this, getString(R.string.recentShoppingListNamePreference)) != null) {
                    intent = new Intent(this, ShoppingListActivity.class);
                }else{
                    intent = new Intent(this, AllShoppingListsActivity.class);
                }
                startActivity(intent);
            }else{
                Intent intent = new Intent(this, StartActivity.class);
                startActivity(intent);
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "Not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onResume() {
        super.onResume();
        fireAuth.addAuthStateListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        fireAuth.removeAuthStateListener(this);
    }

    @OnClick(R.id.loginButton)
    public void login(){
        final String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        if(validate()){
            progressBar.setVisibility(View.VISIBLE);
            String householdID = checkIfPersonIsInHousehold(email);


            fireAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Log.i(LoginActivity.class.getName(), "Login successful");
                        finish();}
                    else {
                        progressBar.setVisibility(View.INVISIBLE);
                        errorMessage2.setText(R.string.ErrorLoginFailed);
                        Log.e(LoginActivity.class.getName(), "Login failed");
                    }
                }
            });
        }
    }

    @OnClick(R.id.registrationButton)
    public void register(){
        errorMessage2.setText("");
        if(isRegistration){
            if(validate()){
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
                progressBar.setVisibility(View.VISIBLE);

                fireAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.i(LoginActivity.class.getName(), "Registration successful");
                        }
                        else{
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                errorMessage1.setText(R.string.ErrorLoginAlreadyExists);
                            }
                            Log.e(LoginActivity.class.getName(), "Registration failed: "+  task.getException().getMessage());
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        }else{
            passwortInputWdh.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.INVISIBLE);
            this.isRegistration = true;
        }
    }

    //for later maybe a field in menu?
    public void logout(FirebaseAuth myAuth){
        myAuth.signOut();
        Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_SHORT).show();
    }

    /**
     *
     * @return firebaseAuthentication instance
     */
    public FirebaseAuth getFireAuth() {
        return fireAuth;
    }

    public Boolean validate(){
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String passwortWdh = passwortInputWdh.getText().toString();
        if(email.isEmpty() && password.isEmpty()){
            errorMessage3.setText(R.string.ErrorLoginNotEntered);
            return false;
        }else if(password.length()<6 ){
            errorMessage2.setText(R.string.ErrorLoginShortPW);
            return false;
        }else if(isRegistration && passwortWdh.isEmpty()){
            errorMessage3.setText(R.string.ErrorLoginRepeatPW);
            return false;
        }else if(isRegistration && !(passwortWdh.equals(password))){
            errorMessage3.setText(R.string.ErrorLoginCheckPW);
            return false;
        }else{
            return true;
        }
    }

    /**
     *
     * @param email
     * checks if there is an householdId for a person with the given email exists
     * navigates to it
     */
    public String checkIfPersonIsInHousehold(String email){
        Log.i(TAG, "Searched email: " +  email);
        String result = null;
        preferences.storePreferences(this, getString(R.string.householdIDPreference), null);
        preferences.storePreferences(this, getString(R.string.recentShoppingListNamePreference), null);
        for(Person entry : personen){
            Log.i(TAG, "persons email: " + entry.getEmail());
            if(entry.getEmail().equals(email)){
                result = entry.getIdBelongingTo();
                Log.i(TAG, "Person found! Belongs to Household: " + result + "; Storing in preferences");
                preferences.storePreferences(this, getString(R.string.householdIDPreference), result);
                break;
            }
        }
      return result;
    }
}
