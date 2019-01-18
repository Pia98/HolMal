package com.holmal.app.holmal.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.holmal.app.holmal.R;
import com.holmal.app.holmal.model.Person;

import java.util.HashMap;
import java.util.List;


public class SettingsAdapter extends ArrayAdapter<Person> {


    private Context context;
    HashMap<String, Person> personInHousehold;
    String[] keys;

    //constructor
    public SettingsAdapter(Context context, HashMap<String, Person> personInHousehold){
        super(context, R.layout.single_household_member_settings);
        this.context = context;
        this.personInHousehold = personInHousehold;
        keys = personInHousehold.keySet().toArray(new String[personInHousehold.size()]);
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
        return personInHousehold.get(keys[i]);
    }

    //method that actually adapts the view to show the household members
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.single_household_member_settings, parent, false);

        ImageView colour = (ImageView) rowView.findViewById(R.id.personColour);
        TextView name = (TextView) rowView.findViewById(R.id.personName);

        Person person = personInHousehold.get(keys[position]);
        Log.i("personInHousehold", "person: " + person);

            if (person.getColor() == R.id.color1){
                colour.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPersonLightGreen));
            }else if (person.getColor() == R.id.color2){
                colour.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPersonGreen));
            }else if (person.getColor() == R.id.color3){
                colour.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPersonBlue));
            }else if (person.getColor() == R.id.color4) {
                colour.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPersonLightBlue));
            }else if (person.getColor() == R.id.color5) {
                colour.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPersonOrange));
            }else if (person.getColor() == R.id.color6) {
                colour.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPersonRed));
            }else if (person.getColor() == R.id.color7) {
                colour.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPersonPink));
            }else{
                colour.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPersonPurple));
            }

            name.setText(person.getPersonName());

        return rowView;
    }

}