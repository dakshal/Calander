package com.example.vidhi.calendar;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Created by vidhi on 01-Mar-16.
 */
public class Bills {

    private String id ;
    private String date ;
    private String billType ;
    private BigDecimal billAmount ;
    private String dueDate;
    private String noOfDaysLeft;
    private String status;


    public Bills() {
    }

    public Bills(String status, String noOfDaysLeft, String date, String billType, BigDecimal billAmount, String dueDate, String id) {
        this.status = status;
        this.noOfDaysLeft = noOfDaysLeft;
        this.date = date;
        this.billType = billType;
        this.billAmount = billAmount;
        this.dueDate = dueDate;
        this.id = id;
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

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public BigDecimal getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(BigDecimal billAmount) {
        this.billAmount = billAmount;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getNoOfDaysLeft() {
        return noOfDaysLeft;
    }

    public void setNoOfDaysLeft(String noOfDaysLeft) {
        this.noOfDaysLeft = noOfDaysLeft;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
