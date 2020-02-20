package com.corny.bredcash;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class serverConnection {
    private static RequestParams params =  new RequestParams();
    private static AsyncHttpClient client = new AsyncHttpClient();
    private  static String message;
    private static CurrentUser currentUser = new CurrentUser();
    public static final String BaseURL = "http://46.41.149.32:8080/BredCash";


    public static CurrentUser Login(String nick, String password, final Context context, final ProgressDialog dialog) {
        params.add("nick", nick);
        params.add("password", password);
        String LoginURL = BaseURL + "/Login";
        client.get(LoginURL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                System.out.println(response.toString());
                try {
                    currentUser = new CurrentUser(response.getString("nick"),
                            Integer.parseInt(response.getString("amount")),
                            Integer.parseInt(response.getString("auctions")),
                            response.getString("join_date"));
                    dialog.dismiss();
                    Intent intent = new Intent(context, PrimaryScreenActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Toast.makeText(context, "Login Failed", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }

        });
        params.remove("nick");
        params.remove("password");

        return currentUser;
    }

    public static void Register(String nick, String password, final Context context, final ProgressDialog progressDialog) {
        params.add("nick", nick);
        params.add("password", password);
        String RegisterURL = BaseURL + "/Registration";
        client.post(RegisterURL, params, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Toast.makeText(context, res, Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                ((Activity) context).finish();
            }
        });
        params.remove("nick");
        params.remove("password");
    }

    public static CurrentUser TopUpAccount(String nick, int value, final Context context, final ProgressDialog progressDialog)
    {
        value = value+(currentUser.getAmount());
        params.add("nick", nick);
        params.add("value", String.valueOf(value));
        String TopUpURL = BaseURL + "/TopUpAccount";
        final int finalValue = value;
        client.get(TopUpURL,params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response)
            {
                try {
                    currentUser = new CurrentUser(response.getString("nick"),
                            Integer.parseInt(response.getString("amount")),
                            Integer.parseInt(response.getString("auctions")),
                            response.getString("join_date"));
                    progressDialog.dismiss();
                    Toast.makeText(context, "top-up successfully completed!", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t)
            {
                Toast.makeText(context, "top-up Failed", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
        params.remove("nick");
        params.remove("value");
        return currentUser;
    }

    public static String uploadImage(Bitmap bitmap  ,final Context context) {

        String UploadURL = BaseURL + "/AddImage";
        String filename = "";
        if (bitmap != null) {
            byte[] image;
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bao);
            image = bao.toByteArray();
            filename = System.currentTimeMillis() + ".png";
            params.put("picture", new ByteArrayInputStream(
                    image),filename);
            client.post(UploadURL,params,new JsonHttpResponseHandler() {});
            params.remove("picture");
        }
        return filename;
    }

    public static void addProduct(String title, String description, String auth_name, String end_date,String imageName,String price,final Callback<String> callback)
    {
        final String[] response = {""};
        String AddURL = BaseURL + "/AddAuction";
        params.put("title",title);
        params.put("price",price);
        params.put("description",description);
        params.put("auth_name",auth_name);
        params.put("end_date",end_date);
        params.put("image_name",imageName);
        client.post(AddURL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
            {
                response[0] = "Auction added succesfully!";
                if (callback != null) {
                    callback.onResponse(response[0]);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                response[0] = "Somenthing went wrong!";
                if (callback != null) {
                    callback.onResponse(response[0]);
                }

            }
        });
        params.remove("title");
        params.remove("description");
        params.remove("auth_name");
        params.remove("end_date");
        params.remove("price");
        params.remove("image_name");

    }

    public static void getAllAuctions(final String type, final Context context, final Callback<ArrayList<Auction>> callback)
    {
        final ArrayList<Auction> list = new ArrayList<>();
        String GetAuctionsURL = BaseURL + "/getAllAuctions";
        params.put("type",type);
        client.get(GetAuctionsURL,params,new JsonHttpResponseHandler()
        {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response)
            {

                for(int i = 0;i<response.length();i++)
                {
                    try
                    {
                        JSONObject obj = response.getJSONObject(i);
                        Auction auObj = makeAuctionFromJson(obj);
                        if(type.equals("withValidation") && auObj.getAuth().equals(CurrentUser.getNick()) == false &&
                                auObj.getWinner().equals(CurrentUser.getNick()) == false){
                            list.add(auObj);
                        }
                        else if(type.equals("withoutValidation"))
                        {
                            list.add(auObj);
                        }

                    } catch (JSONException e)

                    {
                        e.printStackTrace();
                    }
                }
                if (callback != null) {
                    callback.onResponse(list);
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t)
            {
                Toast.makeText(context,res,Toast.LENGTH_LONG).show();
            }
            });
        params.remove("type");

    }

    private static Auction makeAuctionFromJson(JSONObject obj) throws JSONException
    {
       return new Auction(obj.getString("id"),obj.getString("title"),obj.getString("description"),
                obj.getString("photo_name"),obj.getString("price"),obj.getString("start_date"),
                obj.getString("end_date"),obj.getString("auth_id"),obj.getString("win_id"));

    }
    public interface Callback<T>
    {
        void onResponse(T response);
    }

    public static void bidTheOffer(String auction_id,int amount,final Callback<String> callback)
    {
        String BidTheOfferURL = BaseURL + "/bidTheOffer";
        params.put("auction_id",auction_id);
        params.put("nick",CurrentUser.getNick());
        params.put("amount",Integer.toString(amount));

        client.get(BidTheOfferURL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                message = new String(responseBody);
                String[] array = message.split(",");
                if (callback != null) {
                    callback.onResponse(array[1]);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

        params.remove("auction_id");
        params.remove("nick");
        params.remove("amount");


    }
}
