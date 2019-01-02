package com.holmal.app.holmal.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.holmal.app.holmal.R;

import com.holmal.app.holmal.model.Person;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class SettingsAdapter extends ArrayAdapter<Person> {

    ArrayList<Person> householdMembers;
    private Context context;

    //constructor
    public SettingsAdapter(Context context, ArrayList<Person> householdMembers){
        super(context, R.layout.single_household_member_settings, householdMembers);
        this.context = context;
        this.householdMembers = householdMembers;
    }


    @Override
    public int getCount() {
        if(householdMembers == null) {
            return 0;
        }
        else{
            return householdMembers.size();
        }
    }

    @Override
    public Person getItem(int i) {
        return householdMembers.get(i);
    }

    //method that actually adapts the view to show the household members
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.single_household_member_settings, parent, false);

        ImageView colour = (ImageView) rowView.findViewById(R.id.personColour);
        TextView name = (TextView) rowView.findViewById(R.id.personName);

        //gets the person listener from firebase
        PersonListener listener = new PersonListener();
        List<Person> personInHousehold = listener.getPersonList();

        for (int i = 0; i< personInHousehold.size(); i++){

            colour.setColorFilter(personInHousehold.get(i).getColor());
            name.setText(personInHousehold.get(i).getPersonName());


        }
        return rowView;
    }

}