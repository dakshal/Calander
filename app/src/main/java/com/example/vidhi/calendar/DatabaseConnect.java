

package com.example.vidhi.calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DatabaseConnect extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "calendarDb1";

    private static final String TABLE_MEETINGS = "meetings";


    public DatabaseConnect(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MEETINGS_TABLE = "CREATE TABLE if not exists " + TABLE_MEETINGS
                + "( meeting_id  INTEGER PRIMARY KEY autoincrement, meeting_venue  VARCHAR, meeting_date "
                + " TEXT, meeting_time TEXT, meeting_topic VARCHAR )";
        db.execSQL(CREATE_MEETINGS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEETINGS);

        onCreate(db);
    }
    public void addMeeting(Meetings meetings) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("meeting_time",meetings.getTime());
        values.put("meeting_date", meetings.getDate());
        values.put("meeting_venue", meetings.getVenue());
        values.put("meeting_topic", meetings.getTopic());
        db.insert(TABLE_MEETINGS, null, values);
        db.close();
    }

    public List<Meetings> retrieveMeetings(String date) {

        List<Meetings> meetingsList = new ArrayList<Meetings>();

        String query = "SELECT * FROM "+ TABLE_MEETINGS +" WHERE meeting_date = '"+date+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if(cursor!= null && cursor.moveToFirst()){
            do{
                Meetings meetings1 = new Meetings();
                meetings1.setTime(cursor.getString(3));
                meetings1.setDate(cursor.getString(2));
                meetings1.setTopic(cursor.getString(4));
                meetings1.setVenue(cursor.getString(1));
                meetingsList.add(meetings1);
            }while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return meetingsList;
    }

    //retrieve one meeting
    public Meetings retrieveMeeting(String date) {


        String query = "SELECT * FROM "+ TABLE_MEETINGS +" WHERE meeting_date = '"+date+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if(null != cursor)
            cursor.moveToFirst();

        Meetings meetings1 = new Meetings(cursor.getString(4),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(0));
        meetings1.setDate(meetings1.getDate());
        meetings1.setTopic(meetings1.getTopic());
        meetings1.setVenue(meetings1.getVenue());

        db.close();
        return meetings1;
    }

    public int updateMeeting(Meetings meeting){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("meeting_date", meeting.getDate());
        values.put("meeting_venue", meeting.getVenue());
        values.put("meeting_topic", meeting.getTopic());
        values.put("meeting_time",meeting.getTime());
        return db.update(TABLE_MEETINGS, values, "meeting_id = ?",
                new String[]{String.valueOf(meeting.getId())});
    }

    // Deleting single contact
    public void deleteMeeting(Meetings meeting) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEETINGS, "meeting_id = ?",
                new String[]{String.valueOf(meeting.getId())});
        db.close();
    }
}
