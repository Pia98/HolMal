package com.holmal.app.holmal.utils.listener;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.holmal.app.holmal.model.ShoppingList;

import java.util.HashMap;
import java.util.Iterator;

public class ShoppingListListener implements ValueEventListener {
    //private static final String TAG = ShoppingListListener.class.getName();


    HashMap<String, ShoppingList> shoppingListList = new HashMap<>();

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        shoppingListList.clear();
        Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();
        Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
        while (iterator.hasNext()) {
            DataSnapshot snapshot = iterator.next();
            String id = snapshot.getKey();
            ShoppingList value = snapshot.getValue(ShoppingList.class);
            //value.setStoreId(snapshot.getKey());
            shoppingListList.put(id, value);
        }
        Log.i("ShoppingListListener", "onDataChange shoppingListList: " + shoppingListList);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Log.i("ShoppingListListener", "onCancelled");
    }

    public HashMap<String, ShoppingList> getShoppingListList() {
        return shoppingListList;
    }
}
