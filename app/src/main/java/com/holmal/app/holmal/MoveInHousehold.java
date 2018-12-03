package com.holmal.app.holmal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoveInHousehold extends AppCompatActivity {

    //to check validation
    Boolean validationSuccessful =true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_in_household);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.moveInDone)
    public void moveInDoneClick() {
        //if every input is valid go to next window
        validate();
        if (validationSuccessful == true) {
            Intent intent = new Intent(this, MainActivity.class); // decide if main (screen 11) or screen 5, then change here
            startActivity(intent);
        }
    }

    /*
    Method that checks whether the input is valid/there before proceeding to the next screen.
     */
    public void validate(){
        //start validation with assumption that validation will be successful. If it isn't this will change.
        validationSuccessful=true;

        //TODO validate Button 5
        EditText userName = (EditText)findViewById(R.id.userNameInput);
        if (userName.getText().toString().isEmpty()){
            Toast.makeText(this, R.string.ErrorEnterName, Toast.LENGTH_SHORT).show();
            validationSuccessful = false;
        }

        //TODO check if name is not taken
        /*
        if (userName.getText.toString() is in List of Names in Household)
         Toast.makeText(this, ErrorNameTaken, Toast.LENGTH_SHORT).show();
         validationSuccessful = false;
         */
        checkColours();


    }

    /*
    Method that checks if a colour button has been chosen. Users must choose a colour before entering a household.
     */
    public void checkColours(){

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
        }
        else {
            Toast.makeText(this, R.string.ErrorChoseColor, Toast.LENGTH_SHORT).show();
            validationSuccessful=false;
        }
    }
}
