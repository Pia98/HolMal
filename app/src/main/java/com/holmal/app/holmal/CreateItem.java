package com.holmal.app.holmal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.OnClick;

public class CreateItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);
    }

    @OnClick (R.id.add)
    public void itemCreationOnClicked(){
        if(validate()){
            //TODO implement functionality aka actually add item to list
            //maybe also check whether item is a favourite here and add to favourites
        }
    }

    //Validation: add button can only be pressed if a name has been given
    public Boolean validate(){
        EditText itemInput = findViewById(R.id.whatInput);
        String itemName = itemInput.getText().toString();
        if (itemName.isEmpty()){
            Toast.makeText(this, R.string.ErrorEnterItem, Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }
    }
}
