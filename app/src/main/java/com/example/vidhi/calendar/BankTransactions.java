package com.example.vidhi.calendar;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Created by vidhi on 01-Mar-16.
 */
public class BankTransactions {

    private String id ;
    private String date ;
    private String time;
    private BigDecimal balance ;
    private BigDecimal amount ;
    private String transactionId;
    private String accountNumber;
    private String flag;

    public BankTransactions() {
    }

    public BankTransactions(String id, String date,String time, BigDecimal balance,
                            BigDecimal amount, String transactionId, String accountNumber,String flag) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.balance = balance;
        this.amount = amount;
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.flag = flag;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
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

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
