package com.airaroundme.airaroundme;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airaroundme.airaroundme.adapters.SensorResponseAdapter;
import com.airaroundme.airaroundme.objects.Metrics;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailedActivityFragment extends Fragment {

    private View mainView;
    private Context mContext;
    private ArrayList<Metrics> valueList;
    private SensorResponseAdapter adapter;

    public DetailedActivityFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_detailed, container, false);

        return mainView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();

        valueList = getActivity().getIntent().getExtras().getParcelableArrayList("values");
        adapter = new SensorResponseAdapter(getActivity(),valueList);

    }
}
