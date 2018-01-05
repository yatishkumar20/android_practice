package com.yatish.apptime.model;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.BitmapCompat;
import android.util.Log;


/**
 * Created by yatish on 16/6/17.
 */

public class Apps implements Parcelable,Comparable<Apps>{

    String label;
    Drawable icon;
    String packageName;
    long totalTimeForeground;
    long lastTimeUsed;
    int uid;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Override
    public int compareTo(@NonNull Apps o) {
        return Long.compare(o.totalTimeForeground,this.totalTimeForeground);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public long getTotalTimeForeground() {
        return totalTimeForeground;
    }

    public void setTotalTimeForeground(long totalTimeForeground) {
        this.totalTimeForeground = totalTimeForeground;
    }

    public long getLastTimeUsed() {
        return lastTimeUsed;
    }

    public void setLastTimeUsed(long lastTimeUsed) {
        this.lastTimeUsed = lastTimeUsed;
    }

    public Apps(String label,Drawable icon,String packageName,long totalTimeForeground,long lastTimeUsed,int uid){

        this.label = label;
        this.icon = icon;
        this.packageName = packageName;
        this.totalTimeForeground = totalTimeForeground;
        this.lastTimeUsed = lastTimeUsed;
        this.uid = uid;

    }


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label);
        dest.writeString(packageName);
        dest.writeLong(totalTimeForeground);
        dest.writeLong(lastTimeUsed);
        dest.writeInt(uid);
        //dest.writeValue(((BitmapDrawable)icon).getBitmap());
    }

    public static final Parcelable.Creator<Apps> CREATOR
            = new Parcelable.Creator<Apps>() {
        public Apps createFromParcel(Parcel in) {
            return new Apps(in);
        }

        public Apps[] newArray(int size) {
            return new Apps[size];
        }
    };

    private Apps(Parcel in) {
        label = in.readString();
        packageName = in.readString();
        totalTimeForeground = in.readLong();
        lastTimeUsed = in.readLong();
        uid = in.readInt();
        //icon = new BitmapDrawable(((Bitmap) in.readValue(Bitmap.class.getClassLoader())));

    }


}
