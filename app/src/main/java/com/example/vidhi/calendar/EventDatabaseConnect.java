package com.example.vidhi.calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class EventDatabaseConnect extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "calendarDb";

    private static final String TABLE_EVENTS = "EVENTS";


    public EventDatabaseConnect(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EVENTS_TABLE = "CREATE TABLE if not exists " + TABLE_EVENTS
                + "( EVENT_id  INTEGER PRIMARY KEY autoincrement, EVENT_venue  VARCHAR, EVENT_date "
                + " TEXT, EVENT_time TEXT, EVENT_occasion VARCHAR )";
        db.execSQL(CREATE_EVENTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);


        onCreate(db);
    }

    public void addInvitation(Invitations invitations) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("EVENT_date", invitations.getDate());
        values.put("EVENT_venue", invitations.getVenue());
        values.put("EVENT_occasion", invitations.getOccasion());
        values.put("EVENT_time",invitations.getTime());
        db.insert(TABLE_EVENTS, null, values);
        db.close();
    }

    // retrieve all meetings
    public List<Invitations> retrieveInvitations(String date) {

        List<Invitations> eventsList = new ArrayList<Invitations>();

        String query = "SELECT * FROM "+ TABLE_EVENTS+" WHERE EVENT_date = '"+date+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if(cursor!= null && cursor.moveToFirst()){
            do{
                Invitations invitations1 = new Invitations();
                invitations1.setDate(cursor.getString(2));
                invitations1.setTime(cursor.getString(3));
                invitations1.setOccasion(cursor.getString(4));
                invitations1.setVenue(cursor.getString(1));
                eventsList.add(invitations1);
            }while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return eventsList;
    }

    //retrieve one meeting
    public Invitations retrieveInvitation(String date) {


        String query = "SELECT * FROM "+ TABLE_EVENTS +" WHERE EVENT_date = '"+date+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if(null != cursor)
            cursor.moveToFirst();

        Invitations invitations1 = new Invitations(cursor.getString(0),cursor.getString(2),cursor.getString(3),cursor.getString(1),cursor.getString(4));
        db.close();
        return invitations1;
    }

    public int updateInvitation(Invitations invitation){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("EVENT_date", invitation.getDate());
        values.put("EVENT_venue", invitation.getVenue());
        values.put("EVENT_occasion", invitation.getOccasion());
        values.put("EVENT_time",invitation.getTime());
        return db.update(TABLE_EVENTS, values, "EVENT_id = ?",
                new String[]{String.valueOf(invitation.getId())});
    }

    public void deleteInvitation(Invitations invitation) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENTS, "EVENT_id = ?",
                new String[]{String.valueOf(invitation.getId())});
        db.close();
    }
}
