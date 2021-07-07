package com.tm.tarvemart.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tm.tarvemart.R;
import com.tm.tarvemart.entity.CouponData;

import java.util.ArrayList;
import java.util.List;

public class AdapterForCoupon extends RecyclerView.Adapter<AdapterForCoupon.VH> {

    Context context;
    List<CouponData> list_Coupon = new ArrayList<>();
    List<CouponData> list_Coupon_for_Address;

    public AdapterForCoupon(Context context, List<CouponData> list_Coupon) {
        this.context = context;
        this.list_Coupon = list_Coupon;
    }

    public AdapterForCoupon(Context context, List<CouponData> list_Coupon, List<CouponData> list_Coupon_for_Address) {
        this.context = context;
        this.list_Coupon = list_Coupon;
        this.list_Coupon_for_Address = list_Coupon_for_Address;

    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VH(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_for_coupon, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, final int i) {
        vh.tv_couponNo.setText(list_Coupon.get(i).getCoupon_number());
        vh.tv_Amount.setText(list_Coupon.get(i).getAmount());
        vh.tv_valid.setText(list_Coupon.get(i).getValidity_start() + " To " + list_Coupon.get(i).getValidity_end());
        vh.tv_Availability.setText(list_Coupon.get(i).getAvailability());
        if (list_Coupon.get(i).getUsable() != null && !list_Coupon.get(i).getUsable().equalsIgnoreCase("")) {
            vh.tv_Usable.setText(list_Coupon.get(i).getUsable());
        } else {
            vh.ll_uses.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return list_Coupon.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView tv_couponNo,
                tv_Amount,
                tv_valid,
                tv_Availability,
                tv_Usable;
        LinearLayout ll_uses;

        public VH(@NonNull View itemView) {
            super(itemView);
            tv_couponNo = itemView.findViewById(R.id.tv_couponNo);
            tv_Amount = itemView.findViewById(R.id.tv_Amount);
            tv_valid = itemView.findViewById(R.id.tv_valid);
            tv_Availability = itemView.findViewById(R.id.tv_Availability);
            tv_Usable = itemView.findViewById(R.id.tv_Usable);
            ll_uses = itemView.findViewById(R.id.ll_uses);
        }
    }
}
