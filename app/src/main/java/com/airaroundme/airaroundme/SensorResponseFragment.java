package com.airaroundme.airaroundme;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.airaroundme.airaroundme.adapters.SensorResponseAdapter;
import com.airaroundme.airaroundme.asyncTasks.HttpAsyncTask;
import com.airaroundme.airaroundme.constants.Constants;
import com.airaroundme.airaroundme.interfaces.AsyncResponse;
import com.airaroundme.airaroundme.objects.SensorData;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.Gson;


/**
 * A placeholder fragment containing a simple view.
 */
public class SensorResponseFragment extends Fragment {


    private RelativeLayout mainView;
    private ListView sensor_response_listview;
    private SensorResponseAdapter adapter;
    private Context mContext;
    private String jsonStr;
    private SensorData data;
    private CircularProgressView progressView;


    public SensorResponseFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        mainView = (RelativeLayout) inflater.inflate(R.layout.fragment_sensor_response, container, false);
        sensor_response_listview = (ListView) mainView.findViewById(R.id.sensor_response_listview);
        progressView = (CircularProgressView) mainView.findViewById(R.id.progress_view);
        progressView.setVisibility(View.VISIBLE);
        progressView.startAnimation();
        getResponseFromSensor();

        return mainView;
    }

    private void getResponseFromSensor() {
        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                jsonStr = (String)output;
                data = new Gson().fromJson(jsonStr,SensorData.class);
                if(data!=null){
                    progressView.setVisibility(View.GONE);
                    adapter = new SensorResponseAdapter(mContext,data);
                    sensor_response_listview.setAdapter(adapter);
                }
                else{
                    Thread thread = new Thread(){
                        @Override
                        public void run() {
                           try{
                               synchronized (this) {
                                   wait(2000);
                               }
                           }
                           catch (InterruptedException e){
                               //TODO
                           }
                        }
                    };

                    thread.start();
                }

            }
        });
        httpAsyncTask.execute(Constants.urlSensor);


    }


}
