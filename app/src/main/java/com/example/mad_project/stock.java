package com.example.mad_project;

//import java.sql.Timestamp;

import com.google.firebase.Timestamp;

public class stock {
    String company,billno,item,date1,inout;
    Long quantity;
    Timestamp date;
    public stock()
    {

    }
    public stock(String c,String b,Long q,String i,String d,String in)
    {
        this.company = c;
        this.billno = b;
        this.date1 = d;
        this.inout = in;
        this.quantity = q;
        this.item = i;
    }

    public String getData()
    {
        String s = this.company+this.item+this.quantity+this.billno+this.inout+this.inout+this.date.toString();
        return s;
    }


}
