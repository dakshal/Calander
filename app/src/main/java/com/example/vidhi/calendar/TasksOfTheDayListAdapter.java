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

public class TasksOfTheDayListAdapter extends BaseAdapter{
    private TaskOfTheDay taskOfTheDay;
    private LayoutInflater inflater;
    private List<String> tasksOfTheDayList;

    public TasksOfTheDayListAdapter(TaskOfTheDay taskOfTheDay, ArrayList<String> tasksOfTheDayList) {
        this.taskOfTheDay = taskOfTheDay;
        this.tasksOfTheDayList = tasksOfTheDayList;
    }


    @Override
    public int getCount() {
        return tasksOfTheDayList.size();
    }

    @Override
    public Object getItem(int position) {
        return tasksOfTheDayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) taskOfTheDay
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.layout_row, null);

        TextView title = (TextView) convertView.findViewById(R.id.title);
        EditText letter = (EditText)convertView.findViewById(R.id.nameIcon);
        String temp = tasksOfTheDayList.get(position);
        title.setText(temp);
        //letter.setText(temp.getLetter());

        return convertView;

    }
}
