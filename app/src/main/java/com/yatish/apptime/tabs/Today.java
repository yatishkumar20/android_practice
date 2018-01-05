package com.yatish.apptime.tabs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.yatish.apptime.adapter.MyAppAdapter;
import com.yatish.apptime.R;
import com.yatish.apptime.logic.AppUsage;
import com.yatish.apptime.model.Apps;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;


/**
 * Created by yatish on 16/6/17.
 */

public class Today extends Fragment {

    List<Apps> instdApp;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    RecyclerView mRecyclerView;
    ProgressBar progress;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.today, container, false);
        view = rootView;
        init(view);
        return rootView;
    }



    @Override
    public void onStart() {
        super.onStart();
    }


    public void init(View v) {

        mRecyclerView = (RecyclerView) v.findViewById(R.id.installed_apps);
        progress = (ProgressBar) v.findViewById(R.id.progress);

            //new TodayAsync().execute();
            mLayoutManager = new LinearLayoutManager(getContext());

            mRecyclerView.setLayoutManager(mLayoutManager);

            new TodayAsync().execute();

        mAdapter = new MyAppAdapter(getContext(), new ArrayList<Apps>(),"today");
        mRecyclerView.setAdapter(mAdapter);

    }

    class TodayAsync extends AsyncTask<String,Void,List<Apps>>{

        @Override
        protected void onPreExecute() {
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Apps> doInBackground(String... params) {

            List<Apps> installedAppsList = AppUsage.getAppUsageStaticsToday(getStartTime());

            Collections.sort(installedAppsList);

            return installedAppsList;
        }

        @Override
        protected void onPostExecute(List<Apps> apps) {
                progress.setVisibility(View.GONE);
                mAdapter = new MyAppAdapter(getContext(), apps,"today");
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }


    }


    public long getStartTime(){
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        //TimeZone timeZone = calendar.getTimeZone();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long start = (calendar.getTimeInMillis());
        Log.d("Today Start",start+" ");

        return start;

    }


}
