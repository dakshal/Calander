package com.example.vidhi.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class Summary extends AppCompatActivity {
    private Toolbar mToolbar;
    private ArrayList<String> summaryList = new ArrayList<String>();
    private ListView smsListView;
    private SummaryListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        smsListView = (ListView) findViewById(R.id.smsList);
        adapter = new SummaryListAdapter(this, summaryList);
        smsListView.setAdapter(adapter);

        final Calendar c = Calendar.getInstance();
        int yyyy = c.get(Calendar.YEAR);
        int mm = c.get(Calendar.MONTH);
        int dd = c.get(Calendar.DAY_OF_MONTH);

        String date = new StringBuilder()
                .append(yyyy).append("-").append(mm + 1).append("-")
                .append(dd).toString();
        int count = 0;

        PhoneTopupsDatabase p = new PhoneTopupsDatabase(this);

        String amount = p.retrievePhone_topup(date.substring(0,date.length()-4));
        if(amount != null && amount.length()>0){
            summaryList.add("You have spend " + amount + " rs. this month on phone topups");
            count++;
            adapter.notifyDataSetChanged();
        }

        BankTransactionDatabase b = new BankTransactionDatabase(this);
        int transaction_count = b.retrieveBANK_TRANSACTION(date.substring(0,date.length()-4));

        if(transaction_count > 0){
            summaryList.add("There are " + transaction_count + "transactions performed in your bank accounts");
            count++;
            adapter.notifyDataSetChanged();

        }
        List<String> accList = b.retrieveAccnumbers();
        if(accList != null && !accList.isEmpty() && accList.size() > 0){
            for(int i =0;i<accList.size();i++){
                if(accList.get(i) != null){
                    String amt = b.retrieveBalance(accList.get(i));
                    if(amt != null && amt.length() > 0){
                        summaryList.add("Your accountNumber " + accList.get(i) + " has a balance of " + amt);
                        count++;
                        adapter.notifyDataSetChanged();

                    }
                }
            }
        }

        SalaryDatabaseConnect s = new SalaryDatabaseConnect(this);
        String salary = s.retrieveSalary(date.substring(0,date.length()-4));
        if(salary != null && salary.length() > 0){
            summaryList.add(salary);
            count++;
            adapter.notifyDataSetChanged();
        }

        if(count ==0){
            LinearLayout hiddenLayout = (LinearLayout)findViewById(R.id.summaHiddenLayout);
            if(hiddenLayout == null){
                //Inflate the Hidden Layout Information View
                RelativeLayout myLayout = (RelativeLayout)findViewById(R.id.testing);
                View hiddenInfo = getLayoutInflater().inflate(R.layout.summary, myLayout, false);
                myLayout.addView(hiddenInfo);
            }

            //Get References to the TextView
            TextView myTextView = (TextView) findViewById(R.id.summaryText);
            // Update the TextView Text
            myTextView.setText("NO SUMMARIES TO DISPLAY");
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
