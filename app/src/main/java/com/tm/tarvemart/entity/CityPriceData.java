package com.tm.tarvemart.entity;

import java.io.Serializable;

public class CityPriceData implements Serializable {

    String id,
            city_id,
            product_id,
            quantity_id,
            price,
            selling_price,
            percent_off,
            total_availability,
            updated_at,
            quantity_name,
            status,
            created_at,
            CommQty,
            city_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getQuantity_id() {
        return quantity_id;
    }

    public void setQuantity_id(String quantity_id) {
        this.quantity_id = quantity_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(String selling_price) {
        this.selling_price = selling_price;
    }

    public String getDiscount() {
        return percent_off;
    }

    public void setDiscount(String percent_off) {
        this.percent_off = percent_off;
    }

    public String getTotal_availability() {
        return total_availability;
    }

    public void setTotal_availability(String total_availability) {
        this.total_availability = total_availability;
    }




    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getQuantity_name() {
        return quantity_name;
    }

    public void setQuantity_name(String quantity_name) {
        this.quantity_name = quantity_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }
}
