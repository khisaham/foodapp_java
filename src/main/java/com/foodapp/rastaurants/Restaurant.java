package com.foodapp.rastaurants;

import com.foodapp.commons.DbBoilerPlates;
import com.foodapp.commons.Log;
import com.foodapp.commons.SafHikariConnection;
import com.zaxxer.hikari.HikariDataSource;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Restaurant {
    public static String RestaurantLoginHandler(String request_data){
    JSONObject response = new JSONObject();
    if(request_data !=""){
    JSONObject requestData = new JSONObject(request_data);
    if(requestData.has("email") || requestData.has("phone_number")){
        if(requestData.has("password")){
            String whereString = "";
            String email = (requestData.has("email"))? requestData.getString("email") : "";
            String password = requestData.getString("password");
            String phoneNumber = (requestData.has("phone_number")) ? requestData.getString("phone_number") : "";
            if(email.isEmpty()){
                whereString = "phone_number = '"+phoneNumber+"'";
            }else if(phoneNumber.isEmpty()){
                whereString = "email = '"+email+"'";
            }
            else if(!phoneNumber.isEmpty() && !email.isEmpty()){
                whereString = "phone_number = '"+phoneNumber+"' and email = '"+email+"' ";
            }else{
                response.put("status", false);
                response.put("reason","Kindly Use your Phone Number or Email");

            }
            String selectSQL = "select * from restaurants where "+whereString+" and password = '"+password+"' ";
            int isValidCredentials = DbBoilerPlates.DoReturnInt(selectSQL,"id");
            if(isValidCredentials>0) {
                // Log.i(whereString);
                try {
                    HikariDataSource ds = SafHikariConnection.getDataSource();
                    Connection conn = ds.getConnection();
                    Statement stmt = conn.createStatement();

                    Log.i(selectSQL);
                    ResultSet rs = stmt.executeQuery(selectSQL);
                    while (rs.next()) {
                        response.put("name", rs.getString("name"));
                        response.put("city", rs.getString("city"));
                        response.put("country", rs.getString("country"));
                        response.put("location", rs.getString("location"));
                        response.put("email", rs.getString("email"));
                        response.put("phone_number", rs.getString("phone_number"));
                        response.put("opens_on", rs.getString("opens_on"));
                        response.put("extra_services", rs.getString("extra_services"));
                        response.put("description", rs.getString("description"));
                    }

                    rs.close();
                    stmt.close();
                    conn.close();
                } catch (Exception ex) {
                    Log.w("Sql error " + ex);
                }
            }else{
                response.put("status", false);
                response.put("reason", "Wrong Credentials");
            }
        }else{
            response.put("status", false);
            response.put("reason", "Password Field is mandatory");
        }
    }else{
        response.put("status", false);
        response.put("reason", "Fill all mandatory fields");
    }
    }else{
        response.put("status", false);
        response.put("reason", "Wrong request method");
    }
    return response.toString();
    }
    public static String NewRestaurant(String payload){
        JSONObject response = new JSONObject();
        if(payload!=""){
            JSONObject requestData = new JSONObject(payload);
            if(requestData.has("name") && requestData.has("city") && requestData.has("country") && requestData.has("phone_number") && requestData.has("email") && requestData.has("password")){
                String name = requestData.getString("name");
                String city = requestData.getString("city");
                String country = requestData.getString("country");
                String phoneNumber = requestData.getString("phone_number");
                String email = requestData.getString("email");
                String password = requestData.getString("password");
                String location = (requestData.has("location") && !requestData.getString("location").isEmpty()) ? requestData.getString("location") : "";
                String opensOn = (requestData.has("opens_on") && !requestData.getString("location").isEmpty()) ? requestData.getString("opens_on") : "Monday to Friday";
                String extraServices = (requestData.has("extra_services") && !requestData.getString("extra_services").isEmpty()) ? requestData.getString("extra_services") : "";
                String description = (requestData.has("description") && !requestData.getString("description").isEmpty()) ? requestData.getString("description") : "";
                String insertSql = "insert into restaurants (name, city, country, location, phone_number,email, password, opens_on, extra_services, description)"+
                        " values ('"+name+"', '"+city+"','"+country+"','"+location+"','"+phoneNumber+"', '"+email+"','"+password+"','"+opensOn+"', '"+extraServices+"', '"+description+"') ";
                int j = DbBoilerPlates.DoInserts(insertSql);
                if(j>0){
                    response.put("status", true);
                    response.put("reason", "New Restaurant Added Successfully. Login to Start creating Menu");
                }else{
                    response.put("status", false);
                    response.put("reason", "Could not create this Restaurant. Please Try again later");
                }
            }else{
                response.put("status", false);
                response.put("reason", "Fill all mandatory fields");
            }
        }else{
            response.put("status", false);
            response.put("reason", "You can connect outside defined parameters. Wrong Request Method");
        }
        return response.toString();
    }
}
