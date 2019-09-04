package com.foodapp.http;

import com.squareup.okhttp.*;
import org.json.JSONObject;

public class HttpHandShake {


    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");



    public static String HandShake(String requestPayload)
    {

        String result = "";
        try {

            JSONObject jsonObjectRequest=new JSONObject(requestPayload);


            String json = "";
            json = jsonObjectRequest.getJSONObject("request").toString();



            OkHttpClient client = new OkHttpClient();


            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(jsonObjectRequest.getString("url"))
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            result= response.body().string();

        } catch (Exception e) {
            //  Log.d("InputStream", e.getLocalizedMessage());
            System.out.println("InputStream"+ e.getLocalizedMessage());
            System.out.println("InputStream Error "+ e);
        }

        System.out.println("result"+ result);
        return result;
    }
}
