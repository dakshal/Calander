package com.example.vidhi.calendar;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Created by vidhi on 01-Mar-16.
 */
public class Salary {

    private String id ;
    private String date ;
    private BigDecimal balance ;
    private BigDecimal amount ;
    private String time;
    public Salary() {
    }

    public Salary(String id, String date, String time,BigDecimal balance, BigDecimal amount) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.balance = balance;
        this.amount = amount;
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
