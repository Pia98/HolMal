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

import com.holmal.app.holmal.MoveInHousehold;
import com.holmal.app.holmal.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationFragment2 extends Fragment {

    private RegistrationFragment2ViewModel mViewModel;

    public static RegistrationFragment2 newInstance() {
        return new RegistrationFragment2();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_fragment2_fragment, container, false);
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
    public void idInputDoneClick(){
        Intent intent = new Intent(getActivity(), MoveInHousehold.class);
        startActivity(intent);
    }
}
