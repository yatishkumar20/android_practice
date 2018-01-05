package com.yatish.apptime;

import android.app.Application;
import android.content.Context;

/**
 * Created by yatish on 2/12/17.
 */

public class MyApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}
