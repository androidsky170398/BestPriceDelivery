package com.tm.tarvemart.entity;

import java.io.Serializable;

public class Order_Settings implements Serializable {
    String max_qty_per_product, cgst, sgst;

    public String getMax_qty_per_product() {
        return max_qty_per_product;
    }

    public void setMax_qty_per_product(String max_qty_per_product) {
        this.max_qty_per_product = max_qty_per_product;
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

    String is_cashback;

    public String getIs_cashback() {
        return is_cashback;
    }

    public void setIs_cashback(String is_cashback) {
        this.is_cashback = is_cashback;
    }


}
