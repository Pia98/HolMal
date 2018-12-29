package com.holmal.app.holmal.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.holmal.app.holmal.model.ShoppingList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ShoppingListListener implements ValueEventListener {
    List<ShoppingList> shoppingListList = new ArrayList<>();

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        shoppingListList.clear();
        Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();
        Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
        while (iterator.hasNext()) {
            ShoppingList value = iterator.next().getValue(ShoppingList.class);
            shoppingListList.add(value);
        }
        Log.i("ShoppingList", "shoppingListList: " + shoppingListList);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    public List<ShoppingList> getShoppingListList() {
        return shoppingListList;
    }
}
