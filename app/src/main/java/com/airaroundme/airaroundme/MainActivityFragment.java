package com.airaroundme.airaroundme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private LinearLayout mainView;
    private TextView air_around_me_button;
    private Context mContext;

    public MainActivityFragment() {
        mContext=getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = (LinearLayout)inflater.inflate(R.layout.fragment_main, container, false);
        air_around_me_button = (TextView)mainView.findViewById(R.id.air_around_button);
        final Intent intent = new Intent(getActivity(),SensorResponseActivity.class);

        air_around_me_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check if you are connected or not
                if(isConnected()){
                    getActivity().startActivity(intent);
                }
               else
                {
                    Toast.makeText(mContext,"You are offline",Toast.LENGTH_SHORT);
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
    }
}
