package com.holmal.app.holmal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.holmal.app.holmal.utils.FragmentHandling;
import com.holmal.app.holmal.utils.FireBaseHandling;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoveInHousehold extends AppCompatActivity implements PersonalInput.OnFragmentInteractionListener {

    //to check validation
    Boolean validationSuccessful =true;

    Fragment currentFragment;
    FragmentHandling fragmentHandling = new FragmentHandling();

    String userNameString;
    String chosenColorString;

    FireBaseHandling fireBaseHandling = new FireBaseHandling();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_in_household);
        ButterKnife.bind(this);

        fragmentHandling.putFragment(currentFragment,
                PersonalInput.newInstance(),
                getSupportFragmentManager(),
                R.id.fragmentContainerMoveIn);
    }

    @OnClick(R.id.moveInDone)
    public void moveInDoneClick() {
        //if every input is valid go to next window
        if (validate()) {
            Intent intent = new Intent(this, ShoppingList.class); // decide if main (screen 11) or screen 5 (shoppingList), then change here
            startActivity(intent);

            // TODO check first if not taken yet in the household
            fireBaseHandling.storePersonOnDatabase(userNameString, chosenColorString);

        }
    }

    /*
    Method that checks whether the input is valid/there before proceeding to the next screen.
     */
    public Boolean validate(){
        //TODO validate Button 5
        EditText userName = (EditText)findViewById(R.id.userNameInput);
        userNameString = userName.getText().toString();
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

        RadioGroup colourChooser = findViewById(R.id.colorChoice);
        //check if a button was chosen
        if (colourChooser.getCheckedRadioButtonId()!= -1) {
            // TODO just default color, change to chosen
            chosenColorString = "lila";
            // int id = colourChooser.getCheckedRadioButtonId();
            // chosenColorString = ...;
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
