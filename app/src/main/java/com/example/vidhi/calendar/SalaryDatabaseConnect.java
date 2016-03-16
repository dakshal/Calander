package com.example.vidhi.calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SalaryDatabaseConnect extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "calendarDb2";

    private static final String TABLE_SALARY = "SALARY";


    public SalaryDatabaseConnect(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SALARY_TABLE = "CREATE TABLE if not exists " + TABLE_SALARY
                + "( SALARY_id  INTEGER PRIMARY KEY autoincrement, SALARY_amount  VARCHAR, SALARY_date "
                + " TEXT, SALARY_time TEXT, SALARY_balance VARCHAR )";
        db.execSQL(CREATE_SALARY_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SALARY);

        onCreate(db);
    }
    public void addSalary(Salary salary) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("SALARY_date", salary.getDate());
        values.put("SALARY_time",salary.getTime());
        values.put("SALARY_balance", salary.getBalance().toString());
        values.put("SALARY_amount", salary.getAmount().toString());
        db.insert(TABLE_SALARY, null, values);
        db.close();
    }

    public List<Salary> retrieveSalaryToday(String date) {

        List<Salary> salaryList = new ArrayList<Salary>();

        String query = "SELECT * FROM "+ TABLE_SALARY+" where SALARY_date = "+date;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do{
                Salary salary = new Salary();
                salary.setDate(cursor.getString(2));
                salary.setTime(cursor.getString(3));
                salary.setAmount(new BigDecimal(cursor.getString(1)));
                salary.setBalance(new BigDecimal(cursor.getString(4)));
                salaryList.add(salary);
            }while (cursor.moveToNext());
        }
        db.close();
        return salaryList;
    }

    public String retrieveSalary(String date) {


        String query = "SELECT * FROM "+ TABLE_SALARY +" WHERE SALARY_date like '"+date+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String ans ="";
        if(null != cursor && cursor.moveToFirst()) {
            ans = "Your salary with amount " + cursor.getString(1) + " is credited on " + cursor.getString(2);
            cursor.close();
        }
        db.close();
        return ans;
    }

    public int updateSalary(Salary salary){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("SALARY_time",salary.getTime());
        values.put("SALARY_date", salary.getDate());
        values.put("SALARY_balance",salary.getBalance().toString());
        values.put("SALARY_amount", salary.getAmount().toString());
        return db.update(TABLE_SALARY, values, "SALARY_id = ?",
                new String[]{String.valueOf(salary.getId())});
    }

    public void deleteSalary(Salary salary) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SALARY, "SALARY_id = ?",
                new String[]{String.valueOf(salary.getId())});
        db.close();
    }

}
