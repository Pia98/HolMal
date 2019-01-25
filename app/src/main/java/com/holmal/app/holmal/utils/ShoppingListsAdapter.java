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
import com.holmal.app.holmal.model.ShoppingList;
import java.util.HashMap;

/**
 * This adapter is responsible for displaying the lists of a household in an overview.
 * Is called in AllShoppingListsActivity.
 */
public class ShoppingListsAdapter extends RecyclerView.Adapter<ShoppingListsAdapter.ListsViewHolder>{

    private Context context;
    private HashMap<String, ShoppingList> shoppinglists;
    private String[] listKeys;
    private TextView nameView;
    private TextView categoryView;
    private TextView descriptionView;

    /**
     * Provide a reference to the views for each data item
     */
    public static class ListsViewHolder extends RecyclerView.ViewHolder {
        private View gridView;
        private ListsViewHolder(View view) {
            super(view);
            gridView = view;
        }
    }

    /**
     * Constructor of the adapter
     * @param context the context of AllShoppingLists the adapter needs
     * @param shoppinglists a list of all shopping lists a household has
     */
    public ShoppingListsAdapter(Context context, HashMap<String, ShoppingList> shoppinglists) {
        this.context = context;
        this.shoppinglists = shoppinglists;
        listKeys = shoppinglists.keySet().toArray(new String[shoppinglists.size()]);
    }

    /**
     * Method that creates views for the lists. Their layout is specified in single_shoppinglist_layout.
     * @param parent viewgroup the views for the lists lay on
     * @param i type of view
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
     * @param listsViewHolder the viewholder
     * @param i the position at which the list is in the list of lists
     */
    @Override
    public void onBindViewHolder(@NonNull ShoppingListsAdapter.ListsViewHolder listsViewHolder, int i) {

        final ShoppingList listAtPosition = shoppinglists.get(listKeys[i]);
        final String[] keysShoppingLists = shoppinglists.keySet().toArray(new String[shoppinglists.size()]);
        final String listAtPositionKey = keysShoppingLists[i];

        nameView.setText(listAtPosition.getListName());

        categoryView.setText(listAtPosition.getCategory());

        if (listAtPosition.getItemsOfThisList() == null
                || listAtPosition.getItemsOfThisList().size() == 0) {
            descriptionView.setText(R.string.noOpenItems);
        } else {
            HashMap<String, String> itemsOnList = listAtPosition.getItemsOfThisList();
            int amountOpenItems = 0;
            String[] keys = itemsOnList.keySet().toArray(new String[itemsOnList.size()]);
            //iterates over list of items to get the amount of items that are still open
            for(int j = 0; j<itemsOnList.size(); j++ ){
               //if(itemsOnList.get(keys[i]))
            }
            descriptionView.setText(R.string.openItems);
            descriptionView.append(String.valueOf(listAtPosition.getItemsOfThisList().size()));
        }

        //handles click on list to see it in a shopping list view
        listsViewHolder.gridView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("FürSvenja", "clicked list -> open list");
                PreferencesAccess preferencesAccess = new PreferencesAccess();
                preferencesAccess.storePreferences(context,context.getString(R.string.recentShoppingListNamePreference), listAtPosition.getListName());
                Intent intent = new Intent(context, ShoppingListActivity.class);
                v.getContext().startActivity(intent);
            }
        });
        //handles long clicks on shoppinglist which enables one to delete the shopping list
        listsViewHolder.gridView.setOnLongClickListener(new View.OnLongClickListener(){
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
                                HashMap<String, String> itemsToDelete = listAtPosition.getItemsOfThisList();
                                String[] keys = itemsToDelete.keySet().toArray(new String[itemsToDelete.size()]);
                                for (int i = 0; i<itemsToDelete.size(); i++){
                                    FireBaseHandling.getInstance().deleteItem(itemsToDelete.get(keys[i]));
                                }
                                //delete shoppingList itself
                                FireBaseHandling.getInstance().deleteShoppingList(listAtPositionKey);
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
        });
    }

    //only autogenerated
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * Getter for the amount of shopping lists a household has
     * @return amount of shopping lists to be displayed
     */
    @Override
    public int getItemCount() {
        return shoppinglists.size();
    }

}
