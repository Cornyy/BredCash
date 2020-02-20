package com.corny.bredcash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.squareup.picasso.Picasso;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class homeFragment extends Fragment
{
    private ListView listView;
    private TextView textView;
    private ImageView photo;
    private static ArrayList<Auction> list;
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.home_fragment,container,false);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        listView = (ListView) view.findViewById(R.id.AuctionsListView);
        textView = (TextView) view.findViewById(R.id.AuctionsTextView);
      refresh();

    }

    private void refresh()
    {

        final ProgressDialog dialog = ProgressDialog.show(getContext(),"","Loading, Please wait...",true);

        serverConnection.getAllAuctions("withValidation",getContext(), new serverConnection.Callback<ArrayList<Auction>>() {
            @Override
            public void onResponse(ArrayList<Auction> response)
            {
                list = response;
                dialog.dismiss();
                Adapter adapter = new Adapter();
                listView.setAdapter(adapter);
                if(list.isEmpty())
                {
                    textView.setVisibility(View.VISIBLE);
                }
                else
                {
                    textView.setVisibility(View.INVISIBLE);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                ImageView image = (ImageView) view.findViewById(R.id.Auction_image);
                Bitmap tileImage =((BitmapDrawable)image.getDrawable()).getBitmap();
                Intent intent = new Intent(getContext(),AuctionDetailsActivity.class);
                intent.putExtra(AuctionDetailsActivity.DETAILS_KEY,list.get(position).toString());
                intent.putExtra(AuctionDetailsActivity.IMAGE_KEY,ConvertImageToByteArray(tileImage));
                startActivity(intent);
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

    class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();

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
        public View getView(int position, View view, ViewGroup parent) {
            view = getLayoutInflater().inflate(R.layout.auctions_row, null);
            photo = (ImageView) view.findViewById(R.id.Auction_image);
            TextView title = (TextView) view.findViewById(R.id.Auction_title);
            TextView price = (TextView) view.findViewById(R.id.Auction_price);
            TextView date = (TextView) view.findViewById(R.id.Auction_date);
            String URLIMAGE ="http://46.41.149.32:8080/BredCash/DownloadImage?imageName=" + list.get(position).getImage();
            Picasso.get().load(URLIMAGE).placeholder(R.drawable.no_image).resize(300,300).centerInside()
                    .into(photo);
            title.setText(list.get(position).getTitle());
            price.setText(list.get(position).getPrice() + "zł");
            date.setText(list.get(position).getEnd_date());

            return view;
        }


    }



}
