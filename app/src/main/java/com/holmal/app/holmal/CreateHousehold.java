package com.holmal.app.holmal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateHousehold extends AppCompatActivity implements PersonalInput.OnFragmentInteractionListener {

    Fragment currentFragment;
    FragmentHandling fragmentHandling = new FragmentHandling();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_household);
        ButterKnife.bind(this);

        fragmentHandling.putFragment(currentFragment,
                PersonalInput.newInstance(),
                getSupportFragmentManager(),
                R.id.fragmentContainerCreateHousehold);

    }

    @OnClick(R.id.createHouseholdDone)
    public void createHouseholdDoneClick() {
        if (validate()) {
            Intent intent = new Intent(this, RegistrationActivity.class);
            // RegistrationFragment1 has to be drawn
            intent.putExtra("fragmentNumber", 1);
            startActivity(intent);
        }
    }

    //checks to see if a household name was chosen, a name was chosen that is unique and if a colour was chosen
    public Boolean validate(){
        EditText householdName = (EditText) findViewById(R.id.householdNameInput);
        EditText userName = (EditText)findViewById(R.id.userNameInput);
        if (householdName.getText().toString().isEmpty()){
            Toast.makeText(this, R.string.ErrorEnterHouseholdName, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (userName.getText().toString().isEmpty()){
            Toast.makeText(this, R.string.ErrorEnterName, Toast.LENGTH_SHORT).show();
            return false;
        }
        //TODO check if name is not taken
        /*
        if (userName.getText.toString() is in List of Names in Household)
         Toast.makeText(this, ErrorNameTaken, Toast.LENGTH_SHORT).show();
         return false;
         */
        else return checkColours();

    }


    /*
    Method that checks if a colour button has been chosen. Users must choose a colour before entering a household.
     */
    public Boolean checkColours(){

        ToggleButton colour1 = findViewById(R.id.color1);
        ToggleButton colour2 = findViewById(R.id.color2);
        ToggleButton colour3 = findViewById(R.id.color3);
        ToggleButton colour4 = findViewById(R.id.color4);
        ToggleButton colour5 = findViewById(R.id.color5);
        ToggleButton colour6 = findViewById(R.id.color6);
        ToggleButton colour7 = findViewById(R.id.color7);
        ToggleButton colour8 = findViewById(R.id.color8);

        //check if a button was chosen
        if (colour1.isActivated() || colour2.isActivated() || colour3.isActivated() || colour4.isActivated()
                || colour5.isActivated() || colour6.isActivated() || colour7.isActivated() || colour8.isActivated()){
            return true;
        }
        else {
            Toast.makeText(this, R.string.ErrorChoseColor, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
