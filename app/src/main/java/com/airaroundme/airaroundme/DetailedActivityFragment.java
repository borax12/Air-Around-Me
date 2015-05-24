package com.airaroundme.airaroundme;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.airaroundme.airaroundme.adapters.SensorResponseAdapter;
import com.airaroundme.airaroundme.objects.Metrics;
import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailedActivityFragment extends Fragment {

    private View mainView;
    private Context mContext;
    private ArrayList<Metrics> valueList;
    private SwingLeftInAnimationAdapter leftInAnimationAdapter;
    private SensorResponseAdapter adapter;
    private ListView listView;
    private int bgColor;
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
        mainView.setBackgroundColor(bgColor);
        listView = (ListView)mainView.findViewById(R.id.detailed_list);
        leftInAnimationAdapter.setAbsListView(listView);
        leftInAnimationAdapter.getViewAnimator().setAnimationDurationMillis(800);
        listView.setAdapter(leftInAnimationAdapter);
        return mainView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();
        valueList = getActivity().getIntent().getExtras().getParcelableArrayList("values");
        bgColor = getActivity().getIntent().getExtras().getInt("color");
        adapter = new SensorResponseAdapter(getActivity(),valueList);
        leftInAnimationAdapter = new SwingLeftInAnimationAdapter(adapter);

    }
}
