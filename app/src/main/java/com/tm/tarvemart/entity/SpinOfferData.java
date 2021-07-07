package com.tm.tarvemart.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class SpinOfferData {
    String offerPer, offerdate;

    public String getOfferPer() {
        return offerPer;
    }

    public void setOfferPer(String offerPer) {
        this.offerPer = offerPer;
    }

    public String getOfferdate() {
        return offerdate;
    }

    public void setOfferdate(String offerdate) {
        this.offerdate = offerdate;
    }

    public static String createJsonFromObject(SpinOfferData data) {
        Gson gson = new Gson();
        Type type = new TypeToken<SpinOfferData>() {
        }.getType();
        return gson.toJson(data, type);
    }

    public static SpinOfferData createObjectFromjson(String data) {
        Gson gson = new Gson();
        Type type = new TypeToken<SpinOfferData>() {
        }.getType();
        return gson.fromJson(data, type);
    }
}
