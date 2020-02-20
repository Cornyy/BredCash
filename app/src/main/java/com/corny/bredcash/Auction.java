package com.corny.bredcash;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Auction
{
    private String id;
    private String title;
    private String description;
    private String photo_name;
    private String price;
    private String start_date;
    private String end_date;
    private String auth_id;
    private String win_id;


    public Auction( String id,String title,String description,String photo_name,String price,String start_date,String end_date,String auth_id,String win_id)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.photo_name = photo_name;
        this.price = price;
        this.start_date = start_date;
        this.end_date = end_date;
        this.auth_id = auth_id;
        this.win_id = win_id;
    }

    public Auction()
    {

    }

    static public Auction FromJsonToAuction(String JSON)
    {
        String[] separatedJSON = JSON.split("&:");
        return new Auction(separatedJSON[0],separatedJSON[1],separatedJSON[2],separatedJSON[3],separatedJSON[4],separatedJSON[5],
                separatedJSON[6],separatedJSON[7],separatedJSON[8]);
    }
    public String toString()
    {
        return id + "&:" + title + "&:" + description + "&:" + photo_name + "&:" + price+ "&:" + start_date + "&:" + end_date + "&:" + auth_id + "&:" + win_id;
    }

    public String getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }
    public String getEnd_date()
    {
        return end_date;
    }
    public String getPrice()
    {
        return price;
    }
    public String getAuth()
    {
        return auth_id;
    }
    public String getWinner()
    {
        return win_id;
    }
    public String getDescription()
    {
        return description;
    }
    public String getImage(){
        return photo_name;
    }

    public boolean isGoodDate()
    {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calobj = Calendar.getInstance();
            Date date_end = null;
            Date date_now = calobj.getTime();

            try {
                date_end = formatter.parse(this.end_date);


            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (date_end.before(date_now)) {
                return false;
            }
            return true;

    }


}
