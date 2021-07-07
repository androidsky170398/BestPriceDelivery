package com.tm.tarvemart.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

public class CityData implements Serializable {
    String id, city, state_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public static List<CityData> createJsonInList(String str) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<CityData>>() {
        }.getType();
        return gson.fromJson(str, type);
    }
}
