package com.corny.bredcash;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {
    private EditText nick;
    private EditText password;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nick = (EditText) findViewById(R.id.editText_nickRegistery);
        password = (EditText) findViewById(R.id.editText_passwordRegistery);
    }


    public void registerOnClick(View view)
    {
        progressDialog = ProgressDialog.show(RegisterActivity.this, "Loading", "Please wait...", true);
        serverConnection.Register(nick.getText().toString(),password.getText().toString(),RegisterActivity.this,progressDialog);
    }
}
