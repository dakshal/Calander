package com.example.vidhi.calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PhoneTopupsDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "calendarDb4";

    private static final String TABLE_Phone_topup = "Phone_topup";


    public PhoneTopupsDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_Phone_topup_TABLE = "CREATE TABLE if not exists " + TABLE_Phone_topup
                + "( Phone_topup_id  INTEGER PRIMARY KEY autoincrement, Phone_topup_amount  VARCHAR, Phone_topup_date "
                + " TEXT, Phone_topup_time TEXT, Phone_topup_balance VARCHAR, Phone_topup_type VARCHAR)";
        db.execSQL(CREATE_Phone_topup_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Phone_topup);

        onCreate(db);
    }
    public void addPhone_topup(phoneTopUps phone) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Phone_topup_time",phone.getTime());
        values.put("Phone_topup_date", phone.getDate());
        values.put("Phone_topup_balance", phone.getBalance().toString());
        values.put("Phone_topup_amount",phone.getAmount().toString());
        values.put("Phone_topup_type",phone.getType());
        db.insert(TABLE_Phone_topup, null, values);
        db.close();
    }

    public List<phoneTopUps> retrievePhone_topupToday(String date) {

        List<phoneTopUps> phoneTopUpsList = new ArrayList<phoneTopUps>();

        String query = "SELECT * FROM "+ TABLE_Phone_topup +" where Phone_topup_date ="+date;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do{
                phoneTopUps topUp = new phoneTopUps();
                topUp.setDate(cursor.getString(2));
                topUp.setTime(cursor.getString(3));
                topUp.setAmount(new BigDecimal(cursor.getString(1)));
                topUp.setBalance(new BigDecimal(cursor.getString(4)));
                topUp.setType(cursor.getString(5));
                phoneTopUpsList.add(topUp);
            }while (cursor.moveToNext());
        }
        db.close();
        return phoneTopUpsList;
    }

    public String retrievePhone_topup(String date) {


        String query = "SELECT sum(cast(Phone_topup_amount as decimal(4,2))) FROM "+ TABLE_Phone_topup +" WHERE Phone_topup_date like '"+date+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String ans = "";
        if(null != cursor && cursor.moveToFirst()) {

            ans = cursor.getString(0);
            cursor.close();
        }
//        phoneTopUps topUps = new phoneTopUps(cursor.getString(0),cursor.getString(2),cursor.getString(3),
//                new BigDecimal(cursor.getString(4)),
//                new BigDecimal(cursor.getString(1)),cursor.getString(5));
        db.close();
        return ans;
    }

    public int updatePhone_topup(phoneTopUps topUps){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Phone_topup_time",topUps.getTime());
        values.put("Phone_topup_date", topUps.getDate());
        values.put("Phone_topup_type", topUps.getType());
        values.put("Phone_topup_balance",topUps.getBalance().toString());
        values.put("Phone_topup_amount", topUps.getAmount().toString());
        return db.update(TABLE_Phone_topup, values, "Phone_topup_id = ?",
                new String[]{String.valueOf(topUps.getId())});
    }

    public void deletePhone_topup(BankTransactions bank) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Phone_topup, "Phone_topup_id = ?",
                new String[]{String.valueOf(bank.getId())});
        db.close();
    }
public BigDecimal getAmount(String startDate, String endDate ){
    String query = "SELECT sum(amount) FROM "+ TABLE_Phone_topup +" WHERE Phone_topup_date BETWEEN '"+startDate+"' to '"+endDate+"'" ;

    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(query,null);

    if(null != cursor)
        cursor.moveToFirst();

//    phoneTopUps topUps = new phoneTopUps(cursor.getString(0),cursor.getString(2),cursor.getString(3),
//            new BigDecimal(cursor.getString(4)),
//            new BigDecimal(cursor.getString(1)),cursor.getString(5));
    db.close();
    return new BigDecimal(cursor.getString(0));
}


}
