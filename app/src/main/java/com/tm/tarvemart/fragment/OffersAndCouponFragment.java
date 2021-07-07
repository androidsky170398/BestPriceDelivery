package com.tm.tarvemart.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tm.tarvemart.Adapter.SectionsPagerAdapterForCoupon;
import com.tm.tarvemart.R;
import com.tm.tarvemart.entity.CouponData;
import com.tm.tarvemart.inerface.GlobleInterfce;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OffersAndCouponFragment extends Fragment implements GlobleInterfce {
    RecyclerView rv_coupon;
    TextView tv_NoCoupon;
    ViewPager vp_Coupon;
    String url = "coupon-list";
    List<CouponData> list_CouponData = new ArrayList<>();
    TextView tv_coupon_Avail, tv_coupon_Exp;
    SectionsPagerAdapterForCoupon sectionsPagerAdapterForCoupon;

    public OffersAndCouponFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_offers_and_coupon, container, false);
        //rv_coupon = (RecyclerView) view.findViewById(R.id.rv_coupon);
        //tv_NoCoupon = (TextView) view.findViewById(R.id.tv_NoCoupon);
        vp_Coupon = view.findViewById(R.id.vp_Coupon);
        tv_coupon_Avail = view.findViewById(R.id.tv_coupon_Avail);
        tv_coupon_Exp = view.findViewById(R.id.tv_coupon_Exp);
        sectionsPagerAdapterForCoupon = new SectionsPagerAdapterForCoupon(getActivity(), getChildFragmentManager());
        sectionsPagerAdapterForCoupon.instantiateItem(vp_Coupon, 0);
        vp_Coupon.setCurrentItem(0);
        vp_Coupon.setAdapter(sectionsPagerAdapterForCoupon);
        tv_coupon_Avail.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        tv_coupon_Avail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vp_Coupon.setCurrentItem(0);
            }
        });
        tv_coupon_Exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vp_Coupon.setCurrentItem(1);
            }
        });
        vp_Coupon.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    tv_coupon_Avail.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    tv_coupon_Exp.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else if (i == 1) {
                    tv_coupon_Avail.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    tv_coupon_Exp.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        return view;
    }
}
