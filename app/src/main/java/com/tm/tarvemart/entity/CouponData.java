package com.tm.tarvemart.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CouponData {
    String id,
            coupon_number,
            amount,
            validity_start,
            validity_end,
            availibility,
            usable,
            availability;

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoupon_number() {
        return coupon_number;
    }

    public void setCoupon_number(String coupon_number) {
        this.coupon_number = coupon_number;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getValidity_start() {
        return validity_start;
    }

    public void setValidity_start(String validity_start) {
        this.validity_start = validity_start;
    }

    public String getValidity_end() {
        return validity_end;
    }

    public void setValidity_end(String validity_end) {
        this.validity_end = validity_end;
    }

    public String getAvailibility() {
        return availibility;
    }

    public void setAvailibility(String availibility) {
        this.availibility = availibility;
    }

    public String getUsable() {
        return usable;
    }

    public void setUsable(String usable) {
        this.usable = usable;
    }

    List<CouponData> available = new ArrayList<>();
    List<CouponData> expired = new ArrayList<>();

    public List<CouponData> getAvailable() {
        return available;
    }

    public void setAvailable(List<CouponData> available) {
        this.available = available;
    }

    public List<CouponData> getExpired() {
        return expired;
    }

    public void setExpired(List<CouponData> expired) {
        this.expired = expired;
    }

    public static List<CouponData> createJsonInList(String str) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<CouponData>>() {
        }.getType();
        return gson.fromJson(str, type);
    }
    public static CouponData createJsonInObject(String str) {
        Gson gson = new Gson();
        Type type = new TypeToken<CouponData>() {
        }.getType();
        return gson.fromJson(str, type);
    }
}
