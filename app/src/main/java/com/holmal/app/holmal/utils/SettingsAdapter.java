package com.holmal.app.holmal.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.holmal.app.holmal.R;
import android.content.Intent;

import com.holmal.app.holmal.Settings;
import com.holmal.app.holmal.model.Person;
import java.util.ArrayList;
import java.util.List;


public class SettingsAdapter extends ArrayAdapter<Person> {


    private Context context;
    List<Person> personInHousehold;

    //constructor
    public SettingsAdapter(Context context, List<Person> personInHousehold){
        super(context, R.layout.single_household_member_settings);
        this.context = context;
        this.personInHousehold = personInHousehold;
    }


    @Override
    public int getCount() {
        Log.i("personInHousehold", "count: "+personInHousehold);
        if(personInHousehold.size()== 0) {
            return 0;
        }
        else{
            return personInHousehold.size();
        }
    }

    @Override
    public Person getItem(int i) {
        return personInHousehold.get(i);
    }

    //method that actually adapts the view to show the household members
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.single_household_member_settings, parent, false);

        ImageView colour = (ImageView) rowView.findViewById(R.id.personColour);
        TextView name = (TextView) rowView.findViewById(R.id.personName);

        for (int i = 0; i< personInHousehold.size(); i++){

            Log.i("personInHousehold", "person: " + personInHousehold.get(i));
            colour.setColorFilter(personInHousehold.get(i).getColor());
            name.setText(personInHousehold.get(i).getPersonName());
        }
        return rowView;
    }

}