package com.example.m_qui.msquigle_subbook;

import java.util.Date;

/**
 * Created by m_qui on 2/3/2018.
 */

public class Subscription {
    private String name;
    private Date startdate;
    private int charge;
    private String comment;

    Subscription(String name, Date startdate, int charge) {

        this.name = name;
        this.startdate = startdate;
        this.charge = charge;
        this.comment = "";
    }

    Subscription(String name, Date startdate, int charge, String comment) {

        this.name = name;
        this.startdate = startdate;
        this.charge = charge;
        this.comment = comment;
    }

    public String getName() {

        return this.name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public Date getDate() {

        return this.startdate;
    }

    public void setDate(Date date) {

        this.startdate = date;
    }

    public int getCharge() {

        return this.charge;
    }

    public void setCharge(int charge) {

        this.charge = charge;
    }

    public String getComment() {

        return this.comment;
    }

    public void setComment(String comment) {

        this.comment = comment;
    }

}
