package com.tm.tarvemart.entity;

import java.io.Serializable;

public class BillingData implements Serializable {
    String pay_mode, actual_price, wallet_used, wallet_money, cashback_applied,
            cashback_percent,
            cashback_amount,
            coupan_used,
            coupan_id,
            coupan_code,
            coupan_price,
            total_amount_payable,
            delivery_type,
            delivery_time,
            delivery_date,
            cgst,
            sgst,payment_status,payment_datetime,txtId,spinner_percent;

    public String getPay_mode() {
        return pay_mode;
    }

    public void setPay_mode(String pay_mode) {
        this.pay_mode = pay_mode;
    }

    public String getPayment_status(){
        return payment_status;
    }
    public void setPayment_status(String payment_status){
        this.payment_status=payment_status;
    }

    public String getPayment_datetime(){
        return payment_datetime;
    }
    public void setPayment_datetime(String payment_datetime){
        this.payment_datetime=payment_datetime;
    }

    public String getTxtId(){
        return txtId;
    }
    public void setTxtId(String txtId){
        this.txtId=txtId;
    }

    public String getSpinner_percent(){
        return spinner_percent;
    }
    public void setSpinner_percent(String spinner_percent){
        this.spinner_percent=spinner_percent;
    }



    public String getActual_price() {
        return actual_price;
    }

    public void setActual_price(String actual_price) {
        this.actual_price = actual_price;
    }

    public String getWallet_used() {
        return wallet_used;
    }

    public void setWallet_used(String wallet_used) {
        this.wallet_used = wallet_used;
    }

    public String getWallet_money() {
        return wallet_money;
    }

    public void setWallet_money(String wallet_money) {
        this.wallet_money = wallet_money;
    }

    public String getCashback_applied() {
        return cashback_applied;
    }

    public void setCashback_applied(String cashback_applied) {
        this.cashback_applied = cashback_applied;
    }

    public String getCashback_percent() {
        return cashback_percent;
    }

    public void setCashback_percent(String cashback_percent) {
        this.cashback_percent = cashback_percent;
    }

    public String getCashback_amount() {
        return cashback_amount;
    }

    public void setCashback_amount(String cashback_amount) {
        this.cashback_amount = cashback_amount;
    }

    public String getCoupan_used() {
        return coupan_used;
    }

    public void setCoupan_used(String coupan_used) {
        this.coupan_used = coupan_used;
    }

    public String getCoupan_id() {
        return coupan_id;
    }

    public void setCoupan_id(String coupan_id) {
        this.coupan_id = coupan_id;
    }

    public String getCoupan_code() {
        return coupan_code;
    }

    public void setCoupan_code(String coupan_code) {
        this.coupan_code = coupan_code;
    }

    public String getCoupan_price() {
        return coupan_price;
    }

    public void setCoupan_price(String coupan_price) {
        this.coupan_price = coupan_price;
    }

    public String getTotal_amount_payable() {
        return total_amount_payable;
    }

    public void setTotal_amount_payable(String total_amount_payable) {
        this.total_amount_payable = total_amount_payable;
    }

    public String getDelivery_type() {
        return delivery_type;
    }

    public void setDelivery_type(String delivery_type) {
        this.delivery_type = delivery_type;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getCgst() {
        return cgst;
    }

    public void setCgst(String cgst) {
        this.cgst = cgst;
    }

    public String getSgst() {
        return sgst;
    }

    public void setSgst(String sgst) {
        this.sgst = sgst;
    }
}
