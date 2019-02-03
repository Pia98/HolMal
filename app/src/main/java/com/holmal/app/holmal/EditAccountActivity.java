package com.holmal.app.holmal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditAccountActivity extends AppCompatActivity {

    private static final String TAG = EditAccountActivity.class.getName();

    //-------------- view stuff -------------
    @BindView(R.id.editOldPasswordSection)
    ConstraintLayout editOldPassword;

    @BindView(R.id.editAccountSection)
    ConstraintLayout editAccount;

    @BindView(R.id.passwortFurther)
    Button further;

    //--------------- edit stuff --------------
    @BindView(R.id.oldPasswordText)
    EditText oldPasswordText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        ButterKnife.bind(this);

        editAccount.setVisibility(View.GONE);
    }

    /**
     * functionality of the further button:
     *   - tries to log the user in
     *   - then sets the editAccountSection on visible
     *   - sets itself and the editOldPasswortSeciton in gone
     */
    @OnClick(R.id.passwortFurther)
    public void further(){
        String password = oldPasswordText.getText().toString();

        FirebaseAuth fireAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fireAuth.getCurrentUser();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(user.getEmail(), password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, "Login successful");
                    editOldPassword.setVisibility(View.GONE);
                    editAccount.setVisibility(View.VISIBLE);
                    further.setVisibility(View.GONE);
                } else {
                    Log.e(TAG, "Login failed");
                    Toast.makeText(getApplicationContext(), getString(R.string.ErrorLoginPasswordWrong), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * closes the activity
     */
    @OnClick(R.id.accountEditClose)
    public void close(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * functionality of the email complete button
     *   - checks if the new email is already taken
     *   - checks if email is valid
     *   - saves the new email in authentication
     *   - saves the new email on the database
     */
    @OnClick(R.id.emailComplete)
    public void editEmail(){

    }

    /**
     * functionality of the password complete button
     *   - checks if it really is a new password
     *   - checks if new password is valid
     *   - saves new password in authentication
     */
    @OnClick(R.id.passwortComplete)
    public void editPasswort(){

    }
}
