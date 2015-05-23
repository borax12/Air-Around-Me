package com.airaroundme.airaroundme.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.airaroundme.airaroundme.R;
import com.airaroundme.airaroundme.objects.Metrics;

import java.util.ArrayList;

/**
 * Created by borax12 on 10/05/15.
 */
public class SensorResponseAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<Metrics> valueList;

    private class ViewHolder {
        TextView parameterValue;
        TextView parameterName;
        TextView rowDate;
    }

    public SensorResponseAdapter(Context context,ArrayList<Metrics> valueList) {
        this.valueList =valueList;
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
            convertView = inflater.inflate(R.layout.detailed_row, null);
            holder.parameterName = (TextView) convertView.findViewById(R.id.parameter_name);
            holder.parameterValue = (TextView) convertView.findViewById(R.id.parameter_value);
            holder.rowDate = (TextView) convertView.findViewById(R.id.row_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.parameterName.setText(valueList.get(position).getName());
        holder.rowDate.setText(valueList.get(position).getData().getDate());
        holder.parameterValue.setText(valueList.get(position).getData().getValue());

        return convertView;
    }
}
