package com.example.vidhi.calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.LinearLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BankTransactionDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "calendarDb3";

    private static final String TABLE_BANK_TRANSACTION = "BANK_TRANSACTION";


    public BankTransactionDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BANK_TRANSACTION_TABLE = "CREATE TABLE if not exists " + TABLE_BANK_TRANSACTION
                + "( BANK_TRANSACTION_id  INTEGER PRIMARY KEY autoincrement, BANK_TRANSACTION_amount  VARCHAR, BANK_TRANSACTION_date "
                + " TEXT, BANK_TRANSACTION_time TEXT, BANK_TRANSACTION_balance VARCHAR, BANK_TRANSACTION_transacationId  VARCHAR, BANK_TRANSACTION_accountNo VARCHAR, flag VARCHAR )";
        db.execSQL(CREATE_BANK_TRANSACTION_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BANK_TRANSACTION);

        // Create tables again
        onCreate(db);
    }

    public void addTransaction(BankTransactions bank) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("BANK_TRANSACTION_date", bank.getDate());
        values.put("BANK_TRANSACTION_time",bank.getTime());
        values.put("BANK_TRANSACTION_transacationId", bank.getTransactionId());
        values.put("BANK_TRANSACTION_accountNo", bank.getAccountNumber());
        values.put("BANK_TRANSACTION_balance", bank.getBalance().toString());
        values.put("BANK_TRANSACTION_amount", bank.getAmount().toString());
        values.put("flag", bank.getFlag());
        // Inserting Row
        db.insert(TABLE_BANK_TRANSACTION, null, values);
        db.close(); // Closing database connection
    }

    public List<BankTransactions> retrieveTransaction(String date) {

        List<BankTransactions> bankTransactionsList = new ArrayList<BankTransactions>();

        String query = "SELECT * FROM "+ TABLE_BANK_TRANSACTION +" where BANK_TRANSACTION_date = "+date;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do{
                BankTransactions bank1 = new BankTransactions();
                bank1.setDate(cursor.getString(2));
                bank1.setTime(cursor.getString(3));
                bank1.setAmount(new BigDecimal(cursor.getString(1)));
                bank1.setBalance(new BigDecimal(cursor.getString(4)));
                bank1.setTransactionId(cursor.getString(5));
                bank1.setAccountNumber(cursor.getString(6));
                bank1.setFlag(cursor.getString(7));
                bankTransactionsList.add(bank1);
            }while (cursor.moveToNext());
        }
        db.close();
        return bankTransactionsList;
    }

    public int retrieveBANK_TRANSACTION(String date) {


        String query = "SELECT count(*) FROM "+ TABLE_BANK_TRANSACTION +" WHERE BANK_TRANSACTION_date like '"+date+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int temp = 0;
        if(null != cursor && cursor.moveToFirst()) {
            temp = Integer.parseInt(cursor.getString(0));
            cursor.close();
        }
        //        BankTransactions bank = new BankTransactions(cursor.getString(0),cursor.getString(2),cursor.getString(3),
//                new BigDecimal(cursor.getString(4)),
//                new BigDecimal(cursor.getString(1)),
//                cursor.getString(5),cursor.getString(6));

        db.close();
        return temp;
    }
    public String retrieveBalance(String accNo) {


        String query = "SELECT BANK_TRANSACTION_balance FROM "+ TABLE_BANK_TRANSACTION +" WHERE BANK_TRANSACTION_accountNo = '"+accNo+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String temp = "";
        if(null != cursor && cursor.moveToFirst()) {

            temp = cursor.getString(0);
            cursor.close();
        }

        db.close();
        return temp;
    }
    public List<String> retrieveAccnumbers() {

        List<String> temp = new ArrayList<>();
        String query = "SELECT distinct BANK_TRANSACTION_accountNo FROM "+ TABLE_BANK_TRANSACTION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(null != cursor && cursor.moveToFirst()) {
            cursor.moveToFirst();

            do{
                temp.add(cursor.getString(0));
            }while (cursor.moveToNext());
           cursor.close();
        }
        db.close();
        return temp;
    }
    public int updateBANK_TRANSACATION(BankTransactions bank){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("BANK_TRANSACTION_time",bank.getTime());
        values.put("BANK_TRANSACTION_date", bank.getDate());
        values.put("BANK_TRANSACTION_accountNo", bank.getAccountNumber());
        values.put("BANK_TRANSACTION_transacationId", bank.getTransactionId());
        values.put("BANK_TRANSACTION_balance",bank.getBalance().toString());
        values.put("BANK_TRANSACTION_amount", bank.getAmount().toString());
        return db.update(TABLE_BANK_TRANSACTION, values, "BANK_TRANSACTION_id = ?",
                new String[]{String.valueOf(bank.getId())});
    }

    public void deleteBANK_TRANSACTION(BankTransactions bank) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BANK_TRANSACTION, "BANK_TRANSACTION_id = ?",
                new String[]{String.valueOf(bank.getId())});
        db.close();
    }


}
