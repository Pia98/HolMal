package com.holmal.app.holmal;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditAccountActivity extends AppCompatActivity {

    @BindView(R.id.editOldPasswordSection)
    ConstraintLayout editOldPassword;

    @BindView(R.id.editAccountSection)
    ConstraintLayout editAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        ButterKnife.bind(this);

        editAccount.setVisibility(View.GONE);
    }
}
