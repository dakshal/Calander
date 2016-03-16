package com.example.vidhi.calendar;

/**
 * Created by vidhi on 29-Feb-16.
 */
public class Meetings {

    private String id ;
    private String date ;
    private String time;
    private String venue ;
    private String topic ;

    public Meetings() {
    }

    public Meetings(String topic, String venue, String date,String time, String KEY_ID) {
        this.topic = topic;
        this.venue = venue;
        this.date = date;
        this.time = time;
        this.id = KEY_ID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String Id) {
        this.id = Id;
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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
