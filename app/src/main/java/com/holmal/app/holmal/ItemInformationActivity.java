package com.holmal.app.holmal;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.holmal.app.holmal.model.Item;
import com.holmal.app.holmal.model.Person;
import com.holmal.app.holmal.utils.FireBaseHandling;
import com.holmal.app.holmal.utils.PreferencesAccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ItemInformationActivity extends AppCompatActivity {
    private static final String TAG = ItemInformationActivity.class.getName();

    //---------- info tests -------------------
    @BindView(R.id.itemName)
    TextView itemNameText;

    @BindView(R.id.itemAmount)
    TextView itemAmountText;

    @BindView(R.id.itemDesctiption)
    TextView itemDescriptionText;

    @BindView(R.id.itemTask)
    TextView itemTaskText;

    //----------- item edit buttons --------------------------
    @BindView(R.id.itemComplete)
    Button itemComplete;

    @BindView(R.id.itemEdit)
    Button itemEdit;

    //---------- item edit texts ----------------------
    @BindView(R.id.itemEditName)
    EditText itemEditName;

    @BindView(R.id.itemEditAmount)
    EditText itemEditAmount;

    @BindView(R.id.itemEditDescription)
    EditText itemEditDescription;

    @BindView(R.id.itemEditTask)
    Spinner itemEditTask;

    @BindView(R.id.itemUrgentCheck)
    CheckBox itemUrgentCheck;

    //---------------- view groups -------------------------
    @BindView(R.id.editItem)
    ConstraintLayout editItem;

    @BindView(R.id.infoItem)
    ConstraintLayout infoItem;

    HashMap<String, Person> joiningPerson = new HashMap<>();
    ArrayList<String> personenNamen = new ArrayList<>();
    ArrayAdapter<String> adapter;

    Item thisItem;
    String itemId;

    String householdId;
    Boolean itemUrgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_information);
        ButterKnife.bind(this);

        editItem.setVisibility(View.INVISIBLE);
        itemComplete.setVisibility(View.INVISIBLE);
        itemUrgentCheck.setClickable(false);

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
                if (thisItem != null){
                    Log.i(TAG, "thisItem " + thisItem);
                    String itemName = thisItem.getItemName();
                    String itemAmount = thisItem.getQuantity();
                    String itemDescription = thisItem.getAdditionalInfo();
                    String itemTask = thisItem.getItsTask();
                    itemUrgent = thisItem.isImportant();

                    itemNameText.setText(itemName);
                    itemAmountText.setText(itemAmount);
                    itemDescriptionText.setText(itemDescription);
                    if (!itemTask.isEmpty()) {
                        String personName = joiningPerson.get(itemTask).getPersonName();
                        itemTaskText.setText(personName);
                    } else {
                        itemTaskText.setText(itemTask);
                    }
                    if (itemUrgent) {
                        itemUrgentCheck.setChecked(true);
                    } else {
                        itemUrgentCheck.setChecked(false);
                    }
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
                personenNamen.clear();
                personenNamen.add(" - - - ");
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.i(TAG, "alle Personen durchgehen");
                    String id = child.getKey();
                    Person value = child.getValue(Person.class);
                    Log.i(TAG, "Person: " + value);
                    if (value.getIdBelongingTo().equals(householdId)) {
                        Log.i(TAG, "Person gehört zu diesem Haushalt.");
                        joiningPerson.put(id, value);
                        personenNamen.add(value.getPersonName());
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
        finish();
    }

    @OnClick(R.id.itemEdit)
    public void editItem(){
        editItem.setVisibility(View.VISIBLE);
        infoItem.setVisibility(View.INVISIBLE);
        itemComplete.setVisibility(View.VISIBLE);
        itemEdit.setVisibility(View.INVISIBLE);

        itemEditName.setText(itemNameText.getText().toString());
        itemEditAmount.setText(itemAmountText.getText().toString());
        itemEditDescription.setText(itemDescriptionText.getText().toString());
        itemDescriptionText.setVisibility(View.INVISIBLE);
        itemUrgentCheck.setClickable(true);


        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, personenNamen);
        itemEditTask.setAdapter(adapter);
        for(int i=0; i<personenNamen.size(); i++){
            if(personenNamen.get(i) == itemTaskText.getText().toString()){
                itemEditTask.setSelection(i);
                break;
            }
        }
    }

    /**
     * editing of an item complete button
     * checks if a parameter is different than before, than edits it on the database
     */
    @OnClick(R.id.itemComplete)
    public void editComplete(){
        String newName = itemEditName.getText().toString();
        String newAmount = itemEditAmount.getText().toString();
        String newDescription = itemEditDescription.getText().toString();
        String newPersonTask = itemEditTask.getSelectedItem().toString();
        Boolean newUrgent = itemUrgentCheck.isChecked();

        if((newName != itemNameText.getText().toString())&& !newName.isEmpty()){
            FireBaseHandling.getInstance().editItemName(newName, itemId);
        }
        if(newAmount != itemAmountText.getText().toString()){
            FireBaseHandling.getInstance().editItemAmount(newAmount, itemId);
        }
        if(newDescription != itemDescriptionText.getText().toString()){
            FireBaseHandling.getInstance().editItemDescription(newDescription, itemId);
        }
        if(newPersonTask != itemTaskText.getText().toString()){
            Iterator iterator = joiningPerson.entrySet().iterator();
            String personID = null;
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                Person person = (Person) entry.getValue();
                if(person.getPersonName() == newPersonTask){
                    personID = entry.getKey().toString();
                }
            }
            if(personID != null){
                FireBaseHandling.getInstance().editItemPersonTask(personID, itemId);
            }else{
                Toast.makeText(getApplicationContext(), getString(R.string.ErrorItemsPersonTask), Toast.LENGTH_SHORT);
            }
        }
        if(itemUrgent != newUrgent){
            FireBaseHandling.getInstance().editItemUrgent(newUrgent, itemId);
        }

        infoItem.setVisibility(View.VISIBLE);
        editItem.setVisibility(View.INVISIBLE);
        itemUrgentCheck.setClickable(false);
        itemComplete.setVisibility(View.INVISIBLE);
        itemEdit.setVisibility(View.VISIBLE);

    }

    @OnClick(R.id.itemDelete)
    public void deleteItem(){
        if(itemId != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String message = getString(R.string.deleteItemConfirmation) + "\n" + itemNameText.getText().toString();
            builder.setMessage(message);
            builder.setCancelable(true);
            builder.setPositiveButton(
                    R.string.yes,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String tempItemId = itemId;
                            itemId = null;
                            FireBaseHandling.getInstance().deleteItem(tempItemId);
                            Intent intent = new Intent(ItemInformationActivity.this, ShoppingListActivity.class);
                            startActivity(intent);
                            finish();
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
        }
    }
}
