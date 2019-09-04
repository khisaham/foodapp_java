package com.foodapp.customer;

import com.foodapp.commons.DbBoilerPlates;
import com.foodapp.commons.Log;
import com.foodapp.commons.SafHikariConnection;
import com.zaxxer.hikari.HikariDataSource;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Menu {
    public static String CreateNewCategory(String payload){
        JSONObject response = new JSONObject();
        if(payload!=""){
            JSONObject requestedData = new JSONObject(payload);
        if(requestedData.has("restaurant_id") && requestedData.has("name")){
            String categoryName = requestedData.getString("name").toUpperCase();
            String restId = requestedData.getString("restaurant_id");
            String picture = (requestedData.has("picture") && !requestedData.getString("picture").isEmpty()) ? requestedData.getString("picture") : "";

            //check if this restaurant has already created this category
            if(DbBoilerPlates.DoReturnInt("select * from food_category where name = '"+categoryName+"' and restaurant_id = '"+restId+"' ", "id") < 1){
                String insertSQL = "insert into food_category (name, picture, restaurant_id) values ('"+categoryName+"','"+picture+"','"+restId+"')";
                int j = DbBoilerPlates.DoInserts(insertSQL);
                if(j>0){
                    response.put("status", false);
                    response.put("reason", "Category added successfully");
                }else{
                    response.put("status", false);
                    response.put("reason", "Unable to add this category. Try again later");
                }
            }else{
                response.put("status", false);
                response.put("reason", "This category is already set");
            }
        }
        }else{
            response.put("status", false);
            response.put("reason", "Unauthorized method request");
        }
        return  response.toString();
    }

    public static String CreateNewMenu(String payload){
        JSONObject response = new JSONObject();
        if(payload!=""){
            JSONObject requestedData = new JSONObject(payload);
        if(requestedData.has("name") && requestedData.has("restaurant_id") && requestedData.has("category_id") && requestedData.has("selling_price")){
            String name = requestedData.getString("name").toUpperCase();
            Double buyingPrice = (requestedData.has("buying_price") && !requestedData.getString("buying_price").isEmpty()) ? Double.parseDouble(requestedData.getString("buying_price")) : 0D;
            Double sellingPrice = (requestedData.has("selling_price") && !requestedData.getString("selling_price").isEmpty()) ? Double.parseDouble(requestedData.getString("selling_price")) : 0D;
            int categoryId = Integer.parseInt(requestedData.getString("category_id"));
            int restaurantId = Integer.parseInt(requestedData.getString("restaurant_id"));


            String units = (requestedData.has("units")) ? requestedData.getString("units") : "";
            int quantity = Integer.parseInt(requestedData.getString("quantity"));
            String picture1 = (requestedData.has("picture1")) ? requestedData.getString("picture1") : "default1.png";
            String picture2 = (requestedData.has("picture2")) ? requestedData.getString("picture2") : "default2.png";
            String picture3 = (requestedData.has("picture3")) ? requestedData.getString("picture3") : "default3.png";
            String picture4 = (requestedData.has("picture4")) ? requestedData.getString("picture4") : "default4.png";
            String servedWith = (requestedData.has("served_with")) ? requestedData.getString("served_with") : "";
            String description = (requestedData.has("description")) ? requestedData.getString("description"):"";
            String insertSQl = "insert into menu (name, buying_price, selling_price, category_id, restaurant_id, quantity, units, picture1, picture2, picture3, picture4, served_with, descriptions) "+
                    " values ('"+name+"','"+buyingPrice+"','"+sellingPrice+"','"+categoryId+"','"+restaurantId+"','"+quantity+"','"+units+"','"+picture1+"','"+picture2+"','"+picture3+"','"+picture4+"','"+servedWith+"','"+description+"') ";
            if(DbBoilerPlates.DoReturnInt("select * from menu where name = '"+name+"' and restaurant_id = '"+restaurantId+"' ","id") > 0){
                response.put("status", false);
                response.put("reason", "This menu already exist");
            }else{
                int j = DbBoilerPlates.DoInserts(insertSQl);
                if(j>0){
                    response.put("status", true);
                    response.put("reason", "Your Menu Created Successfully");
                }else{
                    response.put("status", false);
                    response.put("reason", "Unable to create your menu. Please try again later");
                }
            }

        }
        }else{
            response.put("status", false);
            response.put("reason", "Unauthorized method request");
        }
        return  response.toString();
    }

    public static JSONObject getAllMenu(){
        JSONObject finalResponse = new JSONObject();
        //TODO: use jwt to validate this request
        String selectSQL = "select * from restaurants where is_closed = 0 ";
        try {
            HikariDataSource ds = SafHikariConnection.getDataSource();
            Connection conn = ds.getConnection();
            Statement stmt = conn.createStatement();

            Log.i(selectSQL);
            ResultSet rs = stmt.executeQuery(selectSQL);
            JSONArray array = new JSONArray();
            while (rs.next()) {
                //restaurant data
                JSONObject response = new JSONObject();
                response.put("rest_id", rs.getString("id"));
                response.put("rest_name", rs.getString("name"));
                response.put("rest_city", rs.getString("city"));
                response.put("rest_country", rs.getString("country"));
                response.put("rest_location", rs.getString("location"));
                response.put("rest_phone_number", rs.getString("phone_number"));
                response.put("rest_email", rs.getString("email"));
                response.put("rest_opens_on", rs.getString("opens_on"));
                response.put("rest_extra_services", rs.getString("extra_services"));
                response.put("descriptions", rs.getString("description"));
                response.put("categories", getAllCategories(rs.getString("id")));
            array.put(response);

            }
            finalResponse.put("data", array);

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception ex) {
            Log.w("Sql error on executing menu query " + ex);
        }
        return finalResponse;
    }
    private static JSONObject getAllCategories(String id){
    JSONObject finalResponse = new JSONObject();
    String selectSQL = "select * from food_category where is_active = 1 and restaurant_id = '"+id+"' ";
        try {
            HikariDataSource ds = SafHikariConnection.getDataSource();
            Connection conn = ds.getConnection();
            Statement stmt = conn.createStatement();

            Log.i(selectSQL);
            ResultSet rs = stmt.executeQuery(selectSQL);
            JSONArray array = new JSONArray();
            while (rs.next()) {
                //restaurant data
                JSONObject response = new JSONObject();
                response.put("cat_id", rs.getString("id"));
                response.put("cat_name", rs.getString("name"));
                response.put("cat_picture", rs.getString("picture"));
                response.put("cat_date_created", rs.getString("date_created"));
                response.put("menu", getAllFoodMenu(rs.getString("id"), id));
                array.put(response);

            }
            finalResponse.put("categories", array);

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception ex) {
            Log.w("Sql error on executing category query " + ex);
        }
    return finalResponse;
    }

    private static JSONObject getAllFoodMenu(String cat_id, String restaurant_id){
        JSONObject finalResponse = new JSONObject();
        String selectSQL = "select * from menu where restaurant_id = '"+restaurant_id+"' and category_id ='"+cat_id+"' ";
        try {
            HikariDataSource ds = SafHikariConnection.getDataSource();
            Connection conn = ds.getConnection();
            Statement stmt = conn.createStatement();

            Log.i(selectSQL);
            ResultSet rs = stmt.executeQuery(selectSQL);
            JSONArray array = new JSONArray();
            while (rs.next()) {
                //restaurant data
                JSONObject response = new JSONObject();
                response.put("menu_id", rs.getString("id"));
                response.put("menu_name", rs.getString("name"));
                response.put("buying_price", rs.getString("buying_price"));
                response.put("selling_price", rs.getString("selling_price"));
                response.put("category_id", rs.getString("category_id"));
                response.put("available", rs.getString("available"));
                response.put("quantity", rs.getString("quantity"));
                response.put("units", rs.getString("units"));
                response.put("served_with", rs.getString("served_with"));
                response.put("descriptions", rs.getString("descriptions"));
                response.put("menu_picture1", rs.getString("picture1"));
                response.put("menu_picture2", rs.getString("picture2"));
                response.put("menu_picture3", rs.getString("picture3"));
                response.put("menu_picture4", rs.getString("picture4"));


                array.put(response);

            }
            finalResponse.put("menu", array);

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception ex) {
            Log.w("Sql error on executing menu query " + ex);
        }
        return finalResponse;
    }
}
