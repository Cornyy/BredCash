package com.corny.bredcash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.squareup.picasso.Picasso;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class yourAuctionsFragment extends Fragment
{
    private ListView listView;
    private TextView textView;
    private Button your,winning;
    private ArrayList<Auction> output = null;
    private ArrayList<Auction> allAuctionsList = null ;
    private Adapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_your_auctions,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        listView = (ListView)view.findViewById(R.id.YourAuctions_YourOfferWinningListView);
        textView = (TextView)view.findViewById(R.id.yourAuctions_TextView);
        your = (Button)view.findViewById(R.id.YourAuctions_yourAuctionsButton);
        winning = (Button)view.findViewById(R.id.YourAuctions_YourOfferWinningButton);
        adapter = new Adapter();
        SetListenersToButtons();
        refresh(1);
    }

    private void refresh(final int choice)
    {

        final ProgressDialog dialog = ProgressDialog.show(getContext(),"","Loading, Please wait...",true);

        serverConnection.getAllAuctions("withoutValidation",getContext(), new serverConnection.Callback<ArrayList<Auction>>() {
            @Override
            public void onResponse(ArrayList<Auction> response)
            {
                allAuctionsList = response;
                output = new ArrayList<>();
                switch (choice) {
                    case 1:
                        for (Auction a : allAuctionsList) {
                            if (a.getAuth().equals(CurrentUser.getNick())) {
                                output.add(a);
                            }
                        }
                        break;
                    case 2:
                        for (Auction a : allAuctionsList) {
                            if (a.getWinner().equals(CurrentUser.getNick()) && !a.getAuth().equals(CurrentUser.getNick())) {
                                output.add(a);
                            }
                        }
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + choice);
                }
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        ImageView image = (ImageView) view.findViewById(R.id.Auction_image);
                        Bitmap tileImage =((BitmapDrawable)image.getDrawable()).getBitmap();
                        Intent intent = new Intent(getContext(),AuctionDetailsActivity.class);
                        intent.putExtra(AuctionDetailsActivity.DETAILS_KEY,output.get(position).toString());
                        intent.putExtra(AuctionDetailsActivity.IMAGE_KEY,ConvertImageToByteArray(tileImage));
                        intent.putExtra(AuctionDetailsActivity.TYPE_KEY,"yours");
                        startActivity(intent);
                    }
                });
                dialog.dismiss();
                if(output.isEmpty())
                {
                    textView.setVisibility(View.VISIBLE);
                }
                else
                {
                    textView.setVisibility(View.INVISIBLE);
                }
            }
        });


    }
    private byte[] ConvertImageToByteArray(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    private void SetListenersToButtons()
    {
        your.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh(1);
            }
        });
        winning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh(2);
            }
        });
    }

    class Adapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return output.size();

        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent)
        {
            view = getLayoutInflater().inflate(R.layout.auctions_row,null);
            ImageView photo = (ImageView) view.findViewById(R.id.Auction_image);
            TextView title = (TextView) view.findViewById(R.id.Auction_title);
            TextView price = (TextView) view.findViewById(R.id.Auction_price);
            TextView date = (TextView) view.findViewById(R.id.Auction_date);

            String URLIMAGE ="http://46.41.149.32:8080/BredCash/DownloadImage?imageName=" + output.get(position).getImage();
            Picasso.get().load(URLIMAGE).placeholder(R.drawable.no_image).resize(300,300).centerInside()
                    .into(photo);

            title.setText(output.get(position).getTitle());
            price.setText(output.get(position).getPrice() + "zł");

            if(output.get(position).isGoodDate()) {
                date.setText(output.get(position).getEnd_date());
                date.setTextColor(Color.GREEN);
            }
            else
            {
                date.setTextColor(Color.RED);
                date.setText("ENDED");
            }

            return view;
        }
    }
}
