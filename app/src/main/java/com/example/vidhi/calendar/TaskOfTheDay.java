package com.example.vidhi.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TaskOfTheDay extends AppCompatActivity {
    private Toolbar mToolbar;
    private ArrayList<String> tasksOfTheDayList = new ArrayList<String>();
    private ListView smsListView;
    private TasksOfTheDayListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_of_the_day);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        smsListView = (ListView) findViewById(R.id.taskList);
        adapter = new TasksOfTheDayListAdapter(this, tasksOfTheDayList);
        smsListView.setAdapter(adapter);

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
        if(invitationList!= null && !invitationList.isEmpty() && invitationList.size() > 0){
            for (int i=0;i<invitationList.size();i++){
                if(null != invitationList.get(i)){
                    String event = "Today you have an event " +
                            " of "+invitationList.get(i).getOccasion() +" at "
                            +invitationList.get(i).getVenue() +" on "
                            +invitationList.get(i).getTime();
                    tasksOfTheDayList.add(event);
                    count++;
                    adapter.notifyDataSetChanged();

                }
            }
        }

        DatabaseConnect d = new DatabaseConnect(this);
        List<Meetings> meetingsList = d.retrieveMeetings(date);
        if(meetingsList != null && !meetingsList.isEmpty() && meetingsList.size() > 0){
            for (int i=0;i<meetingsList.size();i++){
                if(null != meetingsList.get(i)){
                    String meeting = "Today you have a meeting on "+meetingsList.get(i).getTopic()
                            +" at "+meetingsList.get(i).getVenue()+" "+meetingsList.get(i).getTime();
                    tasksOfTheDayList.add(meeting);
                    count++;
                    adapter.notifyDataSetChanged();
                }
            }
        }

        BillsDatabaseConnect b = new BillsDatabaseConnect(this);
        List<Bills> billsList = b.retrieveBills(dd,date.substring(0,date.length()-4));

        if(billsList != null && !billsList.isEmpty() && billsList.size() >0){
            for (int i=0;i<billsList.size();i++){
                if(null != billsList.get(i)){
                    String bill = "Your bill for "+billsList.get(i).getBillType()
                            +" has a "+billsList.get(i).getStatus()+" status, due date is "+billsList.get(i).getDueDate()
                            +" days left :"+billsList.get(i).getNoOfDaysLeft();
                    tasksOfTheDayList.add(bill);
                    count++;
                    adapter.notifyDataSetChanged();
                }
            }
        }

        GasConnectionDatabase g = new GasConnectionDatabase(this);
        List<gasConnection> gasList = g.retrieveConnection(dd,date.substring(0,date.length()-4));

        if(gasList != null && !gasList.isEmpty() && gasList.size() > 0){
            for (int i =0;i<gasList.size();i++){
                if(null != gasList.get(i)){
                    String gas = "Your gas connection with registration no "+gasList.get(i).getRegistrationNo()
                            +"'s status is "+gasList.get(i).getStatus()+" and date of connection is "+gasList.get(i).getDate();
                    tasksOfTheDayList.add(gas);
                    count++;
                    adapter.notifyDataSetChanged();
                }
            }
        }

        if(count == 0) {

            LinearLayout hiddenLayout = (LinearLayout)findViewById(R.id.hiddenLayout);
            if(hiddenLayout == null){
                //Inflate the Hidden Layout Information View
                RelativeLayout myLayout = (RelativeLayout)findViewById(R.id.test);
                View hiddenInfo = getLayoutInflater().inflate(R.layout.allcaughtup, myLayout, false);
                myLayout.addView(hiddenInfo);
            }

            //Get References to the TextView
            TextView myTextView = (TextView) findViewById(R.id.all);
            // Update the TextView Text
            myTextView.setText("ALL CAUGHT UP!!");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        return true;
    }

    public void openSummary(MenuItem item) {
        Intent intent = new Intent(this,Summary.class);
        this.startActivity(intent);

    }

    public void openTasks(MenuItem item) {

        Intent intent = new Intent(this,TaskOfTheDay.class);
        this.startActivity(intent);
    }
    protected void onResume(){
        super.onResume();
    }

}
