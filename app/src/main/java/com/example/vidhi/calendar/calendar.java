package com.example.vidhi.calendar;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class calendar extends AppCompatActivity {
    private Toolbar mToolbar;
    private static calendar inst;
    private ArrayList<Response> smsMessagesList = new ArrayList<Response>();
    private ListView smsListView;
    private CustomListAdapter adapter;
    public static calendar instance() {
        return inst;
    }
    @Override
    public void onStart() {
        super.onStart();
        smsMessagesList = new ArrayList<>();
        smsListView = (ListView) findViewById(R.id.calList);
        adapter = new CustomListAdapter(this, smsMessagesList);
        smsListView.setAdapter(adapter);
        getTodaysReminder();
    }
    protected void onResume() {
        super.onResume();
        Log.d("resume","problem");

    }
protected void onStop(){
    super.onStop();
    Log.d("stop", "problem");
    this.finish();
}
    private void getTodaysReminder() {

        final Calendar c = Calendar.getInstance();
        int yyyy = c.get(Calendar.YEAR);
        int mm = c.get(Calendar.MONTH);
        int dd = c.get(Calendar.DAY_OF_MONTH);

        String date = new StringBuilder()
                .append(yyyy).append("-").append(mm + 1).append("-")
                .append(dd).toString();
        int count = 0;
        EventDatabaseConnect e = new EventDatabaseConnect(this);
        List<Invitations> invitationList = e.retrieveInvitations(date);
        if (invitationList != null && !invitationList.isEmpty() && invitationList.size() > 0) {
            for (int i = 0; i < invitationList.size(); i++) {
                if (null != invitationList.get(i)) {
                    String event = "Reminder is set for the " +
                            invitationList.get(i).getOccasion() + " at "
                            + invitationList.get(i).getVenue() + " on "
                            + invitationList.get(i).getTime();
                    smsMessagesList.add(new Response(event, "E"));
                    count++;
                    adapter.notifyDataSetChanged();

                }
            }
        }

        DatabaseConnect d = new DatabaseConnect(this);
        List<Meetings> meetingsList = d.retrieveMeetings(date);
        if (meetingsList != null && !meetingsList.isEmpty() && meetingsList.size() > 0) {
            for (int i = 0; i < meetingsList.size(); i++) {
                if (null != meetingsList.get(i)) {
                    String meeting = "Reminder is set for " + meetingsList.get(i).getTopic()
                            + " at " + meetingsList.get(i).getVenue() + " " + meetingsList.get(i).getTime();
                    smsMessagesList.add(new Response(meeting, "M"));
                    count++;
                    adapter.notifyDataSetChanged();
                }
            }
        }

        BillsDatabaseConnect b = new BillsDatabaseConnect(this);
        List<Bills> billsList = b.retrieveBillsForToday(date);

        if (billsList != null && !billsList.isEmpty() && billsList.size() > 0) {
            for (int i = 0; i < billsList.size(); i++) {
                if (null != billsList.get(i)) {
                    String bill = "Reminder is set for  " + billsList.get(i).getBillType()
                            + " has a " + billsList.get(i).getStatus() + " status, due date is " + billsList.get(i).getDueDate()
                            + " days left :" + billsList.get(i).getNoOfDaysLeft();
                    smsMessagesList.add(new Response(bill, "B"));
                    count++;
                    adapter.notifyDataSetChanged();
                }
            }
        }

        GasConnectionDatabase g = new GasConnectionDatabase(this);
        List<gasConnection> gasList = g.retrieveConnectionToday(date);

        if (gasList != null && !gasList.isEmpty() && gasList.size() > 0) {
            for (int i = 0; i < gasList.size(); i++) {
                if (null != gasList.get(i)) {
                    String gas = "Reminder is set for gas connection registration no " + gasList.get(i).getRegistrationNo()
                            + "'s status is " + gasList.get(i).getStatus() + " and date of connection is " + gasList.get(i).getDate();
                    smsMessagesList.add(new Response(gas, "G"));
                    count++;
                    adapter.notifyDataSetChanged();
                }
            }
        }


        PhoneTopupsDatabase p = new PhoneTopupsDatabase(this);
        List<phoneTopUps> phoneTopupList= p.retrievePhone_topupToday(date);

        if(phoneTopupList != null && !phoneTopupList.isEmpty() && phoneTopupList.size() >0){
            for(int i=0;i<phoneTopupList.size();i++) {
                    if(phoneTopupList.get(i) != null) {


                        smsMessagesList.add(new Response("Your reminder for today's topup with amount"
                                +phoneTopupList.get(i).getAmount() +"  has been set", "T"));
                        count++;
                        adapter.notifyDataSetChanged();
                    }
            }
        }

        BankTransactionDatabase bank = new BankTransactionDatabase(this);
        List<BankTransactions> transactionList= bank.retrieveTransaction(date);

        if(transactionList != null && !transactionList.isEmpty() && transactionList.size() >0){
            for(int i=0;i<transactionList.size();i++) {
                if(transactionList.get(i) != null){

                smsMessagesList.add(new Response("Reminder for bank transaction with transaction id "
                        +transactionList.get(i).getTransactionId()+" is set", "B"));
                count++;
                adapter.notifyDataSetChanged();
                }
            }

        }

        SalaryDatabaseConnect s = new SalaryDatabaseConnect(this);
        List<Salary> salaryList = s.retrieveSalaryToday(date);
        if(salaryList != null && !salaryList.isEmpty() && salaryList.size() > 0){
            for(int i=0;i<salaryList.size();i++) {
                if(salaryList.get(i) != null) {
                    smsMessagesList.add(new Response("Reminder for salary with amount "
                            +salaryList.get(i).getAmount()+"is set", "S"));
                    count++;
                    adapter.notifyDataSetChanged();
                }
            }
        }

        if (count == 0) {

            LinearLayout hiddenLayout = (LinearLayout) findViewById(R.id.hiddenLayout);
            if (hiddenLayout == null) {
                //Inflate the Hidden Layout Information View
                RelativeLayout myLayout = (RelativeLayout) findViewById(R.id.rem);
                View hiddenInfo = getLayoutInflater().inflate(R.layout.allcaughtup, myLayout, false);
                myLayout.addView(hiddenInfo);
            }

            //Get References to the TextView
            TextView myTextView = (TextView) findViewById(R.id.all);
            // Update the TextView Text
            myTextView.setText("NO REMINDERS TODAY!!");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //smsListView.setOnItemClickListener(this);

        Intent calIntent = new Intent(Intent.ACTION_INSERT);

        SmsBroadcastReceiver smsBroadcastReceiver = new SmsBroadcastReceiver();
        // android.os.Debug.waitForDebugger();
        smsBroadcastReceiver.onReceive(this, calIntent);


    }
@Override
public boolean onCreateOptionsMenu(Menu menu){
    getMenuInflater().inflate(R.menu.menu_calendar, menu);
    return true;
}
    public void updateList(Response res) {
       // adapter.insert(senderAdd, 0);
        smsMessagesList.add(res);
        adapter.notifyDataSetChanged();


    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//
//        if (id == R.id.summary) {
//
//            Intent intent = new Intent(this,Summary.class);
//            this.startActivity(intent);
//            return true;
//        }
//
//        if (id == R.id.tasks) {
//            Intent intent = new Intent(this,TaskOfTheDay.class);
//            this.startActivity(intent);
//            return true;
//        }
//        if(id == android.R.id.home){
//            this.finish();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public void openSummary(MenuItem item) {
        Intent intent = new Intent(this,Summary.class);
        this.startActivity(intent);

    }

    public void openTasks(MenuItem item) {

        Intent intent = new Intent(this,TaskOfTheDay.class);
        this.startActivity(intent);
    }
}
