package com.tm.tarvemart.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CreateOrder implements Serializable {
     List<ProductData> products = new ArrayList<>();
     AddressData delivery_address = new AddressData();
     BillingData billing_detail = new BillingData();
     String customer_id;
     String city_id;


    public List<ProductData> getProducts() {
        return products;
    }

    public void setProducts(List<ProductData> products) {
        this.products = products;
    }

    public AddressData getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(AddressData delivery_address) {
        this.delivery_address = delivery_address;
    }

    public BillingData getBilling_detail() {
        return billing_detail;
    }

    public void setBilling_detail(BillingData billing_detail) {
        this.billing_detail = billing_detail;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }
}
