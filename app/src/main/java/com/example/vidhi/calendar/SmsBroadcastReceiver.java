package com.example.vidhi.calendar;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    public static final String SMS_BUNDLE = "pdus";
    private String baseUri;
    private Context context;

    public void setContext(Context context) {
        this.context = context;
        this.baseUri = getCalendarUriBase();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //android.os.Debug.waitForDebugger();
        Bundle intentExtras = intent.getExtras();
        Log.i("android", "intentExtras" + intentExtras);
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);

            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                String smsBody = smsMessage.getMessageBody().toString();

                Log.i("smsbody", "msggggg" + smsMessage.getMessageBody());

                smsBody = smsBody.toUpperCase();

                Log.i("upper", "case" + smsBody);
                try {
                    if (smsBody.contains("OCCASION")) {
                        String occasion = smsBody.substring(smsBody.indexOf("OCCASION :")+10, smsBody.indexOf("DATE :"));
                        String stime = smsBody.substring(smsBody.indexOf("DATE : ")+7, smsBody.indexOf(","));
                        String etime = smsBody.substring(smsBody.indexOf("TIME : ") + 7, smsBody.indexOf("."));
                        String venue = smsBody.substring(smsBody.indexOf("VENUE : ") + 8, smsBody.indexOf("!"));


                        String[] start = stime.split("-");
                        String[] time = etime.split("TO ");
                        String[] t1= time[0].split(":");
                        EventDatabaseConnect dbConnect = new EventDatabaseConnect(context);
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
                        Calendar beginTime = Calendar.getInstance();

                        beginTime.set(Integer.parseInt(start[0]), Integer.parseInt(start[1]) - 1,
                                Integer.parseInt(start[2]), Integer.parseInt(t1[0]),
                                Integer.parseInt(t1[1].substring(0, t1[1].length() - 2)));

                        Calendar endTime = Calendar.getInstance();
                        String[] end = time[1].split(":");
                        endTime.set(Integer.parseInt(start[0]), Integer.parseInt(start[1]) - 1,
                                Integer.parseInt(start[2]), Integer.parseInt(end[0]),
                                Integer.parseInt(end[1]));
                        dbConnect.addInvitation(new Invitations("1", stime, etime, venue, occasion));

                        ContentValues cv = new ContentValues();
                        cv.put(CalendarContract.Events.CALENDAR_ID, "1");
                        cv.put(CalendarContract.Events.TITLE, occasion);
                        cv.put(CalendarContract.Events.EVENT_LOCATION, venue);

                        cv.put(CalendarContract.Events.DTSTART, beginTime.getTimeInMillis());
                        cv.put(CalendarContract.Events.DTEND, endTime.getTimeInMillis());

                        cv.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
                        ContentResolver cr = context.getContentResolver();
                        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, cv);

                        System.out.println("Event URI ["+uri+"]");
                        calendar ui = calendar.instance();
                        String temp = String.valueOf(smsMessage.getOriginatingAddress().charAt(0));
                        Response res = new Response("Reminder is created for the  "+smsMessage.getOriginatingAddress()+"'s msg",temp);
                        ui.updateList(res);
                        Toast.makeText(context, "Reminder is created from the "+smsMessage.getOriginatingAddress()+"'s msg", Toast.LENGTH_SHORT).show();
                    } else if (smsBody.contains("MEETING")) {

                        String topic = smsBody.substring(smsBody.indexOf("TOPIC : ")+7, smsBody.indexOf("DATE :"));
                        String stime = smsBody.substring(smsBody.indexOf("DATE : ")+7, smsBody.indexOf(","));
                        String etime = smsBody.substring(smsBody.indexOf("TIME : ") + 7, smsBody.indexOf("."));
                        String venue = smsBody.substring(smsBody.indexOf("VENUE : ") + 8, smsBody.indexOf("!"));


                        String[] start = stime.split("-");
                        String[] time = etime.split("TO ");
                        String[] t1= time[0].split(":");

                        DatabaseConnect dbConnect = new DatabaseConnect(context);
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
                        Calendar beginTime = Calendar.getInstance();

                        beginTime.set(Integer.parseInt(start[0]), Integer.parseInt(start[1])-1,
                                Integer.parseInt(start[2]), Integer.parseInt(t1[0]),
                                Integer.parseInt(t1[1].substring(0,t1[1].length()-2)));

                        Calendar endTime = Calendar.getInstance();

                        String[] end = time[1].split(":");
                        endTime.set(Integer.parseInt(start[0]), Integer.parseInt(start[1]) - 1,
                                Integer.parseInt(start[2]), Integer.parseInt(end[0]),
                                Integer.parseInt(end[1]));
                        dbConnect.addMeeting(new Meetings(topic, venue, stime, etime, "1"));

                        ContentValues cv = new ContentValues();
                        cv.put(CalendarContract.Events.CALENDAR_ID, "1");
                        cv.put(CalendarContract.Events.TITLE, topic);
                        cv.put(CalendarContract.Events.EVENT_LOCATION, venue);

                        cv.put(CalendarContract.Events.DTSTART, beginTime.getTimeInMillis());
                        cv.put(CalendarContract.Events.DTEND, endTime.getTimeInMillis());

                        cv.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

                        ContentResolver cr = context.getContentResolver();
                        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, cv);
                        System.out.println("Event URI [" + uri + "]");
                        calendar ui = calendar.instance();
                        String temp = String.valueOf(smsMessage.getOriginatingAddress().charAt(0));
                        Response res = new Response("Reminder is created for the  "+smsMessage.getOriginatingAddress()+"'s msg",temp);
                        ui.updateList(res);
                        Toast.makeText(context, "Reminder is created from the "+smsMessage.getOriginatingAddress()+"'s msg", Toast.LENGTH_SHORT).show();
                    } else if (smsBody.contains("BILL")) {

                        String type = smsBody.substring(smsBody.indexOf("BILL TYPE : ")+12,smsBody.indexOf("DATE : "));
                        String stime = smsBody.substring(smsBody.indexOf("DATE : ") + 7, smsBody.indexOf(","));
                        String etime = smsBody.substring(smsBody.indexOf("DUE DATE : ") + 11, smsBody.indexOf("."));
                        String amount = smsBody.substring(smsBody.indexOf("AMOUNT : ") + 9, smsBody.indexOf("INR") - 1);
                        String status = smsBody.substring(smsBody.indexOf("STATUS : ") + 9, smsBody.indexOf("VERIFY") - 1);
                        String[] start = stime.split("-");


                        String[] end = stime.split("-");

                        BillsDatabaseConnect dbConnect = new BillsDatabaseConnect(context);
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
                        Calendar beginTime = Calendar.getInstance();

                        beginTime.set(Integer.parseInt(start[0]), Integer.parseInt(start[1]) - 1,
                                Integer.parseInt(start[2]), 10, 00);
                        Calendar endTime = Calendar.getInstance();
                        endTime.set(Integer.parseInt(end[0]), Integer.parseInt(end[1]) - 1,
                                Integer.parseInt(end[2]), 10, 00);
                        ContentValues cv = new ContentValues();
                        cv.put(CalendarContract.Events.CALENDAR_ID, "1");
                        cv.put(CalendarContract.Events.TITLE, "BILL");

                        cv.put(CalendarContract.Events.DTSTART, endTime.getTimeInMillis());
                        cv.put(CalendarContract.Events.DTEND, endTime.getTimeInMillis());
                        beginTime.setTime(getDateTime(stime));

                        cv.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

                        endTime.setTime(getDateTime(etime));


                        Calendar sDate = getDatePart(getDateTime(stime));
                        Calendar eDate = getDatePart(getDateTime(etime));

                        long daysBetween = 0;
                        while (sDate.before(eDate)) {
                            sDate.add(Calendar.DAY_OF_MONTH, 1);
                            daysBetween++;
                        }
                        String noOfDaysLeft = String.valueOf(daysBetween);
                        System.out.print("days left is " + noOfDaysLeft);
                        dbConnect.addBill(new Bills(status, noOfDaysLeft, stime, type, new BigDecimal(amount), etime, "1"));
                        cv.put(CalendarContract.Events.DESCRIPTION, "your bill status is " + status + " no of days left : " + noOfDaysLeft);
                        ContentResolver cr = context.getContentResolver();
                        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, cv);
                        System.out.println("Event URI [" + uri + "]");
                        calendar ui = calendar.instance();
                        String temp = String.valueOf(smsMessage.getOriginatingAddress().charAt(0));
                        Response res = new Response("Reminder is created for the  "+smsMessage.getOriginatingAddress()+"'s msg",temp);
                        ui.updateList(res);
                        Toast.makeText(context, "Reminder is created from the "+smsMessage.getOriginatingAddress()+"'s msg", Toast.LENGTH_SHORT).show();
                    } else if (smsBody.contains("SALARY")) {

                        String stime = smsBody.substring(smsBody.indexOf("DATE : ")+7, smsBody.indexOf(","));
                        String etime = smsBody.substring(smsBody.indexOf("TIME : ") + 7, smsBody.indexOf("."));
                        String amount = smsBody.substring(smsBody.indexOf("AMOUNT : ") + 9, smsBody.indexOf("INR") -1);
                        String balance = smsBody.substring(smsBody.indexOf("BALANCE : ") + 10, smsBody.indexOf("RS") -1);

                        String[] start = stime.split("-");
                        String[] t1= etime.split(":");

                        SalaryDatabaseConnect dbConnect = new SalaryDatabaseConnect(context);
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
                        Calendar beginTime = Calendar.getInstance();

                        beginTime.set(Integer.parseInt(start[0]), Integer.parseInt(start[1]) - 1,
                                Integer.parseInt(start[2]), Integer.parseInt(t1[0]),
                                Integer.parseInt(t1[1]));

                        dbConnect.addSalary(new Salary("1", stime, etime, new BigDecimal(balance), new BigDecimal(amount)));

                        ContentValues cv = new ContentValues();
                        cv.put(CalendarContract.Events.CALENDAR_ID, "1");
                        cv.put(CalendarContract.Events.TITLE, "SALARY");
                        cv.put(CalendarContract.Events.DESCRIPTION,"salary amount is "+amount+" your balance is"+balance);
                        cv.put(CalendarContract.Events.DTSTART, beginTime.getTimeInMillis());
                        cv.put(CalendarContract.Events.DTEND, beginTime.getTimeInMillis());

                        cv.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

                        ContentResolver cr = context.getContentResolver();
                        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, cv);
                        System.out.println("Event URI [" + uri + "]");
                        calendar ui = calendar.instance();
                        String temp = String.valueOf(smsMessage.getOriginatingAddress().charAt(0));
                        Response res = new Response("Reminder is created for the  "+smsMessage.getOriginatingAddress()+"'s msg",temp);
                        ui.updateList(res);
                        Toast.makeText(context, "Reminder is created from the "+smsMessage.getOriginatingAddress()+"'s msg", Toast.LENGTH_SHORT).show();
                    } else if (smsBody.contains("BANK TRANSACTION")) {

                        String stime = smsBody.substring(smsBody.indexOf("DATE : ")+7, smsBody.indexOf(","));
                        String etime = smsBody.substring(smsBody.indexOf("TIME : ") + 7, smsBody.indexOf("."));
                        String amount = smsBody.substring(smsBody.indexOf("AMOUNT : ") + 9, smsBody.indexOf("INR") -1);
                        String balance = smsBody.substring(smsBody.indexOf("BALANCE : ") + 10, smsBody.indexOf("RS") -1);
                        String transactionId;
                        String flag = "true";
                        if(smsBody.contains("Withdrawed")) {
                            transactionId = smsBody.substring(smsBody.indexOf("TID : ") + 5, smsBody.indexOf("Withdrawed")-1);
                            flag = "false";
                        }
                        else {
                            transactionId = smsBody.substring(smsBody.indexOf("TID : ") + 5, smsBody.indexOf("Credited")-1);
                        }
                        String accNumber = smsBody.substring(smsBody.indexOf("ACCNO : ")+8);
                        String[] start = stime.split("-");
                        String[] t1= etime.split(":");

                        BankTransactionDatabase dbConnect = new BankTransactionDatabase(context);
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
                        Calendar beginTime = Calendar.getInstance();

                        beginTime.set(Integer.parseInt(start[0]), Integer.parseInt(start[1]) - 1,
                                Integer.parseInt(start[2]), Integer.parseInt(t1[0]),
                                Integer.parseInt(t1[1]));


                        dbConnect.addTransaction(new BankTransactions("1", stime, etime, new BigDecimal(balance), new BigDecimal(amount),transactionId,accNumber,flag));

                        ContentValues cv = new ContentValues();
                        cv.put(CalendarContract.Events.CALENDAR_ID, "1");
                        cv.put(CalendarContract.Events.TITLE, "BANK TRANSACTION");
                        cv.put(CalendarContract.Events.DESCRIPTION,"Bank Transaction with transacation number "+transactionId+" in account number"+accNumber
                        +" with the amount "+amount+" now your account balance is "+balance);
                        cv.put(CalendarContract.Events.DTSTART, beginTime.getTimeInMillis());
                        cv.put(CalendarContract.Events.DTEND, beginTime.getTimeInMillis());

                        cv.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

                        ContentResolver cr = context.getContentResolver();
                        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, cv);
                        System.out.println("Event URI [" + uri + "]");
                        calendar ui = calendar.instance();
                        String temp = String.valueOf(smsMessage.getOriginatingAddress().charAt(0));
                        Response res = new Response("Reminder is created for the  "+smsMessage.getOriginatingAddress()+"'s msg",temp);
                        ui.updateList(res);
                        Toast.makeText(context, "Reminder is created from the "+smsMessage.getOriginatingAddress()+"'s msg", Toast.LENGTH_SHORT).show();
                    } else if (smsBody.contains("PHONE TOP-UP")) {

                        String stime = smsBody.substring(smsBody.indexOf("DATE : ")+7, smsBody.indexOf(","));
                        String etime = smsBody.substring(smsBody.indexOf("TIME : ") + 7, smsBody.indexOf("."));
                        String amount = smsBody.substring(smsBody.indexOf("AMOUNT : ") + 9, smsBody.indexOf("INR") -1);
                        String balance = smsBody.substring(smsBody.indexOf("BALANCE : ") + 10, smsBody.indexOf("RS") -1);
                        String type = smsBody.substring(smsBody.indexOf("TYPE :"), smsBody.indexOf("VERIFY") - 1);
                        String[] start = stime.split("-");
                        String[] t1= etime.split(":");

                        PhoneTopupsDatabase dbConnect = new PhoneTopupsDatabase(context);
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
                        Calendar beginTime = Calendar.getInstance();

                        beginTime.set(Integer.parseInt(start[0]), Integer.parseInt(start[1]) - 1,
                                Integer.parseInt(start[2]), Integer.parseInt(t1[0]),
                                Integer.parseInt(t1[1]));


                        dbConnect.addPhone_topup(new phoneTopUps("1", stime, etime, new BigDecimal(balance), new BigDecimal(amount), type));

                        ContentValues cv = new ContentValues();
                        cv.put(CalendarContract.Events.CALENDAR_ID, "1");
                        cv.put(CalendarContract.Events.TITLE, "PHONE TOP-UP");
                        cv.put(CalendarContract.Events.DESCRIPTION,"recharge done of type "+type
                                +" with the amount "+amount+" now your account balance is "+balance);
                        cv.put(CalendarContract.Events.DTSTART, beginTime.getTimeInMillis());
                        cv.put(CalendarContract.Events.DTEND, beginTime.getTimeInMillis());

                        cv.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

                        ContentResolver cr = context.getContentResolver();
                        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, cv);
                        System.out.println("Event URI [" + uri + "]");
                        calendar ui = calendar.instance();
                        String temp = String.valueOf(smsMessage.getOriginatingAddress().charAt(0));
                        Response res = new Response("Reminder is created for the  "+smsMessage.getOriginatingAddress()+"'s msg",temp);
                        ui.updateList(res);
                        Toast.makeText(context, "Reminder is created from the "+smsMessage.getOriginatingAddress()+"'s msg", Toast.LENGTH_SHORT).show();
                    } else if (smsBody.contains("GAS CONNECTION")) {
                        String regNo = smsBody.substring(smsBody.indexOf("REG NO : ")+9,smsBody.indexOf("DATE : "));
                        String stime = smsBody.substring(smsBody.indexOf("DATE : ")+7, smsBody.indexOf(","));
                        String etime = smsBody.substring(smsBody.indexOf("TIME : ") + 7, smsBody.indexOf("."));
                        String status = smsBody.substring(smsBody.indexOf("STATUS : "));
                        String[] start = stime.split("-");
                        String[] t1= etime.split(":");

                        GasConnectionDatabase dbConnect = new GasConnectionDatabase(context);
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
                        Calendar beginTime = Calendar.getInstance();

                        beginTime.set(Integer.parseInt(start[0]), Integer.parseInt(start[1]) - 1,
                                Integer.parseInt(start[2]), Integer.parseInt(t1[0]),
                                Integer.parseInt(t1[1]));


                        dbConnect.addGasConnection(new gasConnection("1", stime, etime, status, regNo));

                        ContentValues cv = new ContentValues();
                        cv.put(CalendarContract.Events.CALENDAR_ID, "1");
                        cv.put(CalendarContract.Events.TITLE, "GAS CONNECTION");
                        cv.put(CalendarContract.Events.DESCRIPTION,"status is "+status);
                        cv.put(CalendarContract.Events.DTSTART, beginTime.getTimeInMillis());
                        cv.put(CalendarContract.Events.DTEND, beginTime.getTimeInMillis());

                        cv.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

                        ContentResolver cr = context.getContentResolver();
                        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, cv);
                        System.out.println("Event URI [" + uri + "]");
                        calendar ui = calendar.instance();
                        String temp = String.valueOf(smsMessage.getOriginatingAddress().charAt(0));
                        Response res = new Response("Reminder is created for the  "+smsMessage.getOriginatingAddress()+"'s msg",temp);

                        ui.updateList(res);
                        Toast.makeText(context, "Reminder is created from the "+smsMessage.getOriginatingAddress()+"'s SMS", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                }


        }

    }

    private Date getDateTime(String temp) throws ParseException{
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        java.util.Date d = dateFormat.parse(temp);
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
       // System.out.print("Before parse" + temp);

        String tem = dateFormat1.format(d);
        java.util.Date d1 = dateFormat1.parse(tem);

               //dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        return d1;

    }


    private Uri getCalendarUri(String path) {
        return Uri.parse(baseUri + "/" + path);
    }

    private String getCalendarUriBase() {
        String calendarUriBase = null;
        Uri calendars = Uri.parse("content://calendar/calendars");
        Cursor managedCursor = null;
        try {
            managedCursor = context.getContentResolver().query(calendars, null, null, null, null);
        } catch (Exception e) {
            // e.printStackTrace();
        }

        if (managedCursor != null) {
            calendarUriBase = "content://calendar/";
        } else {
            calendars = Uri.parse("content://com.android.calendar/calendars");
            try {
                managedCursor = context.getContentResolver().query(calendars, null, null, null, null);
            } catch (Exception e) {
                // e.printStackTrace();
            }

            if (managedCursor != null) {
                calendarUriBase = "content://com.android.calendar/";
            }

        }

        //Log.d(Par, "URI ["+calendarUriBase+"]");
        return calendarUriBase;

    }
    public static Calendar getDatePart(Date date){
        Calendar cal = Calendar.getInstance();       // get calendar instance
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);            // set hour to midnight
        cal.set(Calendar.MINUTE, 0);                 // set minute in hour
        cal.set(Calendar.SECOND, 0);                 // set second in minute
        cal.set(Calendar.MILLISECOND, 0);            // set millisecond in second

        return cal;                                  // return the date part
    }
}