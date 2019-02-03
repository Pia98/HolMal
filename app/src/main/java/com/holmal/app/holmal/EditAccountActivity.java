package com.holmal.app.holmal;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditAccountActivity extends AppCompatActivity {

    @BindView(R.id.editOldPasswordSection)
    ConstraintLayout editOldPassword;

    @BindView(R.id.editAccountSection)
    ConstraintLayout editAccount;

    @BindView(R.id.passwortFurther)
    Button further;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        ButterKnife.bind(this);

        editAccount.setVisibility(View.GONE);
    }

    @OnClick(R.id.passwortFurther)
    public void further(){
        if(validateOldPassword()){
            editOldPassword.setVisibility(View.GONE);
            editAccount.setVisibility(View.VISIBLE);
            further.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.accountEditClose)
    public void close(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean validateOldPassword(){
        return true;
    }
}
