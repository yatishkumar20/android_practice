package com.yatish.apptime.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yatish.apptime.AppInfo;
import com.yatish.apptime.MainActivity;
import com.yatish.apptime.R;
import com.yatish.apptime.model.Apps;
import com.yatish.apptime.tabs.Today;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by yatish on 16/6/17.
 */

public class MyAppAdapter extends RecyclerView.Adapter<MyAppAdapter.MyViewHolder> {

    private List<Apps> mDataset;
    private Context mContext;
    private String frag;

    public MyAppAdapter(@NonNull Context context, List<Apps> myDataset, String frag) {
        mDataset = myDataset;
        this.mContext = context;
        this.frag = frag;
    }

    public static String getFormattedDate(long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "MMMM d, h:mm aa";
        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
            return "" + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
            return "Yesterday, " + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        } else {
            return DateFormat.format("dd/MM/yy, h:mm aa", smsTime).toString();
        }
    }

    public static String totalTimeInString(long timeInMilis) {


        if (timeInMilis == 0) {

            return "0";

        } else {
            /*Date date = new Date(timeInMilis);
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            String dateFormatted = formatter.format(date);

            //return dateFormatted;*/
            String hms = "0";

            try {
                hms = String.format("%02dh%02dm%02ds", TimeUnit.MILLISECONDS.toHours(timeInMilis),
                        TimeUnit.MILLISECONDS.toMinutes(timeInMilis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeInMilis)),
                        TimeUnit.MILLISECONDS.toSeconds(timeInMilis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeInMilis)));
                //System.out.println(hms);
            } catch (Exception e) {

            }

            return hms;

        }


    }

    public static String formatMs(long millis) {
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        long mins = TimeUnit.MILLISECONDS.toMinutes(millis);
        long secs = TimeUnit.MILLISECONDS.toSeconds(millis);

        //String time = null;

        if (hours == 0) {

        } else if (mins == 0) {

        } else {

        }

        String time = String.format("%dh %d min, %d sec",
                hours,
                mins - TimeUnit.HOURS.toMinutes(hours),
                secs - TimeUnit.MINUTES.toSeconds(mins)
        );

        if (hours == 0) {

            time = time.replace("0h", "");

        }

        if (mins == 0) {

            time = time.replace("0 min,", "");
        }


        return time;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;

        LayoutInflater inflater = LayoutInflater.from(mContext);

        view = inflater.inflate(R.layout.app_info, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Apps app = mDataset.get(position);
        holder.app_name.setText(app.getLabel());
        holder.app_icon.setImageDrawable(app.getIcon());
        holder.app_last_used.setText(getFormattedDate(app.getLastTimeUsed()));
        holder.total_time.setText(formatMs(app.getTotalTimeForeground()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appInfo = new Intent(mContext, AppInfo.class);

                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation((MainActivity) mContext, v.findViewById(R.id.app_icon), "icon");

                Bundle bundle = new Bundle();
                bundle.putParcelable("app", app);
                appInfo.putExtras(bundle);
                appInfo.putExtra("tab", frag);
                mContext.startActivity(appInfo, options.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView app_name;
        public ImageView app_icon;
        public TextView total_time;
        public TextView app_last_used;

        public MyViewHolder(View view) {
            super(view);
            app_name = (TextView) view.findViewById(R.id.app_name);
            app_icon = (ImageView) view.findViewById(R.id.app_icon);
            app_last_used = (TextView) view.findViewById(R.id.app_last_used);
            total_time = (TextView) view.findViewById(R.id.total_time);
        }
    }
}
