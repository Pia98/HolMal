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
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener{


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        fireAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        passwortInputWdh.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null) {
            Toast.makeText(getApplicationContext(), "Logged in as: " + user.getEmail(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, StartActivity.class);
            startActivity(intent);
        }
        else Toast.makeText(getApplicationContext(), "Not logged in", Toast.LENGTH_SHORT).show();
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
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        if(validate()){
            fireAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Log.i(MainActivity.class.getName(), "Login successful");
                        finish();}
                    else {
                        errorMessage2.setText("Login fehlgeschlagen: Bitte überprüfe deine Email und dein Passwort");
                        Log.e(MainActivity.class.getName(), "Login failed");
                    }
                }
            });
        }
    }

    @OnClick(R.id.registrationButton)
    public void register(){
        if(isRegistration){
            if(validate()){
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
                progressBar.setVisibility(View.VISIBLE);

                fireAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.i(MainActivity.class.getName(), "Registration successful");
                        }
                        else{
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                errorMessage1.setText("User with this email already exist.");
                                Toast.makeText(getApplicationContext(), "User with this email already exist.", Toast.LENGTH_SHORT).show();
                            }else if(task.getException() instanceof FirebaseAuthWeakPasswordException){
                                errorMessage2.setText("Das Passwort muss min. 6 Zeichen lang sein.");
                                Toast.makeText(getApplicationContext(), "Das Passwort muss min. 6 Zeichen lang sein.", Toast.LENGTH_SHORT).show();
                            }
                            Log.e(MainActivity.class.getName(), "Registration failed: "+  task.getException().getMessage());
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
            Toast.makeText(getApplicationContext(), "Bitte gib deine Emailadresse und dein Passwort an", Toast.LENGTH_SHORT).show();
            return false;
        }else if(password.length()<6 ){
            Toast.makeText(getApplicationContext(), "Das Passwort muss min. 6 Zeichen lang sein.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }else if(isRegistration && passwortWdh.isEmpty()){
            Toast.makeText(getApplicationContext(), "Bitte wiederhole dein Passwort", Toast.LENGTH_SHORT).show();
            return false;
        }else if(isRegistration && !(passwortWdh.equals(password))){
            Toast.makeText(getApplicationContext(), "Bitte überprüfe dein Passwort: ",
                    Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }
}
