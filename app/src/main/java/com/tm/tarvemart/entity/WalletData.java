package com.tm.tarvemart.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class WalletData implements Serializable {
    String wallet_amount;
    User userdetails = new User();
    Order_Settings order_setting = new Order_Settings();
    Order_Settings is_cashback_applied = new Order_Settings();
    Default_Images default_images = new Default_Images();

    public String getWallet_amount() {
        return wallet_amount;
    }

    public void setWallet_amount(String wallet_amount) {
        this.wallet_amount = wallet_amount;
    }

    public User getUserdetails() {
        return userdetails;
    }

    public void setUserdetails(User userdetails) {
        this.userdetails = userdetails;
    }

    public Order_Settings getOrder_setting() {
        return order_setting;
    }

    public void setOrder_setting(Order_Settings order_setting) {
        this.order_setting = order_setting;
    }

    public Order_Settings getIs_cashback_applied() {
        return is_cashback_applied;
    }

    public void setIs_cashback_applied(Order_Settings is_cashback_applied) {
        this.is_cashback_applied = is_cashback_applied;
    }

    public Default_Images getDefault_images() {
        return default_images;
    }

    public void setDefault_images(Default_Images default_images) {
        this.default_images = default_images;
    }

    public static WalletData createJsonInList(String str) {
        Gson gson = new Gson();
        Type type = new TypeToken<WalletData>() {
        }.getType();
        return gson.fromJson(str, type);
    }

    public static List<WalletData> createJsonInList1(String str) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<WalletData>>() {
        }.getType();
        return gson.fromJson(str, type);
    }



    String id,
            customer_id,
    order_id,
            coupon_id,
    coupan_code,
            points,
    percent,
            type,
    status,
            remark,
    comment,
            updated_at,
    created_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getCoupan_code() {
        return coupan_code;
    }

    public void setCoupan_code(String coupan_code) {
        this.coupan_code = coupan_code;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    String
            amount,
    transaction_date;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }
}
