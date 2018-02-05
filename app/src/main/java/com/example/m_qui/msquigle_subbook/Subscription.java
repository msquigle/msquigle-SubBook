/*
 * AddSubscriptionActivity
 *
 * Feb 2, 2018
 */

package com.example.m_qui.msquigle_subbook;

import java.util.Date;
import java.util.Locale;

/**
 * Purpose: Maintain information about
 * a given subscription
 * Design: Simple container for the data that
 * is relevant to a subscription
 * Issues: When using a DateFormat to format the string the subscription
 * was always a null object reference, this may be
 * related to the improper JSON load as discussed
 * in the MainActivity class. A fix would allow
 * for less hard-coding which part of the string
 * for the date to display
 * @author Matthew Quigley
 */
public class Subscription {

    private String name;
    private Date date;
    private double charge;
    private String comment;

    /**
     * @param name The name of the subscription
     * @param date The date the subscription began
     * @param charge The monthly cost of the subscription
     */
    public Subscription(String name, Date date, double charge) {

        this.name = name;
        this.date = date;
        this.charge = charge;
    }

    /**
     * @param name The name of the subscription
     * @param date The date the subscription began
     * @param charge The monthly cost of the subscription
     * @param comment Additional comments about the subscription
     */
    public Subscription(String name, Date date, double charge, String comment) {

        this.name = name;
        this.date = date;
        this.charge = charge;
        this.comment = comment;
    }

    /**
     * @return The name of the subscription
     */
    public String getName() {

        return this.name;
    }

    /**
     * @param name The name of the subscription
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * @return The date the subscription began
     */
    public Date getDate() {

        return this.date;
    }

    /**
     * @param date The date the subscription began
     */
    public void setDate(Date date) {

        this.date = date;
    }

    /**
     * @return The monthly cost of the subscription
     */
    public double getCharge() {

        return this.charge;
    }

    /**
     * @param charge The monthly cost of the subscription
     */
    public void setCharge(double charge) {

        this.charge = charge;
    }

    /**
     * @return The additional comment about the subscription
     */
    public String getComment() {

        return this.comment;
    }

    /**
     * @param comment The additional comment about the subscription
     */
    public void setComment(String comment) {

        this.comment = comment;
    }

    /**
     * @return A formatted string for displaying information about the subscription
     */
    public String toString() {

        String dateString = date.toString().substring(4, 10) + "," + date.toString().substring(23);

        String newString = this.name + ": " + String.format(Locale.CANADA,"%.2f", this.charge) + "\n"
                + "Date Started: " + dateString + "\n"
                + this.comment;

        return newString;
    }

}
