package com.foodapp.orders;

import com.foodapp.commons.DbBoilerPlates;
import org.json.JSONObject;

public class NewOrder {
    public static JSONObject newOrder(String payload){
        JSONObject finalResponse = new JSONObject();
        if(!payload.isEmpty()){
            JSONObject requestedData = new JSONObject(payload);
            if(requestedData.has("restaurant_id") && requestedData.has("menu_id") && requestedData.has("datetime_ordered") && requestedData.has("geoData") && requestedData.has("quantity") && requestedData.has("customer_id")){
                int restaurantId = Integer.parseInt(requestedData.getString("restaurant_id"));
                String orderCode = generateUniqueOrderCode();
                int menuId = Integer.parseInt(requestedData.getString("menu_id"));
                String datetimeOrdered = requestedData.getString("datetime_ordered");
                String amountToPay = requestedData.getString("amount_to_pay");
                String datetimeToPick = (requestedData.has("datetime_to_pick") && ! requestedData.getString("datetime_to_pick").isEmpty())?requestedData.getString("datetime_to_pick") : "CURRENT_TIMESTAMP()";
                int customerId = Integer.parseInt(requestedData.getString("customer_id"));
                String quantity = requestedData.getString("quantity");
                String geoData = requestedData.getString("geoData"); // splitting geoData will give pickup point, dropoff point, waypoints and addresses for the order map
                String description = requestedData.getString("description");
                String parentOrder = generateAParentOrder(orderCode);
                String extraInfo = requestedData.getString("extra_info");
                String insertSQL = "insert into orders (order_code, menu_id, amount_to_pay, our_commission, amount_paid, customer_id, datetime_ordered, pickup_point, dropoff_point, order_map, description, parent_order, extra_info_from_customer,datetime_to_pick) "+
                        "values ('"+orderCode+"','"+menuId+"','"+amountToPay+"') ";
                if(orderCodeExist(orderCode, customerId)){
                    finalResponse.put("status", false);
                    finalResponse.put("reason", "You have already submitted a similar order");
                }
                int j = DbBoilerPlates.DoInserts(insertSQL);
            }else{
                finalResponse.put("status", false);
                finalResponse.put("reason", "Correct all the errors to continue");
            }
        }else{
            finalResponse.put("status", false);
            finalResponse.put("reason", "Unathorized access outside defined parameters");
        }
        return finalResponse;
    }

    private static String generateUniqueOrderCode(){
        //TODO generate a randomn unique order code using Lazy Algorithm (criteria, use last row id from orders table, use regex [0-3]([a-c]|[e-g]{1,2}) pattern)
        String finalCode = "[0-3]([a-c]|[e-g]{1,2})";
        return finalCode;
    }

    private static String generateAParentOrder(String childOrder){
        //TODO: create lazy algorithm that helps in randomn number generating
//        String parentOrder = LazyAlgorithm.lazySyntax(childOrder);
        return childOrder;
    }

    private static boolean orderCodeExist(String order_code, int customer_id){
        if(DbBoilerPlates.DoReturnInt("select * from orders where order_code = '"+order_code+"' and customer_id = '"+customer_id+"'", "id") > 0){
            return true;
        }
        return false;
    }
}
