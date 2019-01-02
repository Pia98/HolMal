package com.holmal.app.holmal.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.holmal.app.holmal.R;
import com.holmal.app.holmal.model.Item;
import com.holmal.app.holmal.model.ShoppingList;


public class ItemsAdapter extends ArrayAdapter<Item> {

    private Context context;

    //constructor
    public ItemsAdapter(Context context){
        super(context, R.layout.single_item_layout);
        this.context = context;
    }

    //method that actually adapts the view to show the items on the list
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.single_item_layout, parent, false);
        TextView nameView = (TextView) rowView.findViewById(R.id.itemName);
        TextView descriptionView = (TextView) rowView.findViewById(R.id.itemAmount);

        //gets shopping list from firebase ShoppingListListener
        ShoppingListListener listener = new ShoppingListListener();
        ShoppingList shoppingList = listener.getShoppingListList().get(0);
        //TODO abfrage welche shopping list man erhält!! wichtig

        //gets itemlist from this shoppingList
        Item[] items = shoppingList.getItemsOfThisList();

        //iterates over the items and gets name and quantitiy of each one
        for (int i = 0; i< items.length; i++){

           String itemName = items[i].getItemName();
           nameView.setText(itemName);

           int itemQuantity = items[i].getQuantity();
           descriptionView.setText(itemQuantity);
    }
    return rowView;
    }

}