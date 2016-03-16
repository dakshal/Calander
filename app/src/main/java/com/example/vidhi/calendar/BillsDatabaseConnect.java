package com.example.vidhi.calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class BillsDatabaseConnect extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "calendarDb6";

    private static final String TABLE_bills = "bills";


    public BillsDatabaseConnect(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_bills_TABLE = "CREATE TABLE if not exists " + TABLE_bills
                + "( bills_id  INTEGER PRIMARY KEY autoincrement, bills_amount  VARCHAR, bills_date "
                + " TEXT, bills_status VARCHAR, bills_type VARCHAR, bills_dueDate VARCHAR, bills_noOfDaysLeft VARCHAR)";
        db.execSQL(CREATE_bills_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_bills);

        // Create tables again
        onCreate(db);
    }

    public void addBill(Bills bills) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("bills_date", bills.getDate());
        values.put("bills_status", bills.getStatus());
        values.put("bills_type", bills.getBillType());
        values.put("bills_amount",bills.getBillAmount().toString());
        values.put("bills_dueDate", bills.getDueDate());
        values.put("bills_noOfDaysLeft", bills.getNoOfDaysLeft());

        // Inserting Row
        db.insert(TABLE_bills, null, values);
        db.close(); // Closing database connection
    }

    public List<Bills> retrieveBills(int date,String billDate) {

        List<Bills> billsList = new ArrayList<Bills>();

        String query = "SELECT * FROM "+ TABLE_bills+" WHERE bills_date like '"+billDate+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if(cursor!= null &&cursor.moveToFirst()){
            do{
                if(cursor.getString(3).equalsIgnoreCase("pending"))
                if(cursor.getString(2).endsWith("-"+date) || date < Integer.parseInt(cursor.getString(2).substring(7, cursor.getString(2).length() - 1))) {
                    Bills bills1 = new Bills();
                    bills1.setDate(cursor.getString(2));
                    bills1.setStatus(cursor.getString(3));
                    bills1.setBillAmount(new BigDecimal(cursor.getString(1)));
                    bills1.setBillType(cursor.getString(4));
                    bills1.setDueDate(cursor.getString(5));
                    int noOfDayes = Integer.parseInt(cursor.getString(2).substring(7,cursor.getString(2).length()-1)) - date;
                    bills1.setNoOfDaysLeft(String.valueOf(noOfDayes));
                    billsList.add(bills1);
                }
            }while (cursor.moveToNext());
        }
        db.close();
        return billsList;
    }

    public Bills retrieveBill(String date) {


        String query = "SELECT * FROM "+ TABLE_bills +" WHERE bills_date = '"+date+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if(null != cursor)
            cursor.moveToFirst();

        Bills bills1 = new Bills(cursor.getString(3),cursor.getString(6),cursor.getString(2),cursor.getString(4),
                new BigDecimal(cursor.getString(1)),
                cursor.getString(5),cursor.getString(0));
        db.close();
        return bills1;
    }

    public int updateBill(Bills bill){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("bills_date", bill.getDate());
        values.put("bills_noOfDaysLeft", bill.getNoOfDaysLeft());
        values.put("bills_dueDate", bill.getDueDate());
        values.put("bills_amount",bill.getBillAmount().toString());
        values.put("bills_status",bill.getStatus());
        values.put("bills_type", bill.getBillType());
        return db.update(TABLE_bills, values, "bills_id = ?",
                new String[]{String.valueOf(bill.getId())});
    }

    // Deleting single contact
    public void deleteBill(Bills bill) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_bills, "bills_id = ?",
                new String[]{String.valueOf(bill.getId())});
        db.close();
    }

    public List<Bills> retrieveBillsForToday(String date) {

        List<Bills> billsList = new ArrayList<Bills>();

        String query = "SELECT * FROM "+ TABLE_bills+" WHERE bills_date is '"+date+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if(cursor!= null &&cursor.moveToFirst()){
            do{
                        Bills bills1 = new Bills();
                        bills1.setDate(cursor.getString(2));
                        bills1.setStatus(cursor.getString(3));
                        bills1.setBillAmount(new BigDecimal(cursor.getString(1)));
                        bills1.setBillType(cursor.getString(4));
                        bills1.setDueDate(cursor.getString(5));
                        bills1.setNoOfDaysLeft(String.valueOf(cursor.getString(6)));
                        billsList.add(bills1);

            }while (cursor.moveToNext());
        }
        db.close();
        return billsList;
    }
}
