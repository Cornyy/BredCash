package com.corny.bredcash;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import javax.crypto.Cipher;

public class accountFragment extends Fragment
{
    CurrentUser currentUser;
    TextView textViewNick;
    TextView textViewAmount;
    Button button;
    AlertDialog dialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.account_fragment,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        currentUser = new CurrentUser();
        textViewNick = getView().findViewById(R.id.accountFragmentNick);
        textViewAmount = getView().findViewById(R.id.accountFragmentAmount);
        button = getView().findViewById(R.id.accountFragmentTopUpAccount);
        refresh();
        setListener();
    }
    private void refresh()
    {
        textViewNick.setText("Nick : " + currentUser.getNick());
        textViewAmount.setText("Money : " + currentUser.getAmount());
    }
    private void setListener()
    {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder Builder = new AlertDialog.Builder(getView().getContext());
                View view = getLayoutInflater().inflate(R.layout.top_up_account_dialog,null);
                final EditText editText = (EditText) view.findViewById(R.id.topUpAccountDialogEditText);
                Button button = (Button) view.findViewById(R.id.topUpAccountDialogButton);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        ProgressDialog Prdialog = ProgressDialog.show(getView().getContext(), "Loading", "Please wait...", true);
                        CurrentUser currentUser = new CurrentUser();
                        serverConnection.TopUpAccount(currentUser.getNick(), Integer.parseInt(editText.getText().toString()
                        ),getView().getContext(),Prdialog);
                        dialog.dismiss();
                    }
                });
                Builder.setView(view);
               dialog = Builder.create();
               dialog.show();
            }
        });
    }
}
