package com.example.mad_project;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Processing {
    private final String item;
    private final Long quantity;
    private final Long rejection;
    private final Date date;
    private final String startTime;
    private final String endTime;

    public Processing (String item, Long quantity, Long rejection, Date date, String startTime, String endTime)
    {
        this.item = item;
        this.quantity = quantity;
        this.rejection = rejection;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getItem() {
        return item;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Long getRejection() {
        return rejection;
    }

    public Date getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}

