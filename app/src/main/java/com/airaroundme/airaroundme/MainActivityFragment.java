package com.airaroundme.airaroundme;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private RelativeLayout mainView;
    private Button air_around_me_button;
    private Context mContext;

    public MainActivityFragment() {

    }


    @Override
    public void onResume() {
        super.onResume();
        Animation animation = new TranslateAnimation(0,0,300f,0);
        animation.setDuration(1000);
        if(air_around_me_button!=null){
            air_around_me_button.setAnimation(animation);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = (RelativeLayout)inflater.inflate(R.layout.fragment_main, container, false);
        air_around_me_button = (Button)mainView.findViewById(R.id.air_around_button);
        final Intent intent = new Intent(getActivity(),SensorResponseActivity.class);
        Animation animation = new TranslateAnimation(0,0,300f,0);
        animation.setDuration(1000);
        if(air_around_me_button!=null){
            air_around_me_button.setAnimation(animation);
        }
        air_around_me_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check if you are connected or not
                if(isConnected()){
                    getActivity().startActivity(intent);
                }
               else
                {
                    Toast.makeText(mContext,"You are offline",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return mainView;
    }

    private boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();
    }
}
