package com.example.vidhi.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class CustomListAdapter extends BaseAdapter {
    private calendar activity;
    private LayoutInflater inflater;
    private List<Response> smsMessagesList;

    public CustomListAdapter(calendar calendar, ArrayList<Response> smsMessagesList) {
        this.activity= calendar;
        this.smsMessagesList = smsMessagesList;
    }


    @Override
    public int getCount() {
        return smsMessagesList.size();
    }

    @Override
    public Object getItem(int position) {
        return smsMessagesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.layout_row, null);

        TextView title = (TextView) convertView.findViewById(R.id.title);
        EditText letter = (EditText)convertView.findViewById(R.id.nameIcon);
        Response temp = smsMessagesList.get(position);
        // title
        title.setText(temp.getTitle());
        letter.setText(temp.getLetter());

        return convertView;

    }
}
