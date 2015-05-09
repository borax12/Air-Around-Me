package com.airaroundme.airaroundme;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.airaroundme.airaroundme.adapters.SensorResponseAdapter;
import com.airaroundme.airaroundme.asyncTasks.HttpAsyncTask;
import com.airaroundme.airaroundme.constants.Constants;
import com.airaroundme.airaroundme.interfaces.AsyncResponse;
import com.airaroundme.airaroundme.objects.SensorData;
import com.google.gson.Gson;


/**
 * A placeholder fragment containing a simple view.
 */
public class SensorResponseFragment extends Fragment {


    private LinearLayout mainView;
    private ListView sensor_response_listview;
    private SensorResponseAdapter adapter;
    private Context mContext;
    private String jsonStr;
    private SensorData data;


    public SensorResponseFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        mainView = (LinearLayout) inflater.inflate(R.layout.fragment_sensor_response, container, false);
        sensor_response_listview = (ListView) mainView.findViewById(R.id.sensor_response_listview);
        getResponseFromSensor();

        return mainView;
    }

    private void getResponseFromSensor() {
        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                jsonStr = (String)output;
                data = new Gson().fromJson(jsonStr,SensorData.class);
                adapter = new SensorResponseAdapter(mContext,data);
                sensor_response_listview.setAdapter(adapter);
            }
        });
        httpAsyncTask.execute(Constants.urlSensor);


    }


}
