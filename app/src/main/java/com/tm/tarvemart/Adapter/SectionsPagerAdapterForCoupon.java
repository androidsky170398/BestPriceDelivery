package com.tm.tarvemart.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tm.tarvemart.fragment.CouponAdapterFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapterForCoupon extends FragmentPagerAdapter {

    private final Context mContext;

    public SectionsPagerAdapterForCoupon(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return CouponAdapterFragment.newInstance(position + 1);
    }


   /* @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }*/
    @Override
    public int getItemPosition(Object object) {
        /*if (object instanceof MyFragment) {
            // Create a new method notifyUpdate() in your fragment
            // it will get call when you invoke
            // notifyDatasetChaged();
            ((MyFragment) object).notifyUpdate();
        }
        //don't return POSITION_NONE, avoid fragment recreation.
        return super.getItemPosition(object);*/
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}