package com.corny.bredcash;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.*;


import java.util.Map;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends Activity {
   private Button button;
   private static EditText nick,password;
    CurrentUser currentUser;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nick = (EditText) findViewById(R.id.editText_nick);
        password = (EditText) findViewById(R.id.editText_password);
        button = (Button) findViewById(R.id.buttonLogin);


        showInformation();
        SetListener();
    }
    public void SetListener()
    {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "Loading", "Please wait...", true);
                currentUser = serverConnection.Login(nick.getText().toString(),password.getText().toString(),getApplicationContext(),dialog);
            }
        });

    }

    public void goToRegistrationOnClick(View view)
    {
        Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void showInformation()
    {

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Information")
                .setMessage("The auctions contained in this app are not real.The application was made for practice purposes and to present the developer's skills.")


                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
