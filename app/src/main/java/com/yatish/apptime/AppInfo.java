package com.yatish.apptime;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yatish.apptime.logic.AppUsage;
import com.yatish.apptime.model.Apps;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class AppInfo extends AppCompatActivity {

    List<Apps> instdApp;
    private static final long  MEGABYTE = 1024L * 1024L;
    ProgressBar progress;
    int uid;
    TextView tv_wifi,tv_sim1,tv_sim2;
    CardView card_net;
    String tab;
    public static final int PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_restriction);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        TextView app_name = (TextView) findViewById(R.id.app_name);
        TextView app_pkg = (TextView) findViewById(R.id.app_pkg);
        TextView tv_time = (TextView) findViewById(R.id.tv_time);
        tv_wifi = (TextView) findViewById(R.id.tv_wifi);
        tv_sim1 = (TextView) findViewById(R.id.tv_sim1);
        tv_sim2 = (TextView) findViewById(R.id.tv_sim2);
        card_net = (CardView) findViewById(R.id.card_net);

        progress = (ProgressBar) findViewById(R.id.progress);


        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null){

            Apps app = bundle.getParcelable("app");

            try {
                Drawable icon = this.getPackageManager().getApplicationIcon(app.getPackageName());
                imageView.setImageDrawable(icon);
            }catch (Exception e){
                e.printStackTrace();
            }

            //imageView.setImageDrawable(app.getIcon());
            app_name.setText(app.getLabel());
            app_pkg.setText(app.getPackageName());
            tv_time.setText(formatMs(app.getTotalTimeForeground()));
            uid = app.getUid();
        }

        Intent i = getIntent();
        if(i != null){
            tab = i.getStringExtra("tab");
            isPermissionGranted();

        }


    }

    class CalcAsync extends AsyncTask<String,Void,ArrayList<String>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(progress.getVisibility() == View.VISIBLE){
                progress.setVisibility(View.GONE);
            }else{
                progress.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            String tab = params[0];
            long start = 0;

            if(tab.equals("today")){
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                calendar.add(Calendar.DAY_OF_MONTH, 0);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                start = calendar.getTimeInMillis();

            }else if (tab.equals("week")){

                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                calendar.set(Calendar.DAY_OF_WEEK, calendar.getActualMinimum(Calendar.DAY_OF_WEEK));
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                start = calendar.getTimeInMillis();

            }else if (tab.equals("month")){

                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                start = calendar.getTimeInMillis();

            }

            ArrayList<String> values = new ArrayList<String>();

            try {
                long dataSim1 = AppUsage.getDataUsageStaticsSIM1(start, uid);
                long dataSim2 = AppUsage.getDataUsageStaticsSIM2(start, uid);
                long dataWifi = AppUsage.getDataUsageStaticsWifi(start, uid);

                values.add(getDynamicSpace(dataSim1));
                values.add(getDynamicSpace(dataSim2));
                values.add(getDynamicSpace(dataWifi));
            }catch (Exception e){
                e.printStackTrace();
            }

            return values;
        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {

            tv_sim1.setText(s.get(0));
            tv_sim2.setText(s.get(1));
            tv_wifi.setText(s.get(2));
            progress.setVisibility(View.GONE);
            card_net.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCompat.finishAfterTransition(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static String formatMs(long millis) {
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        long mins = TimeUnit.MILLISECONDS.toMinutes(millis);
        long secs = TimeUnit.MILLISECONDS.toSeconds(millis);

        //String time = null;

        if(hours == 0){

        }else if(mins == 0){

        }else{

        }

        String time = String.format("%dh %d min, %d sec",
                hours,
                mins - TimeUnit.HOURS.toMinutes(hours),
                secs - TimeUnit.MINUTES.toSeconds(mins)
        );

        if(hours == 0){

            time = time.replace("0h","");

        }

        if(mins == 0){

            time = time.replace("0 min,","");
        }


        return time;
    }

    public static long bytesToMeg(long bytes) {
        return bytes / MEGABYTE ;
    }

    public static String getDynamicSpace(long diskSpaceUsed)
    {
        if (diskSpaceUsed <= 0) {
            return "0";
        }

        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(diskSpaceUsed) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(diskSpaceUsed / Math.pow(1024, digitGroups))
                + " " + units[digitGroups];
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                new CalcAsync().execute(tab);
               // return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION);
                //return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //return true;
            //new CalcAsync().execute(tab);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if (Build.VERSION.SDK_INT >= 23) {
                    new CalcAsync().execute(tab);
                }

            }else{
                onBackPressed();
            }
        }

    }
}
