package com.holmal.app.holmal.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.holmal.app.holmal.ItemInformationActivity;
import com.holmal.app.holmal.R;
import com.holmal.app.holmal.model.Item;
import com.holmal.app.holmal.model.Person;

import java.util.HashMap;

/**
 * Items Adapter is the adapter that sets the items on the shopping list.
 * It fills the content of every single item and sets it on the list and also enables interactions by providing a click-
 * and a long-click listener.
 * The items adapter is set in {@link com.holmal.app.holmal.ShoppingListActivity}.
 */
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder> {
    private static final String TAG = ItemsAdapter.class.getName();


    private Context context;
    private HashMap<String, Item> items;
    private String[] itemKeys;
    private HashMap<String, Person> person;

    private TextView nameView;
    private TextView descriptionView;
    private ImageView urgencyView;
    private TextView infoView;
    private ImageView assignedView;
    private CheckBox doneView;
    private RecyclerView singleItemView;

    /**
     * Provide a reference to the views for each data item
     */
    static class ItemsViewHolder extends RecyclerView.ViewHolder {
        private View rowView;

        private ItemsViewHolder(View view) {
            super(view);
            rowView = view;
        }
    }

    /**
     * Constructor for ItemsAdapter
     *
     * @param context {@link com.holmal.app.holmal.ShoppingListActivity} context is provided
     * @param items   the items to be displayed on the list
     * @param person  the people of the household are given to enable the display of the assignment of tasks
     */
    public ItemsAdapter(Context context, HashMap<String, Item> items, HashMap<String, Person> person) {
        this.context = context;
        this.items = items;
        this.person = person;
        this.itemKeys = items.keySet().toArray(new String[items.size()]);

    }

    /**
     * Method that creates views for items including part-views for all their data whose layout is specified in single_item_layout
     *
     * @param parent   the parent view
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
        infoView = (TextView) rowView.findViewById(R.id.itemInformation);
        assignedView = (ImageView) rowView.findViewById(R.id.assignedTo);
        doneView = (CheckBox) rowView.findViewById(R.id.itemDone);
        singleItemView = (RecyclerView) rowView.findViewById(R.id.list);

        ItemsViewHolder viewHolder = new ItemsViewHolder(rowView);
        return viewHolder;
    }

    /**
     * Method that fills the items of the viewholder with appropriate content. Actually adapts the view to show items on the list.
     *
     * @param itemsViewHolder the viewHolder
     * @param position        the position at which an item is in the list
     */
    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder itemsViewHolder, final int position) {

        //iterates over the items and gets name and quantitiy of each one
        final Item itemAtPosition = items.get(itemKeys[position]);

        String itemName = itemAtPosition.getItemName();
        nameView.setText(itemName);

        String itemQuantity = itemAtPosition.getQuantity();
        descriptionView.setText(itemQuantity);

        //only shows first 30 letters of the additional info
        String itemInformation = itemAtPosition.getAdditionalInfo();
        if (itemInformation.length() > 30) {
            String itemInformationPreview = itemInformation.substring(0, 30);
            infoView.setText(itemInformationPreview + "...");
        } else {
            infoView.setText(itemInformation);
        }

        //shows ! if the item is urgent
        if (itemAtPosition.isImportant()) {
            urgencyView.setImageAlpha(255);
        } else {
            urgencyView.setImageAlpha(0);
        }

        // checked when item is already done
        if (itemAtPosition.isDone()) {
            doneView.setChecked(true);
        } else {
            doneView.setChecked(false);
        }

        //shows a colored bar according to the person who took on this item as a task
        // when it is noones task set backgroundColor, else the color of the person
        if (itemAtPosition.getItsTask() == null || itemAtPosition.getItsTask().isEmpty()) {
            assignedView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBackground));
        } else {
            Person thisPerson = person.get(itemAtPosition.getItsTask());
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
        itemsViewHolder.rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "clicked item -> open info");
                Intent intent = new Intent(context, ItemInformationActivity.class);
                intent.putExtra("itemId", itemKeys[position]);
                v.getContext().startActivity(intent);
            }
        });
        //handles long click on item to assign the item to oneself (and colour them)
        itemsViewHolder.rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i(TAG, "assign person");
                PreferencesAccess preferencesAccess = new PreferencesAccess();
                String ownPersonID = preferencesAccess.readPreferences(context, "personID");
                if (itemAtPosition.getItsTask().isEmpty()) {
                    String ownPersonKey = FirebaseDatabase.getInstance().getReference().child("person").child(ownPersonID).getKey();
                    FirebaseDatabase.getInstance().getReference().child("item").child(itemKeys[position]).child("itsTask").setValue(ownPersonID);
                } else if (itemAtPosition.getItsTask().equals(ownPersonID)) {
                    FirebaseDatabase.getInstance().getReference().child("item").child(itemKeys[position]).child("itsTask").setValue("");
                } else if (!itemAtPosition.getItsTask().equals(ownPersonID)) {
                    Toast.makeText(context.getApplicationContext(), R.string.alreadyAssigned, Toast.LENGTH_LONG).show();
                }

                return true;
            }
        });

        // handles checkBox click
        doneView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    FirebaseDatabase.getInstance().getReference().child("item").child(itemKeys[position]).child("done").setValue(true);
                } else {
                    FirebaseDatabase.getInstance().getReference().child("item").child(itemKeys[position]).child("done").setValue(false);
                }
            }
        });

    }

    /**
     * getter for the length of the hashmap that contains the items to be displayed
     *
     * @return amount of dispayed items
     */
    public int getItemCount() {
        return itemKeys.length;
    }

}