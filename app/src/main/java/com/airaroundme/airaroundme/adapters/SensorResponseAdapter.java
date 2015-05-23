package com.airaroundme.airaroundme.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.airaroundme.airaroundme.R;

/**
 * Created by borax12 on 10/05/15.
 */
public class SensorResponseAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private class ViewHolder {
        TextView textView1;
        TextView textView2;
    }

    public SensorResponseAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return 5;
    }

    @Override
    public String getItem(int i) {

        return null;
    }


    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.sensor_response_row, null);
            holder.textView1 = (TextView) convertView.findViewById(R.id.sensor_response_row_field);
            holder.textView2 = (TextView) convertView.findViewById(R.id.sensor_response_row_value);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        switch (position){
            case 0: holder.textView1.setText("CO");
                break;
            case 1: holder.textView1.setText("Ozone");
                break;
            case 2: holder.textView1.setText("PPM 2.5");
                break;
            case 3: holder.textView1.setText("PPM 10");
                break;
            case 4: holder.textView1.setText("SO2");
                break;
        }
        holder.textView2.setText(getItem(position));
        return convertView;
    }
}
