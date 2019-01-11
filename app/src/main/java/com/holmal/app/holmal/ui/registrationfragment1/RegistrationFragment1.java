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
import android.widget.TextView;

import com.holmal.app.holmal.R;
import com.holmal.app.holmal.ShoppingListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationFragment1 extends Fragment {

    private RegistrationFragment1ViewModel mViewModel;

    public static RegistrationFragment1 newInstance() {
        return new RegistrationFragment1();
    }

    @BindView(R.id.householtNameFromInput)
    TextView inputHousehold;

    @BindView(R.id.generatedID)
    TextView generatedID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_registration_fragment1, container, false);
        ButterKnife.bind(this, view);

        String userName = this.getArguments().getString("userName");
        String householdName = this.getArguments().getString("householdName");
        String householdId = this.getArguments().getString("householdId");

        inputHousehold.setText(householdName);
        generatedID.setText(householdId);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RegistrationFragment1ViewModel.class);
        // TODO: Use the ViewModel
    }

    @OnClick(R.id.registrationCreateDone)
    public void registrationCreateButtonDoneClick(){
        Intent intent = new Intent(getActivity(), ShoppingListActivity.class); // decide if main (screen 11) or screen 5 (shoppingList), then change here
        startActivity(intent);
    }
}
