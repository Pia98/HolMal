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

import com.holmal.app.holmal.MainActivity;
import com.holmal.app.holmal.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationFragment1 extends Fragment {

    private RegistrationFragment1ViewModel mViewModel;

    public static RegistrationFragment1 newInstance() {
        return new RegistrationFragment1();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.registration_fragment1_fragment, container, false);
        ButterKnife.bind(this, view);

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
        Intent intent = new Intent(getActivity(), MainActivity.class); // decide if main (screen 11) or screen 5, then change here
        startActivity(intent);
    }
}
