package com.holmal.app.holmal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
        validate();
        if (validationSuccessful == true) {
            Intent intent = new Intent(this, MainActivity.class); // decide if main (screen 11) or screen 5, then change here
            startActivity(intent);
        }
    }

    public void validate(){
        //TODO validate Button 5
        EditText userName = (EditText)findViewById(R.id.userNameInput);
        if (userName.getText().toString() == ""){
            //TODO text in variable
            Toast.makeText(this, "Gib einen Namen ein", Toast.LENGTH_SHORT).show();
            validationSuccessful = false;
        }
        else {validationSuccessful =true;}//TODO ans Ende wenn die anderen geschrieben sind
        //TODO check if name is not taken
        /*
        if (userName.getText.toString() is in List of Names in Household)
         Toast.makeText(this, "Dieser Name ist schon vergeben", Toast.LENGTH_SHORT).show();
         validationSuccessful = false;
         */
        //TODO check if a button has been chosen
        checkColours();

    }

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
            Toast.makeText(this, "Wähle eine Farbe", Toast.LENGTH_SHORT).show();
        }
    }
}
