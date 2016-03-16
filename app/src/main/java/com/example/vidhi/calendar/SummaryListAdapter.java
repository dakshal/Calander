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

public class SummaryListAdapter extends BaseAdapter {
    private Summary summary;
    private LayoutInflater inflater;
    private List<String> summaryList;

    public SummaryListAdapter(Summary summary, ArrayList<String> summaryList) {
        this.summary = summary;
        this.summaryList = summaryList;
    }

    @Override
    public int getCount() {
        return summaryList.size();
    }

    @Override
    public Object getItem(int position) {
        return summaryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) summary
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.layout_row, null);

        TextView title = (TextView) convertView.findViewById(R.id.title);
        EditText letter = (EditText)convertView.findViewById(R.id.nameIcon);
        String temp = summaryList.get(position);
        title.setText(temp);
        //letter.setText(temp.getLetter());

        return convertView;

    }
}
