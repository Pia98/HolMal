package com.holmal.app.holmal.utils;

import android.content.Context;
import android.os.Bundle;
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

    ArrayList<Person> householdMembers;
    private Context context;
    FireBaseHandling fireBaseHandling = FireBaseHandling.getInstance();
    Intent intent;

    //constructor
    public SettingsAdapter(Context context, ArrayList<Person> householdMembers, Intent intent){
        super(context, R.layout.single_household_member_settings, householdMembers);
        this.context = context;
        this.householdMembers = householdMembers;
        this.intent = intent;

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

        Bundle extras =  intent.getExtras();
        String householdId = extras.getString("inputId");
        // listener fuer personen starten, gleich bei erzeugen, bevor Person gespeichert, wegen Abfragen ob bereits in Haushalt vorhanden
        fireBaseHandling.startPersonValueEventListener(householdId);

        List<Person> personInHousehold = fireBaseHandling.getPersonListener().getPersonList();

        for (int i = 0; i< personInHousehold.size(); i++){

            colour.setColorFilter(personInHousehold.get(i).getColor());
            name.setText(personInHousehold.get(i).getPersonName());
        }
        return rowView;
    }

}