package com.corny.bredcash;


import android.app.ProgressDialog;
import android.content.Intent;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


import static android.app.Activity.RESULT_OK;

public class addProductFragment extends Fragment
{
    private Bitmap bitmap;
    private Button buttonSelectImage;
    private Button buttonAddProduct;
    private EditText title;
    private EditText description;
    private TextView imageName;
    private EditText duration;
    private EditText price;
    private CurrentUser currentUser;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.add_product_fragment,container,false);
    }



    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        buttonSelectImage = (Button)view.findViewById(R.id.selectImageButton);
        buttonAddProduct = (Button)view.findViewById(R.id.addProductButton);
        title = (EditText)view.findViewById(R.id.addProduct_Title);
        description = (EditText) view.findViewById(R.id.addProduct_Description);
        imageName = (TextView) view.findViewById(R.id.selectImageTextView);
        duration = (EditText) view.findViewById(R.id.durationEditText);
        price = (EditText) view.findViewById(R.id.addProduct_Price) ;
        currentUser = new CurrentUser();



        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });



        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = ProgressDialog.show(getContext(),"","please wait...");
               String filename  =  serverConnection.uploadImage(bitmap,getContext());
                serverConnection.addProduct(title.getText().toString(), description.getText().toString()
                        , currentUser.getNick(), addDay(Integer.valueOf(duration.getText().toString())), filename, price.getText().toString(), new serverConnection.Callback<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                                clearFields();
                                progressDialog.dismiss();
                            }
                        });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                Uri selectedImage = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                    imageName.setText(selectedImage.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }


    private static String addDay(int numberOfDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(calendar.getTime());
        calendar.add(Calendar.DAY_OF_YEAR, numberOfDays);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(calendar.getTime());
        return date;
    }
    private void clearFields()
    {
        title.getText().clear();
        description.getText().clear();
        imageName.setText("No photo selected");
        duration.getText().clear();
        //price.setText(0);
    }
}
