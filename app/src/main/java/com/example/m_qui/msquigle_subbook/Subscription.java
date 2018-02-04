package com.example.m_qui.msquigle_subbook;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by m_qui on 2/3/2018.
 */

public class Subscription {

    private String name;
    private Date date;
    private double charge;
    private String comment;

    public Subscription(String name, Date date, double charge) {

        this.name = name;
        this.date = date;
        this.charge = charge;
    }

    public Subscription(String name, Date date, double charge, String comment) {

        this.name = name;
        this.date = date;
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

        return this.date;
    }

    public void setDate(Date date) {

        this.date = date;
    }

    public double getCharge() {

        return this.charge;
    }

    public void setCharge(double charge) {

        this.charge = charge;
    }

    public String getComment() {

        return this.comment;
    }

    public void setComment(String comment) {

        this.comment = comment;
    }

    public String toString() {

        String dateString = date.toString().substring(4, 10) + date.toString().substring(23);

        String newString = this.name + ": " + String.format(Locale.CANADA,"%.2f", this.charge) + "\n"
                + dateString + "\n" + this.comment;

        return newString;
    }

}
