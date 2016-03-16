package com.example.vidhi.calendar;

import java.sql.Date;

/**
 * Created by vidhi on 29-Feb-16.
 */
public class Invitations {
    private String id ;
    private String date ;
    private String venue ;
    private String occasion ;
    private String time;

    public Invitations() {
    }

    public Invitations(String id, String date,String time, String venue, String occasion) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.occasion = occasion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getOccasion() {
        return occasion;
    }

    public void setOccasion(String occasion) {
        this.occasion = occasion;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
