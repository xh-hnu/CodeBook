package com.xuhe.codebook20;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/3/31.
 */

public class DataAdapter extends ArrayAdapter {
    private int resourceId;
    private List list;

    public DataAdapter(Context context, int textViewResourceId, List<Data> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Data data = (Data) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView title_tv = view.findViewById(R.id.title);
        TextView content_tv = view.findViewById(R.id.content);
        TextView create_time_tv = view.findViewById(R.id.createDate);
        title_tv.setText(data.getmTitle());
        content_tv.setText(data.getmContent());
        create_time_tv.setText(data.getmTime());
        return view;
    }
}