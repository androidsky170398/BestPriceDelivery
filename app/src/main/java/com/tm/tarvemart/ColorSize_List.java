package com.tm.tarvemart;

import java.io.Serializable;

public class ColorSize_List implements Serializable {
    private  String color_name, color_code,color_id;

    public ColorSize_List(String color_name, String color_code,
                     String color_id) {
        this.color_name=color_name;
        this.color_code=color_code;
        this.color_id=color_id;
    }

    public String getColor_name(){
        return color_name;
    }
    public void setColor_name(String color_name) {
        this.color_name = color_name;
    }


    public String getColor_code() {
        return color_code;
    }
    public void setColor_code(String color_code) {
        this.color_code = color_code;
    }



    public String getColor_id(){
        return color_id;
    }
    public void setColor_id(String colorid) {
        this.color_id = color_id;
    }


    private  String quantity_name, size_id;
    public ColorSize_List(String quantity_name, String size_id) {
        this.quantity_name=quantity_name;
        this.size_id=size_id;
    }

    public String getQuantity_name(){
        return quantity_name;
    }
    public void setQuantity_name(String quantity_name) {
        this.quantity_name = quantity_name;
    }




    public String getSize_id() {
        return size_id;
    }
    public void setSize_id(String size_id) {
        this.size_id = size_id;
    }


}
