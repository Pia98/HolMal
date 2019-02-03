package com.holmal.app.holmal;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
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
import com.holmal.app.holmal.utils.FireBaseHandling;
import com.holmal.app.holmal.utils.PreferencesAccess;

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

    @BindView(R.id.editEmailText)
    EditText editEmailText;

    @BindView(R.id.editNeuesPasswortText)
    EditText editNeuesPasswort;

    @BindView(R.id.editNeuesWdhPasswortText)
    EditText editNeuesPasswortWdh;

    FirebaseAuth fireAuth;
    FirebaseUser user;
    String password;

    PreferencesAccess preferencesAccess = new PreferencesAccess();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        ButterKnife.bind(this);

        editAccount.setVisibility(View.GONE);

        fireAuth = FirebaseAuth.getInstance();
        user = fireAuth.getCurrentUser();
    }

    /**
     * functionality of the further button:
     *   - tries to log the user in
     *   - then sets the editAccountSection on visible
     *   - sets itself and the editOldPasswortSeciton in gone
     */
    @OnClick(R.id.passwortFurther)
    public void further(){
        password = oldPasswordText.getText().toString();

        if(!password.isEmpty()){
            fireAuth.signInWithEmailAndPassword(user.getEmail(), password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "Login successful");
                        editOldPassword.setVisibility(View.GONE);
                        editAccount.setVisibility(View.VISIBLE);
                        further.setVisibility(View.GONE);
                        editEmailText.setText(user.getEmail());
                    } else {
                        Log.e(TAG, "Login failed");
                        Toast.makeText(getApplicationContext(), getString(R.string.ErrorLoginCheckPW), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else Toast.makeText(getApplicationContext(), getString(R.string.ErrorLoginCheckPW), Toast.LENGTH_SHORT).show();
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
        final String newEmail = editEmailText.getText().toString();
        boolean checked = isEmailValid(newEmail);
        final String personID = preferencesAccess.readPreferences(this, getString(R.string.personIDPreference));

        if(checked){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String message = getString(R.string.editEmailConfirmation) + newEmail;
            builder.setMessage(message);
            builder.setCancelable(true);
            builder.setPositiveButton(
                    R.string.yes,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            fireAuth.getCurrentUser().updateEmail(newEmail);
                            FireBaseHandling.getInstance().editPersonEmail(newEmail, personID);

                            Intent intent = new Intent(EditAccountActivity.this, SettingsActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
            builder.setNegativeButton(
                    R.string.no,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    /**
     * functionality of the password complete button
     *   - checks if it really is a new password
     *   - checks if new password is valid
     *   - saves new password in authentication
     */
    @OnClick(R.id.passwortComplete)
    public void editPasswort(){
        final String newPassword = editNeuesPasswort.getText().toString();
        final String newPasswordWdh = editNeuesPasswortWdh.getText().toString();

        boolean checked = isPasswordValid(newPassword, newPasswordWdh);

        if(checked){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String message = getString(R.string.editPasswordConfirmation);
            builder.setMessage(message);
            builder.setCancelable(true);
            builder.setPositiveButton(
                    R.string.yes,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            fireAuth.getCurrentUser().updatePassword(newPassword);

                            Intent intent = new Intent(EditAccountActivity.this, SettingsActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
            builder.setNegativeButton(
                    R.string.no,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();
        }

    }

    /**
     * checks if the inserted email is valid
     * @param email
     * @return
     */
    private boolean isEmailValid(String email){
        if(email.isEmpty()){
            Toast.makeText(getApplicationContext(), getString(R.string.ErrorNoEmail), Toast.LENGTH_SHORT).show();
            return false;
        }else if(email.equals(user.getEmail())){
            Toast.makeText(getApplicationContext(), getString(R.string.ErrorNewEmail), Toast.LENGTH_SHORT).show();
            return false;
        }else return true;
    }

    /**
     * checks if the new password is valid
     * @param password
     * @param passwordWdh
     * @return
     */
    private boolean isPasswordValid(String password, String passwordWdh){
        if (password.isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.ErrorNoPassword), Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), getString(R.string.ErrorLoginShortPW), Toast.LENGTH_SHORT).show();
            return false;
        } else if (passwordWdh.isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.ErrorLoginRepeatPW), Toast.LENGTH_SHORT).show();
            return false;
        } else if (!(passwordWdh.equals(password))) {
            Toast.makeText(getApplicationContext(), getString(R.string.ErrorLoginCheckPW), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}
