package com.example.mad_project;

//import java.sql.Timestamp;

import com.google.firebase.Timestamp;

public class stock {
    String company,billno,item,inout;
    Long quantity;
    Timestamp date;
    public stock()
    {

    }
    public stock(String c,String b,Long q,String i,Timestamp d,String in)
    {
        this.company = c;
        this.billno = b;
        this.date = d;
        this.inout = in;
        this.quantity = q;
        this.item = i;
    }

    /*public String getData()
    {
        String s = this.company+this.item+this.quantity+this.billno+this.inout+this.inout+this.date.toString();
        return s;
    }*/
    public String getCompany()
    {
        return company;
    }
    public String getBillno()
    {
        return billno;
    }

    public String getItem()
    {
        return item;
    }


    public String getInout()
    {
        return inout;
    }



}
