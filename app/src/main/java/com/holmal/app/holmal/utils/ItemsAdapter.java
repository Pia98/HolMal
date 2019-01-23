package com.holmal.app.holmal.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.holmal.app.holmal.R;
import com.holmal.app.holmal.model.Item;
import com.holmal.app.holmal.model.Person;
import java.util.HashMap;


public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder> {

    private Context context;
    private HashMap<String, Item> items;
    private String[] itemKeys;
    private HashMap<String, Person> person;

    private TextView nameView;
    private TextView descriptionView;
    private ImageView urgencyView;
    private ImageView infoView;
    private ImageView assignedView;
    RecyclerView singleItemView;

    /**
     * Provide a reference to the views for each data item
     */
    public static class ItemsViewHolder extends RecyclerView.ViewHolder {
        private View rowView;
        private ItemsViewHolder(View view) {
            super(view);
            rowView = view;
        }
    }

    /**
     * constructor for ItemsAdapter
     * @param context ShoppingListActivity context is provided
     * @param items the items to be displayed on the list
     * @param person the people of the household are given to enable the display of the assignment of tasks
     */
    public ItemsAdapter(Context context, HashMap<String, Item> items, HashMap<String, Person> person){
        this.context = context;
        this.items = items;
        this.person = person;
        this.itemKeys = items.keySet().toArray(new String[items.size()]);

    }

    /**
     * Method that creates views for items including part-views for all their data whose layout is specified in single_item_layout
     * @param parent the parent view
     * @param viewType the type of view
     * @return the item view holder
     */
    @NonNull
    @Override
    public ItemsAdapter.ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.single_item_layout, parent, false);

        nameView = (TextView) rowView.findViewById(R.id.itemName);
        descriptionView = (TextView) rowView.findViewById(R.id.itemAmount);
        urgencyView = (ImageView) rowView.findViewById(R.id.urgent);
        infoView = (ImageView) rowView.findViewById(R.id.infoAvailable);
        assignedView = (ImageView) rowView.findViewById(R.id.assignedTo);
        singleItemView = (RecyclerView) rowView.findViewById(R.id.list) ;

        ItemsViewHolder viewHolder = new ItemsViewHolder(rowView);
        return viewHolder;
    }


    /**
     * Method that fills the items of the viewholder with appropriate content
     * @param itemsViewHolder the viewHolder
     * @param position the position at which an item is in the list
     */
    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder itemsViewHolder, final int position) {
       //adapts view to show items on list
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

        //handles click on item to see detailed information
        itemsViewHolder.rowView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Item clickedItem = items.get(itemKeys[position]);
                if (!clickedItem.getAdditionalInfo().isEmpty()) {
                    //TODO starte ItemInformationFragment
                    Log.i("FürSvenja", "clicked item -> open info");
                }
            }
        });


    }


    /**
     * getter for the itemid that isn't used
     * @param position position of the item
     * @return the itemid
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * getter for the length of the hashmap that contains the items to be displayed
     * @return amount of dispayed items
     */
    @Override
    public int getItemCount() {
        return itemKeys.length;
    }



/*

        //handles click on item to see detailed information
        rowView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Item clickedItem = getItem(position);
                if (!clickedItem.getAdditionalInfo().isEmpty()) {
                    //TODO starte ItemInformationFragment
                    Log.i("FürSvenja", "clicked item -> open info");
                }
            }
        });


        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Item clickedItem = (Item) parent.getItemAtPosition(position);
            Log.i("FürSvenja", "assign person");
            if (clickedItem.getItsTask() == null) {

                PreferencesAccess preferencesAccess = new PreferencesAccess();
                String ownPersonID = preferencesAccess.readPreferences(ShoppingListActivity.this, "personID");
                String ownPersonKey = FirebaseDatabase.getInstance().getReference().child("person").child(ownPersonID).getKey();
                clickedItem.setItsTask(ownPersonID);
            }
            else{
                clickedItem.setItsTask(null);
            }
            return false;
        }
    });*/

    /**
     * the viewHolder for this recyclerview
     * contains the views that the content is displayed upon
     */
    public class ItemViewHolder extends RecyclerView.ViewHolder{

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.itemName);
            descriptionView = (TextView) itemView.findViewById(R.id.itemAmount);
            urgencyView = (ImageView) itemView.findViewById(R.id.urgent);
            infoView = (ImageView) itemView.findViewById(R.id.infoAvailable);
            assignedView = (ImageView) itemView.findViewById(R.id.assignedTo);
            singleItemView = (RecyclerView) itemView.findViewById(R.id.list) ;
        }


    }




}

