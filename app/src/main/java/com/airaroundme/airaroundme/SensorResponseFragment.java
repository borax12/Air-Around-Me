package com.airaroundme.airaroundme;

import android.content.Context;
import android.graphics.Typeface;
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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airaroundme.airaroundme.asyncTasks.HttpAsyncTask;
import com.airaroundme.airaroundme.constants.Constants;
import com.airaroundme.airaroundme.interfaces.AsyncResponse;
import com.airaroundme.airaroundme.objects.FetchedData;
import com.airaroundme.airaroundme.objects.Station;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * A placeholder fragment containing a simple view.
 */
public class SensorResponseFragment extends Fragment implements LocationListener{


    private RelativeLayout mainView;
    private Context mContext;
    private String jsonStr;
    private CircularProgressView progressView;
    private LocationManager mLocationManager;
    private List<Station> stationList;
    private int SIZE=3;
    private TreeMap<Float,Station> stationToDistance = new TreeMap<Float,Station>();
    private Station mStation;
    private boolean firstTime=true;
    private FetchedData data;
    private TextView airQualityLine;
    private TextView airQualityTextValue;
    private Typeface font;
    private Location mLocation;
    private TextView airQualityValue;
    private LinearLayout aqiBar;
    private LinearLayout mainBarContainer;
    private LinearLayout proTipContainer;
    private TextView proTipBulb;
    private TextView proTipText;

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

        getCurrentLocation();
    }

    public void getCurrentLocation() {
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(mLocation!=null){
            chooseNearestStation();
        }
        else{
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        mainView = (RelativeLayout) inflater.inflate(R.layout.fragment_sensor_response, container, false);
        progressView = (CircularProgressView) mainView.findViewById(R.id.progress_view);
        progressView.setVisibility(View.VISIBLE);
        progressView.startAnimation();

        airQualityLine =(TextView) mainView.findViewById(R.id.air_quality_line);
        airQualityTextValue = (TextView)mainView.findViewById(R.id.air_quality_text);
        airQualityValue = (TextView) mainView.findViewById(R.id.aqi_value);
        aqiBar = (LinearLayout) mainView.findViewById(R.id.aqi_bar);
        mainBarContainer = (LinearLayout) mainView.findViewById(R.id.main_bar_container);
        proTipContainer = (LinearLayout) mainView.findViewById(R.id.pro_tip);
        proTipBulb = (TextView) mainView.findViewById(R.id.pro_tip_bulb);
        proTipText = (TextView) mainView.findViewById(R.id.pro_tip_text);


        font = Typeface.createFromAsset(getActivity().getAssets(),"Roboto-Light.ttf");
        airQualityLine.setTypeface(font);
        airQualityValue.setTypeface(font);
        proTipText.setTypeface(font);

        font = Typeface.createFromAsset(getActivity().getAssets(),"Roboto-Medium.ttf");
        airQualityTextValue.setTypeface(font);
        font = Typeface.createFromAsset(getActivity().getAssets(),"fontawesome-webfont.ttf");
        proTipBulb.setTypeface(font);
        return mainView;
    }

    public void chooseNearestStation() {

        firstTime=false;
        Double currentLat = mLocation.getLatitude();
        Double currentLong = mLocation.getLongitude();

        float results[] = new float[2];

        for(Station station:stationList){
            Location.distanceBetween(currentLat, currentLong, station.getLatitude(), station.getLongitude(), results);
            stationToDistance.put(results[0],station);
        }

        Map.Entry<Float,Station> entry = stationToDistance.firstEntry();

        mStation = entry.getValue();

        getResponseFromSensor();

    }

    private void getResponseFromSensor() {
        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                jsonStr = (String)output;
                data = new Gson().fromJson(jsonStr,FetchedData.class);
                if(jsonStr!=null){
                    progressView.setVisibility(View.GONE);
                    setUpViews(data);
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
        String urlBuilder = Constants.urlSensor;
        urlBuilder =urlBuilder.concat("?stationId=" + mStation.getStationId());
        httpAsyncTask.execute(urlBuilder);
    }

    private void setUpViews(FetchedData data) {

        float scaleFactor = Float.parseFloat(data.getAqi().getValue())/500.0f;

        airQualityTextValue.setText(data.getAqi().getRemark().toUpperCase());
        airQualityValue.setText(data.getAqi().getValue());
        airQualityTextValue.setVisibility(View.VISIBLE);
        int newWidth = 100+ (int) (aqiBar.getWidth()*scaleFactor);
        int originalHeight = aqiBar.getHeight();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(newWidth,originalHeight );
        aqiBar.setLayoutParams(params);
        params.setMargins(0,0,20,0);
        mainBarContainer.setVisibility(View.VISIBLE);


        Animation animation = new ScaleAnimation(0.0f,1.0f,1.0f,1.0f,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0);
        animation.setDuration(1000);
        aqiBar.setAnimation(animation);

        proTipContainer.setVisibility(View.VISIBLE);
        animation = new AlphaAnimation(0.0f,1.0f);
        animation.setDuration(1000);

        proTipContainer.setAnimation(animation);

    }


    @Override
    public void onLocationChanged(Location location) {
            mLocation = location;
            chooseNearestStation();
            mLocationManager.removeUpdates(this);
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
}
