package com.holmal.app.holmal.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.holmal.app.holmal.R;
import com.holmal.app.holmal.ShoppingListActivity;
import com.holmal.app.holmal.model.Item;
import com.holmal.app.holmal.model.ShoppingList;

import java.util.HashMap;

/**
 * This adapter is responsible for displaying the lists of a household in an overview.
 * Is called in {@link com.holmal.app.holmal.AllShoppingListsActivity}.
 */
public class ShoppingListsAdapter extends RecyclerView.Adapter<ShoppingListsAdapter.ListsViewHolder> {

    private static final String TAG = ShoppingListsAdapter.class.getName();

    private Context context;
    private HashMap<String, ShoppingList> shoppinglists;
    private String[] listKeys;
    private HashMap<String, Item> itemsOfHousehold;
    private TextView nameView;
    private TextView categoryView;
    private TextView descriptionView;
    private String listAtPositionKey;

    /**
     * Provide a reference to the views for each data item
     */
    static class ListsViewHolder extends RecyclerView.ViewHolder {
        private View gridView;

        private ListsViewHolder(View view) {
            super(view);
            gridView = view;
        }
    }

    /**
     * Constructor of the adapter
     *
     * @param context       the context of {@link com.holmal.app.holmal.AllShoppingListsActivity} the adapter needs
     * @param shoppinglists a list of all shopping lists a household has
     */
    public ShoppingListsAdapter(Context context, HashMap<String, ShoppingList> shoppinglists, HashMap<String, Item> itemsOfHousehold) {
        this.context = context;
        this.shoppinglists = shoppinglists;
        this.listKeys = shoppinglists.keySet().toArray(new String[shoppinglists.size()]);
        this.itemsOfHousehold = itemsOfHousehold;
    }

    /**
     * Method that creates views for the lists. Their layout is specified in single_shoppinglist_layout.
     *
     * @param parent viewgroup the views for the lists lay on
     * @param i      type of view
     * @return the list view holder
     */
    @NonNull
    @Override
    public ShoppingListsAdapter.ListsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView = inflater.inflate(R.layout.single_shoppinglist_layout, parent, false);

        nameView = (TextView) gridView.findViewById(R.id.nameOfList);
        categoryView = (TextView) gridView.findViewById(R.id.categoryOfList);
        descriptionView = (TextView) gridView.findViewById(R.id.amountItemsOnList);

        ListsViewHolder viewHolder = new ListsViewHolder(gridView);
        return viewHolder;
    }

    /**
     * Method that fills the grid of the viewholder with the appropriate content.
     * Also handles interactions with the shoppinglist items.
     *
     * @param listsViewHolder the viewholder
     * @param i               the position at which the list is in the list of lists
     */
    @Override
    public void onBindViewHolder(@NonNull ShoppingListsAdapter.ListsViewHolder listsViewHolder, int i) {

        final ShoppingList listAtPosition = shoppinglists.get(listKeys[i]);
        final String listsID = listKeys[i];
        listAtPositionKey = listKeys[i];

        if(listAtPosition != null){
        //only shows first letters of the name
        String listName = listAtPosition.getListName();
        if (listName.length() > 11) {
            String listNamePreview = listName.substring(0, 10);
            nameView.setText(listNamePreview + "...");
        } else {
            nameView.setText(listName);
        }

        categoryView.setText(listAtPosition.getCategory());

        //add the amount of open items to the card
        int amountOpenItems = 0;
        //iterates over all items
        String[] itemsKeys = itemsOfHousehold.keySet().toArray(new String[itemsOfHousehold.size()]);

        for (int j = 0; j < itemsKeys.length - 1; j++) {
            //checks if the item is on the currently looked at list
            String itemListID = itemsOfHousehold.get(itemsKeys[j]).getBelongsTo();
            if (itemListID.equals(listAtPositionKey)) {
                if (!itemsOfHousehold.get(itemsKeys[j]).isDone()) {
                    amountOpenItems += 1;
                }
            }
        }

        //sets the number of items into the descriptionView
        if (listAtPosition.getItemsOfThisList() == null
                || listAtPosition.getItemsOfThisList().size() == 0 || amountOpenItems == 0) {
            descriptionView.setText(R.string.noOpenItems);
        } else {
            descriptionView.setText(R.string.openItems);
            //descriptionView.append(String.valueOf(listAtPosition.getItemsOfThisList().size()));
            descriptionView.append(String.valueOf(amountOpenItems));
        }

        //handles click on list to see it in a shopping list view
        listsViewHolder.gridView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "clicked list -> open list");
                PreferencesAccess preferencesAccess = new PreferencesAccess();
                preferencesAccess.storePreferences(context, context.getString(R.string.recentShoppingListNamePreference), listAtPosition.getListName());
                Intent intent = new Intent(context, ShoppingListActivity.class);
                v.getContext().startActivity(intent);
            }
        });
        //handles long clicks on shoppinglist which enables one to delete the shopping list
        listsViewHolder.gridView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                //pop up that asks the user if they are sure
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(R.string.deleteListConfirmation);
                builder.setCancelable(true);
                builder.setPositiveButton(
                        R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                //delete items from the pressed list
                                if(listAtPosition.getItemsOfThisList()!= null) {
                                    HashMap<String, String> itemsToDelete = listAtPosition.getItemsOfThisList();
                                        String[] keys = itemsToDelete.keySet().toArray(new String[itemsToDelete.size()]);
                                        for (int i = 0; i < itemsToDelete.size(); i++) {
                                            FireBaseHandling.getInstance().deleteItem(itemsToDelete.get(keys[i]));
                                        }
                                }
                                // set preference null if shoppingList to delete was recent
                                PreferencesAccess preferencesAccess = new PreferencesAccess();
                                String recentShoppingList = preferencesAccess.readPreferences(context, context.getString(R.string.recentShoppingListNamePreference));
                                if(recentShoppingList != null){
                                    if(recentShoppingList.equals(listAtPosition.getListName())){
                                        Log.i(TAG, "set recent shoppingList null");
                                        preferencesAccess.storePreferences(context, context.getString(R.string.recentShoppingListNamePreference), null);
                                    }
                                }
                                //delete shoppingList itself
                                FireBaseHandling.getInstance().deleteShoppingList(listsID);
                            }
                        });
                builder.setNegativeButton(
                        R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();

                return true;
            }
        });}
    }

    //only autogenerated
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Getter for the amount of shopping lists a household has
     *
     * @return amount of shopping lists to be displayed
     */
    @Override
    public int getItemCount() {
        return shoppinglists.size();
    }

}
