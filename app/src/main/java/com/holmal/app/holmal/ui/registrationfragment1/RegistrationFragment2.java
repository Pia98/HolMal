package com.holmal.app.holmal.ui.registrationfragment1;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.holmal.app.holmal.MoveInHouseholdActivity;
import com.holmal.app.holmal.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationFragment2 extends Fragment {

    private RegistrationFragment2ViewModel mViewModel;
    String inputId;

    public static RegistrationFragment2 newInstance() {
        return new RegistrationFragment2();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration_fragment2, container, false);
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
            Intent intent = new Intent(getActivity(), MoveInHouseholdActivity.class);
            intent.putExtra("inputId", inputId);
            startActivity(intent);
        }
    }

    //checks for input
    public Boolean validate() {
        EditText householdId = (EditText) getView().findViewById(R.id.idInput);
        inputId = householdId.getText().toString();
        if (inputId.isEmpty()) {
            Toast.makeText(getActivity(), R.string.ErrorChooseHousehold, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}