package com.example.vidhi.calendar;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Created by vidhi on 01-Mar-16.
 */
public class gasConnection {

    private String id ;
    private String date ;
    private String status;
    private String registrationNo;
    private String time;

    public gasConnection() {
    }

    public gasConnection(String id, String date, String time, String status, String registrationNo) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.status = status;
        this.registrationNo = registrationNo;
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

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }
}
