package com.foodapp.http;

import com.foodapp.commons.DbBoilerPlates;
import com.foodapp.commons.Log;
import org.json.JSONObject;

public class TLSSecurity {

    public static String TLSHandshake(String requestedObject){

        JSONObject response = new JSONObject();
        JSONObject requestPayload = new JSONObject(requestedObject);

        if(requestPayload.toString()!=""){
            Log.d("Message:"+requestedObject);
            String lat = requestPayload.getString("lat");
            String lng = requestPayload.getString("lng");
            String phoneNumber = requestPayload.getString("phone_number");
            String simCardSn = requestPayload.getString("simcard_sn");
            String alertMessage = requestPayload.getString("alert_message");

            String sqlQuery = "insert into tbl_alerts (phone_number, lat, lng, simcard_sn, alert_message) values ('"+phoneNumber+"','"+lat+"','"+lng+"','"+simCardSn+"','"+alertMessage+"')";

          int insertKey = DbBoilerPlates.DoInserts(sqlQuery);
          if(insertKey>0){
              response.put("status", true);
              response.put("reason", "Success");
          }
        }else{
            response.put("status", false);
            response.put("reason", "did not send any data");
        }
        return response.toString();

    }
}
