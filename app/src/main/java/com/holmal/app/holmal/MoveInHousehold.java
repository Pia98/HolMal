package com.holmal.app.holmal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

        Button colour1 = (Button) findViewById(R.id.color1);
        Button colour2 = (Button) findViewById(R.id.color2);
        Button colour3 = (Button) findViewById(R.id.color3);
        Button colour4 = (Button) findViewById(R.id.color4);
        Button colour5 = (Button) findViewById(R.id.color5);
        Button colour6 = (Button) findViewById(R.id.color6);
        Button colour7 = (Button) findViewById(R.id.color7);
        Button colour8 = (Button) findViewById(R.id.color8);

        //TODO check if one! button was chosen (I'll do that later)
        if (colour1.isActivated() || colour2.isActivated()){

        }
        else {
            Toast.makeText(this, "Wähle eine Farbe", Toast.LENGTH_SHORT).show();
        }

    }
}
