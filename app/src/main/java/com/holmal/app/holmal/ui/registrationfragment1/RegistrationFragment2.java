package com.holmal.app.holmal.ui.registrationfragment1;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.holmal.app.holmal.MoveInHouseholdActivity;
import com.holmal.app.holmal.R;
import com.holmal.app.holmal.model.Household;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationFragment2 extends Fragment {
    private static final String TAG = RegistrationFragment2.class.getName();

    private RegistrationFragment2ViewModel mViewModel;
    String inputId;
    private HashMap<String, Household> haushalte = new HashMap<>();

    public static RegistrationFragment2 newInstance() {
        return new RegistrationFragment2();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration_fragment2, container, false);
        startHouseholdListener();
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RegistrationFragment2ViewModel.class);
        // TODO: Use the ViewModel
    }

    @OnClick(R.id.idInputDone)
    public void idInputDoneClick() {
        if (validate()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            String haushaltName = haushalte.get(inputId).getHouseholdName();
            String message = R.string.moveinConfirmation + "\nID: " + inputId + "\nName: " + haushaltName;
            builder.setMessage(message);
            builder.setCancelable(true);
            builder.setPositiveButton(
                    R.string.yes,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(getActivity(), MoveInHouseholdActivity.class);
                            intent.putExtra("inputId", inputId);
                            startActivity(intent);
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

    private void startHouseholdListener() {
        FirebaseDatabase.getInstance().getReference().child("household").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, "household listener in onCreate: LoginActivity");
                haushalte.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.i(TAG, "alle Haushalte durchgehen");
                    String id = child.getKey();
                    Household value = child.getValue(Household.class);
                    Log.i(TAG, "Haushalt: " + value.getHouseholdName());
                    haushalte.put(id, value);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //checks for input
    public Boolean validate() {
        EditText householdId = (EditText) getView().findViewById(R.id.idInput);
        inputId = householdId.getText().toString();
        if (inputId.isEmpty()) {
            Toast.makeText(getActivity().getApplicationContext(), R.string.ErrorChooseHousehold, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if(haushalte.containsKey(inputId)){
                return true;
            }
            else{
                Toast.makeText(getActivity().getApplicationContext(), R.string.ErrorFindHousehold, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }
}