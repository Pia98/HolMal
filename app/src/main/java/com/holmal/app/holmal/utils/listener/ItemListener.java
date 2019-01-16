package com.holmal.app.holmal.utils.listener;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.holmal.app.holmal.model.Item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemListener implements ValueEventListener {
    private static final String TAG = ItemListener.class.getName();

    List<Item> itemList = new ArrayList<>();

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        itemList.clear();
        Iterable<DataSnapshot> snapshotIterable = dataSnapshot.getChildren();
        Iterator<DataSnapshot> iterator = snapshotIterable.iterator();
        while (iterator.hasNext()) {
            DataSnapshot snapshot = iterator.next();
            Item value = snapshot.getValue(Item.class);
            value.setStoreId(snapshot.getKey());
            itemList.add(value);
        }
        Log.i(TAG, "onDataChange itemList: " + itemList);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Log.i(TAG, "onCancelled");
    }
}
