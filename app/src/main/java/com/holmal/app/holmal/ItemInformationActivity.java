package com.holmal.app.holmal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.holmal.app.holmal.model.Item;
import com.holmal.app.holmal.model.Person;
import com.holmal.app.holmal.utils.PreferencesAccess;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ItemInformationActivity extends AppCompatActivity {
    private static final String TAG = ItemInformationActivity.class.getName();

    @BindView(R.id.itemName)
    TextView itemNameText;

    @BindView(R.id.itemAmount)
    TextView itemAmountText;

    @BindView(R.id.itemDesctiption)
    TextView itemDescriptionText;

    @BindView(R.id.itemTask)
    TextView itemTaskText;

    @BindView(R.id.itemUrgent)
    TextView itemUrgentText;

    HashMap<String, Person> joiningPerson = new HashMap<>();

    HashMap<String, Item> item = new HashMap<>();
    Item thisItem;
    String itemId;

    String householdId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_information);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        itemId = extras.getString("itemId");

        PreferencesAccess preferencesAccess = new PreferencesAccess();
        householdId = preferencesAccess.readPreferences(this, getString(R.string.householdIDPreference));

        startPersonListener();

        startItemListener();
    }

    private void startItemListener() {
        FirebaseDatabase.getInstance().getReference().child("item").child(itemId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, "item listener in onCreate...");
                thisItem = dataSnapshot.getValue(Item.class);
                Log.i(TAG, "thisItem " + thisItem);
                String itemName = thisItem.getItemName();
                String itemAmount = thisItem.getQuantity();
                String itemDescription = thisItem.getAdditionalInfo();
                String itemTask = thisItem.getItsTask();
                Boolean itemUrgent = thisItem.isImportant();

                itemNameText.setText(itemName);
                itemAmountText.setText(itemAmount);
                itemDescriptionText.setText(itemDescription);
                if(!itemTask.isEmpty()){
                    String personName = joiningPerson.get(itemTask).getPersonName();
                    itemTaskText.setText(String.format(getString(R.string.brings), personName));
                } else {
                    itemTaskText.setText(itemTask);
                }
                if(itemUrgent){
                    itemUrgentText.setVisibility(View.VISIBLE);
                } else{
                    itemUrgentText.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void startPersonListener() {
        FirebaseDatabase.getInstance().getReference().child("person").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, "listener in onCreate...");
                joiningPerson.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.i(TAG, "alle Personen durchgehen");
                    String id = child.getKey();
                    Person value = child.getValue(Person.class);
                    Log.i(TAG, "Person: " + value);
                    if (value.getIdBelongingTo().equals(householdId)) {
                        Log.i(TAG, "Person gehört zu diesem Haushalt.");
                        joiningPerson.put(id, value);
                    }

                    Log.i(TAG, "joiningPerson in for Schleife bei listener: " + joiningPerson);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.itemClose)
    public void onItemCloseClicked(){
        Intent intent = new Intent(this, ShoppingListActivity.class);
        startActivity(intent);
    }
}
