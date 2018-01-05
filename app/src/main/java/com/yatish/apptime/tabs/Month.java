package com.yatish.apptime.tabs;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
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
import java.util.List;
import java.util.TimeZone;

/**
 * Created by yatish on 19/6/17.
 */

public class Month extends Fragment {

    List<Apps> instdApp;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    boolean granted = false;
    View view;
    RecyclerView mRecyclerView;
    ProgressBar progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.today, container, false);
        view = rootView;
        init(view);
        return rootView;
    }


    public void init(View v) {

        mRecyclerView = (RecyclerView) v.findViewById(R.id.installed_apps);
        progress = (ProgressBar) v.findViewById(R.id.progress);

        new MonthAsync().execute();

        mLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAppAdapter(getContext(), new ArrayList<Apps>(),"month");
        mRecyclerView.setAdapter(mAdapter);

    }

    class MonthAsync extends AsyncTask<String,Void,List<Apps>>{
        @Override
        protected List<Apps> doInBackground(String... params) {

            List<Apps> installedAppsList = AppUsage.getAppUsageStatics(getStartTime());

            Collections.sort(installedAppsList);

            return installedAppsList;
        }

        @Override
        protected void onPostExecute(List<Apps> appses) {
                progress.setVisibility(View.GONE);
                mAdapter = new MyAppAdapter(getContext(), appses,"month");
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

        }
    }

    public long getStartTime(){

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long start = calendar.getTimeInMillis();
        return start;

    }

}
