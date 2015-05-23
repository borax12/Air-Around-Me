package com.airaroundme.airaroundme;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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
import com.airaroundme.airaroundme.objects.Aqi;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.Gson;


/**
 * A placeholder fragment containing a simple view.
 */
public class PredictionResponseFragment extends Fragment{


    private RelativeLayout mainView;
    private Context mContext;
    private String jsonStr;
    private CircularProgressView progressView;
    private int stationId;
    private Aqi data;
    private TextView airQualityLine;
    private TextView airQualityTextValue;
    private Typeface font;
    private TextView airQualityValue;
    private LinearLayout aqiBar;
    private LinearLayout mainBarContainer;
    private LinearLayout proTipContainer;
    private TextView proTipBulb;
    private TextView proTipText;

    public PredictionResponseFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if(data!=null){
            float scaleFactor = Float.parseFloat(data.getValue())/500.0f;

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
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stationId = getActivity().getIntent().getExtras().getInt("stationId");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        mainView = (RelativeLayout) inflater.inflate(R.layout.fragment_prediction_response, container, false);
        progressView = (CircularProgressView) mainView.findViewById(R.id.progress_view);
        progressView.setVisibility(View.VISIBLE);
        progressView.startAnimation();

        airQualityLine =(TextView) mainView.findViewById(R.id.air_quality_line_prediction);
        airQualityTextValue = (TextView)mainView.findViewById(R.id.air_quality_text_prediction);
        airQualityValue = (TextView) mainView.findViewById(R.id.aqi_value_prediction);
        aqiBar = (LinearLayout) mainView.findViewById(R.id.aqi_bar_prediction);
        mainBarContainer = (LinearLayout) mainView.findViewById(R.id.main_bar_container_prediction);
        proTipContainer = (LinearLayout) mainView.findViewById(R.id.pro_tip_prediction);
        proTipBulb = (TextView) mainView.findViewById(R.id.pro_tip_bulb_prediction);
        proTipText = (TextView) mainView.findViewById(R.id.pro_tip_text_prediction);


        font = Typeface.createFromAsset(getActivity().getAssets(),"Roboto-Light.ttf");
        airQualityLine.setTypeface(font);
        airQualityValue.setTypeface(font);
        proTipText.setTypeface(font);

        font = Typeface.createFromAsset(getActivity().getAssets(),"Roboto-Medium.ttf");
        airQualityTextValue.setTypeface(font);
        font = Typeface.createFromAsset(getActivity().getAssets(),"fontawesome-webfont.ttf");
        proTipBulb.setTypeface(font);

        getResponseFromSensor();
        return mainView;
    }


    private void getResponseFromSensor() {
        HttpAsyncTask httpAsyncTask = new HttpAsyncTask(new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                jsonStr = (String)output;
                data = new Gson().fromJson(jsonStr,Aqi.class);
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
        String urlBuilder = Constants.predictionSensor;
        urlBuilder =urlBuilder.concat("?stationId="+stationId);
        httpAsyncTask.execute(urlBuilder);
    }

    private void setUpViews(Aqi data) {

        float scaleFactor = Float.parseFloat(data.getValue())/500.0f;
        String remark = data.getRemark();

        airQualityLine.setVisibility(View.VISIBLE);
        //setting up the background color
        mainView.setBackgroundColor(Color.parseColor(data.getColor()));
        airQualityTextValue.setText(remark.toUpperCase());
        airQualityValue.setText(data.getValue());

        switch (remark){
            case "Good": proTipText.setText("Go take a walk");
                break;
            case "Satisfactory": proTipText.setText("Minor Breathing discomfort for Sensitive People");
                break;
            case "Moderate": proTipText.setText("Lungs Disease Patients beware");
                break;
            case "Poor": proTipText.setText("Breathing discomfort on Prolonged Exposure");
                break;
            case "Very Poor": proTipText.setText("Respiratory Illness on Prolonged Exposure");
                break;
            case "Severe": proTipText.setText("Time to get a Gas Mask");
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

    }

}
