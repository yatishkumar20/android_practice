package com.yatish.apptime.logic;

import android.annotation.TargetApi;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.yatish.apptime.MyApplication;
import com.yatish.apptime.model.Apps;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static android.content.Context.TELEPHONY_SERVICE;
import static android.content.Context.TELEPHONY_SUBSCRIPTION_SERVICE;


/**
 * Created by yatish on 2/12/17.
 */

public class AppUsage {

    public static List<Apps> getAppUsageStatics(long start) {

        UsageStatsManager usageStatsManager = (UsageStatsManager) MyApplication.getContext().getSystemService(MyApplication.getContext().USAGE_STATS_SERVICE);

        List<UsageStats> stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, start, System.currentTimeMillis());

        PackageManager pm = MyApplication.getContext().getPackageManager();

        /*List<ApplicationInfo> apps = pm.getInstalledApplications(0);*/

        List<ApplicationInfo> installedApps = new ArrayList<>();


        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resInfos = pm.queryIntentActivities(intent, 0);
        // using hashset so that there will be no duplicate packages,
        // if no duplicate packages then there will be no duplicate apps
        HashSet<ApplicationInfo> apps = new HashSet<>();

        List<Apps> instdApp = new ArrayList<>();

        for (ResolveInfo resolveInfo : resInfos) {
            apps.add(resolveInfo.activityInfo.applicationInfo);
        }


        for (ApplicationInfo app : apps) {
            //checks for flags; if flagged, check if updated system app
       /*     if ((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                installedApps.add(app);
                //it's a system app, not interested
            } else if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                //Discard this one
                //in this case, it should be a user-installed app
            } else {*/
                installedApps.add(app);

                String label = (String) pm.getApplicationLabel(app);
                Drawable icon = pm.getApplicationIcon(app);
                String app_packageName = app.packageName;
                int uid = app.uid;
                long totalTimeForeground = 0;
                long lastTimeUsed = 0;

                //String pk_name = "";
                for (UsageStats us : stats) {

                    if (!app_packageName.equals("com.yatish.apptime") && app_packageName.equals(us.getPackageName())) {

                        // if(pk_name.equals("")) {
                        totalTimeForeground += us.getTotalTimeInForeground();
                        lastTimeUsed = us.getLastTimeUsed();
                           /* }else if(app_packageName.equals(pk_name)){
                        totalTimeForeground = totalTimeForeground + us.getTotalTimeInForeground();
                        lastTimeUsed = us.getLastTimeUsed();
                    }else{
                        totalTimeForeground = us.getTotalTimeInForeground();
                        lastTimeUsed = us.getLastTimeUsed();
                    }*/

                    }

                    //pk_name = app_packageName;
                }

                if (lastTimeUsed != 0 && lastTimeUsed > start) {
                    Apps insApp = new Apps(label, icon, app_packageName, totalTimeForeground, lastTimeUsed, uid);

                    instdApp.add(insApp);
                }

            }


        //}

