package com.yatish.apptime.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.yatish.apptime.tabs.Month;
import com.yatish.apptime.tabs.Today;
import com.yatish.apptime.tabs.Week;

/**
 * Created by yatish on 16/6/17.
 */

public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                Today tab1 = new Today();
                return tab1;
            case 1:
                Week tab2 = new Week();
                return tab2;
            case 2:
                Month tab3 = new Month();
                return tab3;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}

