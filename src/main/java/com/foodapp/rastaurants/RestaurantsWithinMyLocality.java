package com.foodapp.rastaurants;

import org.json.JSONArray;
import org.json.JSONObject;

public class RestaurantsWithinMyLocality {
    public static JSONObject RestaurantsWithinMyArea(String payload, String geoData){
        JSONObject finalResponse = new JSONObject();
        String[] myLocation = GetMyLocation(geoData).split("_", 2);
        return finalResponse;
    }
    private static String GetMyLocation(String geoLocationData){
        JSONObject requestData = new JSONObject(geoLocationData);
        String lnLat = requestData.getString("lng")+"_"+requestData.getString("lat");
        return lnLat;
    }
}
