package com.airaroundme.airaroundme;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airaroundme.airaroundme.adapters.SensorResponseAdapter;
import com.airaroundme.airaroundme.asyncTasks.HttpAsyncTask;
import com.airaroundme.airaroundme.constants.Constants;
import com.airaroundme.airaroundme.interfaces.AsyncResponse;
import com.airaroundme.airaroundme.objects.SensorData;
import com.airaroundme.airaroundme.objects.Station;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


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
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private List<Station> stationList;
    private int SIZE=3;
    private TreeMap<Float,Station> stationToDistance = new TreeMap<Float,Station>();
    private Station mStation;


    public SensorResponseFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stationList = new ArrayList<>(SIZE);

        //Creating the stations
        Station stationBTM = new Station(808,12.912811,77.609218);
        Station stationPeenya = new Station(809,13.033836,77.513245);
        Station stationBSSW = new Station(810,12.938932,77.697271 );

        //adding to the list
        stationList.add(stationBTM);
        stationList.add(stationPeenya);
        stationList.add(stationBSSW);
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
        chooseNearestStation();
        return mainView;
    }

    public void chooseNearestStation() {
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                //your code here
                Double currentLat = location.getLatitude();
                Double currentLong = location.getLongitude();

                float results[] = new float[2];

                for(Station station:stationList){
                    Location.distanceBetween(currentLat, currentLong, station.getLatitude(), station.getLongitude(), results);
                    stationToDistance.put(results[0],station);
                }

                Map.Entry<Float,Station> entry = stationToDistance.firstEntry();

                mStation = entry.getValue();

                getResponseFromSensor();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,
                0, mLocationListener);


    }

    private void getResponseFromSensor() {
        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                jsonStr = (String)output;
                //data = new Gson().fromJson(jsonStr,SensorData.class);
                if(jsonStr!=null){
                    progressView.setVisibility(View.GONE);
                }
                else{

                    Handler handler = new Handler(Looper.getMainLooper());

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Run your task here
                            Toast.makeText(mContext,"Couldnt connect to Server",Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }
                    }, 2000 );
                }

            }
        });
        String urlBuilder = Constants.baseUrl;
        urlBuilder =urlBuilder.concat(""+mStation.getStationId()+"?d=21%2F05%2F2015&h=23");
        httpAsyncTask.execute(urlBuilder);


    }


}
