package com.holmal.app.holmal.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.holmal.app.holmal.R;
import com.holmal.app.holmal.model.Item;
import com.holmal.app.holmal.model.Person;

import java.util.HashMap;


public class ItemsAdapter extends BaseAdapter {
    // TODO raus

    private Context context;
    private HashMap<String, Item> items;
    private String[] itemKeys;
    private HashMap<String, Person> person;

    //constructor
    public ItemsAdapter(Context context, HashMap<String, Item> items, HashMap<String, Person> person) {
        this.context = context;
        this.items = items;
        this.person = person;
        this.itemKeys = items.keySet().toArray(new String[items.size()]);

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Item getItem(int i) {
        return items.get(itemKeys[i]);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    //method that actually adapts the view to show the items on the list
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.single_item_layout, parent, false);
        TextView nameView = (TextView) rowView.findViewById(R.id.itemName);
        TextView descriptionView = (TextView) rowView.findViewById(R.id.itemAmount);

        ImageView urgencyView = (ImageView) rowView.findViewById(R.id.urgent);
        ImageView infoView = (ImageView) rowView.findViewById(R.id.infoAvailable);
        ImageView assignedView = (ImageView) rowView.findViewById(R.id.assignedTo);

        //iterates over the items and gets name and quantitiy of each one

        String itemName = items.get(itemKeys[position]).getItemName();
        nameView.setText(itemName);

        String itemQuantity = items.get(itemKeys[position]).getQuantity();
        descriptionView.setText(itemQuantity);

        //shows ! if the item is urgent
        if (items.get(itemKeys[position]).isImportant()) {
            urgencyView.setImageAlpha(255);
        } else {
            urgencyView.setImageAlpha(0);
        }

        //shows an i if there is additional information to this item
        if (items.get(itemKeys[position]).getAdditionalInfo().isEmpty()) {
            infoView.setImageAlpha(0);
        } else {
            infoView.setImageAlpha(255);
        }

        //shows a colored bar according to the person who took on this item as a task
        // when it is noones task set backgroundColor, else the color of the person
        if (items.get(itemKeys[position]).getItsTask() == null || items.get(itemKeys[position]).getItsTask().isEmpty()) {
            assignedView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBackground));
        } else {
            Person thisPerson = person.get(items.get(itemKeys[position]).getItsTask());
            if (thisPerson.getColor() == R.id.color1) {
                assignedView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPersonLightGreen));
            } else if (thisPerson.getColor() == R.id.color2) {
                assignedView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPersonGreen));
            } else if (thisPerson.getColor() == R.id.color3) {
                assignedView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPersonBlue));
            } else if (thisPerson.getColor() == R.id.color4) {
                assignedView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPersonLightBlue));
            } else if (thisPerson.getColor() == R.id.color5) {
                assignedView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPersonOrange));
            } else if (thisPerson.getColor() == R.id.color6) {
                assignedView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPersonRed));
            } else if (thisPerson.getColor() == R.id.color7) {
                assignedView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPersonPink));
            } else if (thisPerson.getColor() == R.id.color8) {
                assignedView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPersonPurple));
            }
        }

        return rowView;
    }
}