package com.corny.bredcash;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AuctionDetailsActivity extends AppCompatActivity {
    private Auction auction;
    private String JSON,type;
    private Intent intent;
    private TextView title,description,actuallyPrice;
    private ImageView image;
    private Button bidOfferButton;
    private byte[] imageArray;
    private Bitmap imageBitmap;
    static public String DETAILS_KEY = "DETAILS_KEY";
    static public String IMAGE_KEY = "IMAGE_KEY";
    static public String TYPE_KEY = "TYPE_KEY";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_details);



        title = (TextView) findViewById(R.id.auctionDetails_title);
        description = (TextView) findViewById(R.id.auctionDetails_description);
        actuallyPrice = (TextView) findViewById(R.id.auctionDetails_actuallyPriceTextView);
        image = (ImageView) findViewById(R.id.auctionDetails_image);

        System.out.println(image.getLayoutParams().width + " " + image.getLayoutParams().height);
        bidOfferButton = (Button) findViewById(R.id.auctionDetails_bidTheOffer);

        intent = getIntent();
        JSON = intent.getStringExtra(DETAILS_KEY);
        imageArray = intent.getByteArrayExtra(IMAGE_KEY);
        type = intent.getStringExtra(TYPE_KEY);
        auction = Auction.FromJsonToAuction(JSON);
        if(type!=null)
        {
            bidOfferButton.setVisibility(View.INVISIBLE);
        }
        bidOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BidTheOfferOnClick();
            }
        });
        showInformations();
        convertByteArrayToBitmap();

    }

    private void showInformations()
    {
        title.setText(auction.getTitle());
        description.setText(auction.getDescription());
        actuallyPrice.setText(" Actually price : " + auction.getPrice() + " zł");

    }

    private void convertByteArrayToBitmap()
    {
        imageBitmap = BitmapFactory.decodeByteArray(imageArray,0,imageArray.length);

        image.setImageBitmap(imageBitmap);
        image.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap,imageBitmap.getWidth(),imageBitmap.getHeight(),false));


    }

    private void BidTheOfferOnClick()
    {
        final CurrentUser user = new CurrentUser();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = null;
        View dialogView = getLayoutInflater().inflate(R.layout.bid_the_offer_dialog,null);
        TextView textView = dialogView.findViewById(R.id.BidOfferDialogTextView);
        final EditText editText = dialogView.findViewById(R.id.BidOfferDialogEditText);
        Button button = dialogView.findViewById(R.id.BidOfferDialogButton);
        textView.setText(actuallyPrice.getText().toString());

        builder.setView(dialogView);
        dialog = builder.create();
        dialog.show();

        final AlertDialog finalDialog = dialog;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int i = Integer.parseInt(editText.getText().toString());
                final int x = CurrentUser.getAmount();
                if(i<= x) {
                    serverConnection.bidTheOffer(auction.getId(), Integer.parseInt(editText.getText().toString()), new serverConnection.Callback<String>() {
                        @Override
                        public void onResponse(String response) {
                            String message = response;
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            if (message.equals("You succesfully bidded the offer!")) {
                            CurrentUser.decreaseAmount(i);
                                finish();
                            }
                        }
                    });
                    finalDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Please wait...", Toast.LENGTH_LONG).show();
                }
                else
                {
                    finalDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "You dont have enough money!", Toast.LENGTH_LONG).show();
                }

            }

        });


    }
}
