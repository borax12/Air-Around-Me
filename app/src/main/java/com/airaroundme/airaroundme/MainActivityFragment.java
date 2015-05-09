package com.airaroundme.airaroundme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private LinearLayout mainView;
    private TextView air_around_me_button;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = (LinearLayout)inflater.inflate(R.layout.fragment_main, container, false);
        air_around_me_button = (TextView)mainView.findViewById(R.id.air_around_button);
        return mainView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent = new Intent(getActivity(),SensorResponseActivity.class);

        air_around_me_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(intent);
            }
        });


    }
}
