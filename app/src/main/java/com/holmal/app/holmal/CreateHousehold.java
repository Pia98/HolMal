package com.holmal.app.holmal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.holmal.app.holmal.model.Household;
import com.holmal.app.holmal.model.TestHoushold;
import com.holmal.app.holmal.utils.FireBaseHandling;
import com.holmal.app.holmal.utils.FragmentHandling;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateHousehold extends AppCompatActivity implements PersonalInput.OnFragmentInteractionListener {

    Fragment currentFragment;
    FragmentHandling fragmentHandling = new FragmentHandling();
    FireBaseHandling fireHandling = new FireBaseHandling();
    String userNameString;
    String houseHoldNameString;

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
            TestHoushold household = new TestHoushold(houseHoldNameString);
            DatabaseReference myRef =FirebaseDatabase.getInstance().getReference();
            myRef.child("haushalt").setValue(household);


            Intent intent = new Intent(this, RegistrationActivity.class);
            // RegistrationFragment1 has to be drawn
            intent.putExtra("fragmentNumber", 1);
            intent.putExtra("userName", userNameString);
            intent.putExtra("householdName", houseHoldNameString);
            intent.putExtra("householdName", houseHoldNameString);
            startActivity(intent);
        }
    }

    //checks to see if a household name was chosen, a name was chosen that is unique and if a colour was chosen
    public Boolean validate(){
        EditText userName = (EditText) findViewById(R.id.userNameInput);
        EditText householdName = (EditText) findViewById(R.id.householdNameInput);
        userNameString = userName.getText().toString();
        houseHoldNameString = householdName.getText().toString();
        if (houseHoldNameString.isEmpty()){
            Toast.makeText(this, R.string.ErrorEnterHouseholdName, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (userNameString.isEmpty()){
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

        RadioGroup colourChooser = findViewById(R.id.colorChoice);
        //check if a button was chosen
        if (colourChooser.getCheckedRadioButtonId()== -1) {
            Toast.makeText(this, R.string.ErrorChoseColor, Toast.LENGTH_SHORT).show();
            return false;
            }
        else {
            return true;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
