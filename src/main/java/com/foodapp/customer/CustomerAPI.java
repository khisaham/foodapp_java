package com.foodapp.customer;

import com.foodapp.commons.DbBoilerPlates;
import com.foodapp.commons.Log;
import com.foodapp.commons.SafHikariConnection;
import com.zaxxer.hikari.HikariDataSource;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CustomerAPI {
public static String LoginHandler(String request_data){
    JSONObject response = new JSONObject();
    Log.i(request_data);
    if(request_data.isEmpty()){
        response.put("status", false);
        response.put("reason","All Fields are required");
    }else{
        JSONObject requestedData = new JSONObject(request_data);
        if(requestedData.has("phone_number") || requestedData.has("email")){
            if(requestedData.has("password")){
                String whereString = "";
                String email = (requestedData.has("email"))? requestedData.getString("email") : "";
                String password = requestedData.getString("password");
                String phoneNumber = (requestedData.has("phone_number")) ? requestedData.getString("phone_number") : "";
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
                String selectSQL = "select * from customer where "+whereString+" and password = '"+password+"' ";
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
//                  if(rs.next()){
                            response.put("first_name", rs.getString("first_name"));
                            response.put("last_name", rs.getString("last_name"));
                            response.put("surname", rs.getString("surname"));
                            response.put("phone_number", rs.getDouble("phone_number"));
                            response.put("email", rs.getString("email"));
                            response.put("passport_number", rs.getDouble("passport_number"));
                            response.put("gender", rs.getString("gender"));
                            response.put("iscooperate_account", rs.getInt("iscooperate"));
                            response.put("dob", rs.getDate("dob"));
                            response.put("date_registered", rs.getDate("date_registered"));
                            response.put("profile_pic", rs.getString("ppic"));
                            response.put("descriptions", rs.getString("description"));
                            // }
//                  else{
//                      response.put("status", false);
//                      response.put("reason", "Wrong Credentials");
//                  }
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
                response.put("reason","Kindly Enter you password");
            }
        }else{
            response.put("status", false);
            response.put("reason","Fill all Mandatory Fields");
        }
    }
    return response.toString();
}


public static String createNewCustomer(String payload){
    JSONObject response = new JSONObject();
    if(!payload.isEmpty()){
    JSONObject requestData = new JSONObject(payload);
    if(requestData.has("email") || requestData.has("phone_number")){

        if(requestData.getDouble("phone_number")>0 && (DbBoilerPlates.DoReturnInt("select * from customer where phone_number = '"+requestData.getDouble("phone_number")+"'","id") < 1)) {
            String fisrtName = (requestData.has("first_name") && !requestData.getString("first_name").isEmpty() ? requestData.getString("first_name") : "not set");
            String lastName = (requestData.has("last_name") && !requestData.getString("last_name").isEmpty() ? requestData.getString("last_name") : "not set");
            String surName = (requestData.has("surname") && !requestData.getString("surname").isEmpty() ? requestData.getString("surname") : "Doe");
            Double phoneNumber = requestData.getDouble("phone_number");
            String email = requestData.getString("email");
            Double passport_number = (requestData.has("passport_number") && requestData.getDouble("passport_number") > 0D ? requestData.getDouble("passport_number") : 0D);
            String password = requestData.getString("password");
            String insertSQL = "insert into customer (email, first_name, last_name, surname, phone_number, passport_number) values('" + email + "','" + fisrtName + "','" + lastName + "','" + surName + "','" + phoneNumber + "','" + passport_number + "')";
            int j = DbBoilerPlates.DoInserts(insertSQL);
            if (j > 0) {
                response.put("status", true);
                response.put("reason", "Customer Created Successfully");
            } else {
                response.put("status", false);
                response.put("reason", "We could not register you at this time. Try later please");
            }
        } else{
            response.put("status", false);
            response.put("reason", "A user with same details exist");
        }
    }else{
        response.put("status", false);
        response.put("reason", "You must Fill at least Phone Number or Email");
    }
    }else{
        response.put("status", false);
        response.put("reason", "Cannot access this service outside defined perimeter");
    }
    return response.toString();
}
}
