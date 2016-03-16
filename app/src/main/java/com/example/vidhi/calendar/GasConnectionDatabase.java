package com.example.vidhi.calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vidhi on 02-Mar-16.
 */
public class GasConnectionDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "calendarDb5";

    private static final String TABLE_GASCONNECTION = "GasConnection";


    public GasConnectionDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_GASCONNECTION_TABLE = "CREATE TABLE if not exists " + TABLE_GASCONNECTION
                + "( GASCONNECTION_id  INTEGER PRIMARY KEY autoincrement, GASCONNECTION_registrationNo  VARCHAR, " +
                "GASCONNECTION_date  TEXT, GASCONNECTION_time TEXT, GASCONNECTION_status VARCHAR )";
        db.execSQL(CREATE_GASCONNECTION_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GASCONNECTION);

        // Create tables again
        onCreate(db);
    }
    public void addGasConnection(gasConnection connection) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("GASCONNECTION_time",connection.getTime());
        values.put("GASCONNECTION_date", connection.getDate());
        values.put("GASCONNECTION_registrationNo", connection.getRegistrationNo());
        values.put("GASCONNECTION_status", connection.getStatus());

        // Inserting Row
        db.insert(TABLE_GASCONNECTION, null, values);
        db.close(); // Closing database connection
    }

    // retrieve all meetings
    public List<gasConnection> retrieveConnection(int date,String billDate) {

        List<gasConnection> gasConnectionList = new ArrayList<gasConnection>();

        String query = "SELECT * FROM "+ TABLE_GASCONNECTION +" WHERE GASCONNECTION_date like '"+billDate+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if(cursor!= null && cursor.moveToFirst()){
            do{
                if(cursor.getString(4).equalsIgnoreCase("pending")) {
                    if(cursor.getString(2).endsWith("-" + date) || date < Integer.parseInt(cursor.getString(2).substring(7, cursor.getString(2).length() - 1))) {
                        gasConnection connection1 = new gasConnection();
                        connection1.setDate(cursor.getString(2));
                        connection1.setTime(cursor.getString(3));
                        connection1.setStatus(cursor.getString(4));
                        connection1.setRegistrationNo(cursor.getString(1));
                        gasConnectionList.add(connection1);
                    }
                }
            }while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return gasConnectionList;
    }

    public gasConnection retrieveGASCONNECTION(String date) {


        String query = "SELECT * FROM "+ TABLE_GASCONNECTION +" WHERE GASCONNECTION_date = '"+date+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if(null != cursor)
            cursor.moveToFirst();

        gasConnection connection = new gasConnection(cursor.getString(0),cursor.getString(2),cursor.getString(3),
                cursor.getString(4),
                cursor.getString(1));
        db.close();
        return connection;
    }

    public int updateGASCONNECTION(gasConnection connection){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("GASCONNECTION_time",connection.getTime());
        values.put("GASCONNECTION_date", connection.getDate());
        values.put("GASCONNECTION_registrationNo", connection.getRegistrationNo());
        values.put("GASCONNECTION_status", connection.getStatus());
        return db.update(TABLE_GASCONNECTION, values, "GASCONNECTION_id = ?",
                new String[]{String.valueOf(connection.getId())});
    }

    public void deleteGASCONNECTION(gasConnection connection) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GASCONNECTION, "GASCONNECTION_id = ?",
                new String[]{String.valueOf(connection.getId())});
        db.close();
    }

    public List<gasConnection> retrieveConnectionToday(String date) {
        List<gasConnection> gasConnectionList = new ArrayList<gasConnection>();

        String query = "SELECT * FROM "+ TABLE_GASCONNECTION +" WHERE GASCONNECTION_date is '"+date+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if(cursor!= null && cursor.moveToFirst()){
            do{
                        gasConnection connection1 = new gasConnection();
                        connection1.setDate(cursor.getString(2));
                        connection1.setTime(cursor.getString(3));
                        connection1.setStatus(cursor.getString(4));
                        connection1.setRegistrationNo(cursor.getString(1));
                        gasConnectionList.add(connection1);

            }while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return gasConnectionList;
    }
}
