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

import java.util.HashMap;


public class ItemsAdapter extends BaseAdapter {

    private Context context;
    private HashMap<String, Item> items;
    private String[] itemKeys;

    //constructor
    public ItemsAdapter(Context context, HashMap<String, Item> items){
        this.context = context;
        this.items = items;
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

        String itemName = items.get(itemKeys[position]).getItemName();
        nameView.setText(itemName);

        String itemQuantity = items.get(itemKeys[position]).getQuantity();
        descriptionView.setText(itemQuantity);

        //shows ! if the item is urgent
        if(items.get(itemKeys[position]).isImportant()){
            urgencyView.setImageAlpha(255);
        }
        else{
            urgencyView.setImageAlpha(0);
        }

        //shows an i if there is additional information to this item
        if(items.get(itemKeys[position]).getAdditionalInfo().isEmpty()){
            infoView.setImageAlpha(0);
        }
        else{
            infoView.setImageAlpha(255);
        }

        //shows a colored bar accordiing to the person who took on this item as a task
        if(items.get(itemKeys[position]).getItsTask() == null){
            assignedView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBackground));
        }
        else{
            assignedView.setBackgroundColor(ContextCompat.getColor(context, items.get(itemKeys[position]).getItsTask().getColor()));
        }

        return rowView;
    }}