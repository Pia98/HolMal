package com.holmal.app.holmal.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.holmal.app.holmal.R;
import com.holmal.app.holmal.model.Item;



public class ItemsAdapter extends ArrayAdapter<Item> {

    private Context context;
    private Item[] items;

    //constructor
    public ItemsAdapter(Context context, Item[] items){
        super(context, R.layout.single_item_layout);
        this.context = context;
        this.items = items;

    }

    @Override
    public int getCount() {
        if(items.length== 0) {
            return 0;
        }
        else{
            return items.length;
        }
    }

    @Override
    public Item getItem(int i) {
        return items[i];
    }


    //method that actually adapts the view to show the items on the list
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.single_item_layout, parent, false);
        TextView nameView = (TextView) rowView.findViewById(R.id.itemName);
        TextView descriptionView = (TextView) rowView.findViewById(R.id.itemAmount);

        ImageView urgencyView = (ImageView) rowView.findViewById(R.id.urgent);
        ImageView infoView = (ImageView) rowView.findViewById(R.id.infoAvailable);
        ImageView assignedView = (ImageView) rowView.findViewById(R.id.assignedTo);

        //iterates over the items and gets name and quantitiy of each one

           String itemName = items[position].getItemName();
           nameView.setText(itemName);

           String itemQuantity = items[position].getQuantity();
           descriptionView.setText(itemQuantity);

           //TODO Kommentar weg wenn Eigenschaften auf die ich mich beziehe existieren
            /*
            if(items[i].getUrgency()){
                urgencyView.setVisible(true);
                }
            else{
                 urgencyView.setVisible(false);
                }

            if(items[i].getInfoAvailable(){
                infoView.setVisible(true);
                }
            else{
                 infoView.setVisible(false);
                }

            if (items[i].isAssignedTo() = null){
                assignedView.setBackground to Backgroundcolour
                }
            else{
            assignedView.setBackground(getColour of the person its assigned to)
            }
             */

    return rowView;
    }

}