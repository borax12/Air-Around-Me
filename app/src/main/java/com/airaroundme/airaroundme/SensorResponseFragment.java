package com.airaroundme.airaroundme;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airaroundme.airaroundme.asyncTasks.HttpAsyncTask;
import com.airaroundme.airaroundme.constants.Constants;
import com.airaroundme.airaroundme.interfaces.AsyncResponse;
import com.airaroundme.airaroundme.objects.FetchedData;
import com.airaroundme.airaroundme.objects.Metrics;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A placeholder fragment containing a simple view.
 */
public class SensorResponseFragment extends Fragment implements LocationListener{


    private RelativeLayout mainView;
    private Context mContext;
    private String jsonStr;
    private CircularProgressView progressView;
    private LocationManager mLocationManager;
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
    private Button moreButton;
    private Button predictionButton;
    private TextView infoButton;

    public SensorResponseFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();
        getCurrentLocation();
        if(data!=null){
            float scaleFactor = Float.parseFloat(data.getAqi().getValue())/500.0f;
            String remark = data.getAqi().getRemark();

            //setting up the background color

            int newWidth = 100+ (int) (aqiBar.getWidth()*scaleFactor);
            int originalHeight = aqiBar.getHeight();

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(newWidth,originalHeight );
            aqiBar.setLayoutParams(params);
            params.setMargins(0, 0, 20, 0);

            Animation animation = new ScaleAnimation(0.0f,1.0f,1.0f,1.0f,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0);
            animation.setDuration(1000);
            aqiBar.setAnimation(animation);

            animation = new AlphaAnimation(0.0f,1.0f);
            animation.setDuration(1000);

            proTipContainer.setAnimation(animation);
            moreButton.setAnimation(animation);
            predictionButton.setAnimation(animation);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SensorResponse", "Screen Made");
    }

    public void getCurrentLocation() {
/*        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);*/

        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria locationCritera = new Criteria();
        locationCritera.setAccuracy(Criteria.ACCURACY_FINE);
        locationCritera.setAltitudeRequired(false);
        locationCritera.setBearingRequired(false);
        locationCritera.setCostAllowed(true);
        locationCritera.setPowerRequirement(Criteria.NO_REQUIREMENT);

        String providerName = mLocationManager.getBestProvider(locationCritera, true);

        if (!providerName.equals("passive") && mLocationManager.isProviderEnabled(providerName)) {
            // Provider is enabled
            mLocationManager.requestLocationUpdates(providerName, 0, 0, this);
        } else {
            // Provider not enabled, prompt user to enable it
            Toast.makeText(getActivity(), R.string.please_turn_on_gps, Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(myIntent);
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
        moreButton = (Button) mainView.findViewById(R.id.more_button);
        predictionButton = (Button) mainView.findViewById(R.id.prediction_button);
        infoButton =(TextView) mainView.findViewById(R.id.info_button);


        font = Typeface.createFromAsset(getActivity().getAssets(),"Roboto-Light.ttf");
        airQualityLine.setTypeface(font);
        airQualityValue.setTypeface(font);
        proTipText.setTypeface(font);
        moreButton.setTypeface(font);
        predictionButton.setTypeface(font);

        font = Typeface.createFromAsset(getActivity().getAssets(),"Roboto-Medium.ttf");
        airQualityTextValue.setTypeface(font);
        font = Typeface.createFromAsset(getActivity().getAssets(),"fontawesome-webfont.ttf");
        proTipBulb.setTypeface(font);
        infoButton.setTypeface(font);
        return mainView;
    }

    private void getResponseFromSensor() {
        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                Log.d("SensorResponse","Response Received");
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
        urlBuilder =urlBuilder.concat("?"+"lon="+mLocation.getLongitude()+"&"+"lat="+mLocation.getLatitude());
        Log.d("SensorResponse","Request Made");
        httpAsyncTask.execute(urlBuilder);

    }

    private void setUpViews(FetchedData data) {

        float scaleFactor = Float.parseFloat(data.getAqi().getValue())/500.0f;
        String remark = data.getAqi().getRemark();

        //setting up the background color
        airQualityTextValue.setText(remark.toUpperCase());
        airQualityValue.setText(data.getAqi().getValue());

        int color=0;

        switch (remark){
            case "Good": proTipText.setText("Go take a walk");
                color = getActivity().getResources().getColor(R.color.good);
                mainView.setBackgroundColor(color);
                break;
            case "Satisfactory": proTipText.setText("Minor Breathing discomfort for Sensitive People");
                color = getActivity().getResources().getColor(R.color.satisfactory);
                mainView.setBackgroundColor(color);
                break;
            case "Moderate": proTipText.setText("Lungs Disease Patients beware");
                color = getActivity().getResources().getColor(R.color.moderate);
                mainView.setBackgroundColor(color);
                break;
            case "Poor": proTipText.setText("Breathing discomfort on Prolonged Exposure");
                color = getActivity().getResources().getColor(R.color.poor);
                mainView.setBackgroundColor(color);
                break;
            case "Very Poor": proTipText.setText("Respiratory Illness on Prolonged Exposure");
                color = getActivity().getResources().getColor(R.color.verypoor);
                mainView.setBackgroundColor(color);
                break;
            case "Severe": proTipText.setText("Time to get a Gas Mask");
                color = getActivity().getResources().getColor(R.color.severe);
                mainView.setBackgroundColor(color);
                break;

        }

        airQualityTextValue.setVisibility(View.VISIBLE);
        int newWidth = 100+ (int) (aqiBar.getWidth()*scaleFactor);
        int originalHeight = aqiBar.getHeight();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(newWidth,originalHeight );
        aqiBar.setLayoutParams(params);
        params.setMargins(0, 0, 20, 0);
        mainBarContainer.setVisibility(View.VISIBLE);


        Animation animation = new ScaleAnimation(0.0f,1.0f,1.0f,1.0f,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0);
        animation.setDuration(1000);
        aqiBar.setAnimation(animation);

        proTipContainer.setVisibility(View.VISIBLE);
        animation = new AlphaAnimation(0.0f,1.0f);
        animation.setDuration(1000);

        proTipContainer.setAnimation(animation);

        moreButton.setVisibility(View.VISIBLE);
        //TODO: Make the visibility to visible when prediction part of app is done
        predictionButton.setVisibility(View.GONE);
        moreButton.setAnimation(animation);
        predictionButton.setAnimation(animation);

        final Intent intent = new Intent(getActivity(),DetailedActivity.class);
        final Intent intent_prediction = new Intent(getActivity(),PredictionResponseActivity.class);
        final Intent intent_help = new Intent(getActivity(),InfoActivity.class);

         Metrics[] valueArray = data.getMetrics();
        ArrayList<Metrics> valueList = new ArrayList<>(Arrays.asList(valueArray));

        intent.putExtra("color",color);
        intent.putExtra("values",valueList);
        //intent_prediction.putExtra("stationId",mStation.getStationId());

        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(intent);
            }
        });

        predictionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(intent_prediction);
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(intent_help);
            }
        });


    }


    @Override
    public void onLocationChanged(Location location) {
            mLocation = location;
            getResponseFromSensor();
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