        return instdApp;
    }


    public static List<Apps> getAppUsageStaticsToday(long start) {

        UsageStatsManager usageStatsManager = (UsageStatsManager) MyApplication.getContext().getSystemService(MyApplication.getContext().USAGE_STATS_SERVICE);

        List<UsageStats> stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, start, System.currentTimeMillis());

        PackageManager pm = MyApplication.getContext().getPackageManager();

        /*List<ApplicationInfo> apps = pm.getInstalledApplications(0);*/

        List<ApplicationInfo> installedApps = new ArrayList<>();


        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resInfos = pm.queryIntentActivities(intent, 0);
        // using hashset so that there will be no duplicate packages,
        // if no duplicate packages then there will be no duplicate apps
        HashSet<ApplicationInfo> apps = new HashSet<>();

        List<Apps> instdApp = new ArrayList<>();

        for (ResolveInfo resolveInfo : resInfos) {
            apps.add(resolveInfo.activityInfo.applicationInfo);
        }


        for (ApplicationInfo app : apps) {
            //checks for flags; if flagged, check if updated system app
       /*     if ((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                installedApps.add(app);
                //it's a system app, not interested
            } else if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                //Discard this one
                //in this case, it should be a user-installed app
            } else {*/
            installedApps.add(app);

            String label = (String) pm.getApplicationLabel(app);
            Drawable icon = pm.getApplicationIcon(app);
            String app_packageName = app.packageName;
            int uid = app.uid;
            long totalTimeForeground = 0;
            long lastTimeUsed = 0;

            //String pk_name = "";
            for (UsageStats us : stats) {

                if (!app_packageName.equals("com.yatish.apptime") && app_packageName.equals(us.getPackageName())) {

                    // if(pk_name.equals("")) {
                    totalTimeForeground += us.getTotalTimeInForeground();
                    lastTimeUsed = us.getLastTimeUsed();
                           /* }else if(app_packageName.equals(pk_name)){
                        totalTimeForeground = totalTimeForeground + us.getTotalTimeInForeground();
                        lastTimeUsed = us.getLastTimeUsed();
                    }else{
                        totalTimeForeground = us.getTotalTimeInForeground();
                        lastTimeUsed = us.getLastTimeUsed();
                    }*/

                }

                //pk_name = app_packageName;
            }

            if (lastTimeUsed != 0 && lastTimeUsed > start) {
                Apps insApp = new Apps(label, icon, app_packageName, totalTimeForeground, lastTimeUsed, uid);

                instdApp.add(insApp);
            }

        }


        //}

        return instdApp;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static long getDataUsageStaticsSIM1(long start,int uid){
        long currentYoutubeUsage = 0;
        try {
            //TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(TELEPHONY_SERVICE);
            //String subscriberID = tm.getSubscriberId();

            String imsi = null;
            TelephonyManager tm = (TelephonyManager) MyApplication.getContext().getSystemService(TELEPHONY_SERVICE);
            try {
                Method getSubId = TelephonyManager.class.getMethod("getSubscriberId", int.class);
                SubscriptionManager sm = (SubscriptionManager) MyApplication.getContext().getSystemService(TELEPHONY_SUBSCRIPTION_SERVICE);
                imsi = (String) getSubId.invoke(tm, sm.getActiveSubscriptionInfoForSimSlotIndex(0).getSubscriptionId()); // Sim slot 1 IMSI
                //return imsi;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }catch (Exception e){
                imsi = "";
            }
            //return imsi;
            if(!imsi.equals("")) {
                NetworkStatsManager networkStatsManager = MyApplication.getContext().getSystemService(NetworkStatsManager.class);
                NetworkStats networkStatsByApp = networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_MOBILE, imsi, start, System.currentTimeMillis(), uid);
                do {
                    NetworkStats.Bucket bucket = new NetworkStats.Bucket();
                    networkStatsByApp.getNextBucket(bucket);
                    if (bucket.getUid() == uid) {

                        currentYoutubeUsage += (bucket.getRxBytes() + bucket.getTxBytes());

                    }
                } while (networkStatsByApp.hasNextBucket());
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return currentYoutubeUsage;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static long getDataUsageStaticsSIM2(long start,int uid){
        long currentYoutubeUsage = 0;
        try {
            //TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(TELEPHONY_SERVICE);
            //String subscriberID = tm.getSubscriberId();

            String imsi = null;
            TelephonyManager tm = (TelephonyManager) MyApplication.getContext().getSystemService(TELEPHONY_SERVICE);
            try {
                Method getSubId = TelephonyManager.class.getMethod("getSubscriberId", int.class);
                SubscriptionManager sm = (SubscriptionManager) MyApplication.getContext().getSystemService(TELEPHONY_SUBSCRIPTION_SERVICE);
                imsi = (String) getSubId.invoke(tm, sm.getActiveSubscriptionInfoForSimSlotIndex(1).getSubscriptionId()); // Sim slot 2 IMSI
                //return imsi;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (Exception e){
                imsi = "";
            }
            //return imsi;

            if(!imsi.equals("")) {
                NetworkStatsManager networkStatsManager = MyApplication.getContext().getSystemService(NetworkStatsManager.class);
                NetworkStats networkStatsByApp = networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_MOBILE, imsi, start, System.currentTimeMillis(), uid);
                do {
                    NetworkStats.Bucket bucket = new NetworkStats.Bucket();
                    networkStatsByApp.getNextBucket(bucket);
                    if (bucket.getUid() == uid) {
                        currentYoutubeUsage += (bucket.getRxBytes() + bucket.getTxBytes());

                    }
                } while (networkStatsByApp.hasNextBucket());
            }

        }catch (Exception e){
            e.printStackTrace();
        }


        return currentYoutubeUsage;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static long getDataUsageStaticsWifi(long start,int uid){
        long currentYoutubeUsage = 0;
        try {
            //TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(TELEPHONY_SERVICE);
            //String subscriberID = tm.getSubscriberId();
            NetworkStatsManager networkStatsManager = MyApplication.getContext().getSystemService(NetworkStatsManager.class);
            NetworkStats networkStatsByApp = networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_WIFI, null, start, System.currentTimeMillis(), uid);
            do {
                NetworkStats.Bucket bucket = new NetworkStats.Bucket();
                networkStatsByApp.getNextBucket(bucket);
                if (bucket.getUid() == uid) {

                    currentYoutubeUsage += (bucket.getRxBytes() + bucket.getTxBytes());

                }
            } while (networkStatsByApp.hasNextBucket());

        }catch (Exception e){
            e.printStackTrace();
        }

        return currentYoutubeUsage;
    }
}