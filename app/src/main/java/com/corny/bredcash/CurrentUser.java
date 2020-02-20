package com.corny.bredcash;

import androidx.annotation.NonNull;

import java.util.Date;

public class CurrentUser
{
    private static String nick;
    private static int amount;
    private static int auctions;
    private static String join_date;
    private static int licznik = 0;

    public CurrentUser(String nick, int amount, int auctions, String Join_date){

                this.nick = nick;
                this.amount = amount;
                this.auctions = auctions;
                this.join_date = Join_date;
                licznik++;

    }
    public CurrentUser()
    {

    }

    public static void increaseMoney(int money)
    {
        amount+=money;
    }

    public static String getNick()
    {
        return nick;
    }

    public static int getAmount()
    {
        return amount;
    }
    public static int getAuctions()
    {
        return auctions;
    }
    public static String getJoinDate()
    {
        return join_date;
    }
    public static void decreaseAmount(int money)
    {
        amount = amount-money;
    }



}
