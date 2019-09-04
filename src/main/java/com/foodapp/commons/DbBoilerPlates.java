package com.foodapp.commons;


import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;

/** Handles data store operation boiler plates
 * @author khisahamphrey
 */
public class DbBoilerPlates {

    /**
     *This boiler plate returns an int from a data source. It defaults to 0 if a record is not found or fails for any reason
     * @param passedSQL
     * @param field
     * @return
     */
    public static int DoReturnInt(String passedSQL, String field) {

        int RetunInt = 0;

        try {
            HikariDataSource ds = SafHikariConnection.getDataSource();
            Connection conn = ds.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(passedSQL.trim());
            while (rs.next()) {
                RetunInt = rs.getInt(field);
            }
            rs.close();
            stmt.close();
            conn.close();
        }catch (SQLException ex) {
            Log.e("An error occurred while trying to execute a select query", ex);
        }

        return RetunInt;
    }

    /**
     *This boiler plate returns a long from a data source. It defaults to 0L if a record is not found or fails for any reason
     * @param passedSQL
     * @param field
     * @return
     */
    public static Long DoReturnLong(String passedSQL, String field) {

        Long RetunInt = 0L;

        try {
            HikariDataSource ds = SafHikariConnection.getDataSource();
            Connection conn = ds.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(passedSQL.trim());
            while (rs.next()) {
                RetunInt = rs.getLong(field);
            }
            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException ex) {
            Log.e("An error occurred while trying to execute a select query", ex);
        }

        return RetunInt;
    }

    /**
     *This boiler plate returns a double from a data source. It defaults to 0 if a record is not found or fails for any reason
     * @param passedSQL
     * @param field
     * @return
     */
    public static Double DoReturnDouble(String passedSQL, String field) {

        double Retundouble = 0;

        try {

            HikariDataSource ds = SafHikariConnection.getDataSource();
            Connection conn = ds.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(passedSQL.trim());
            while (rs.next()) {
                Retundouble = rs.getDouble(field);
            }
            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException ex) {
            Log.e("An error occurred while trying to execute a select query", ex);
        }

        return Retundouble;
    }

    /**
     * This boiler plate returns a  single string from a data source. It defaults to empty string if a record is not found or fails for any reason
     * @param passedSQL
     * @param field
     * @return a string
     */

    public static String DoReturnString(String passedSQL, String field) {

        String RetunString = "";

        try {

            HikariDataSource ds = SafHikariConnection.getDataSource();
            Connection conn = ds.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(passedSQL.trim());
            while (rs.next()) {
                RetunString = rs.getString(field);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            Log.e("An error occurred while trying to execute a select query", ex);
        }
        return RetunString;
    }


    /**
     * This boiler plate does an insert to a data store
     * @param passedSQL
     * @return the generated table UUID
     */
    public static int DoInserts(String passedSQL) {

        int theUID = 0;

        try {

            HikariDataSource ds = SafHikariConnection.getDataSource();
            Connection conn = ds.getConnection();
            PreparedStatement ps = conn.prepareStatement(passedSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                theUID = rs.getInt(1);
            }
            rs.close();
            ps.close();
            conn.close();

        }catch (SQLException ex) {
            Log.e("An error occurred while trying to execute an insert query", ex);
        }
        return theUID;
    }

    /**
     * Does an update to a data store
     * @param passedSQL
     * @return boolean
     */
    public static boolean DoUpdates(String passedSQL) {

        try {

            HikariDataSource ds = SafHikariConnection.getDataSource();
            Connection conn = ds.getConnection();
            PreparedStatement ps = conn.prepareStatement(passedSQL);
            ps.executeUpdate();
            ps.close();
            conn.close();

        } catch (SQLException ex) {
            Log.e("An error occurred while trying to execute an update query", ex);
            return false;
        }
        return true;

    }

    /**
     * Does an update to a data store and returns the status if the row was actually affected
     * @param passedSQL the update sql statement
     * @return boolean
     */
    public static boolean DoUpdatesWithRowCount(String passedSQL) {

        boolean result=false;
        try {

            HikariDataSource ds = SafHikariConnection.getDataSource();
            Connection conn = ds.getConnection();
            PreparedStatement ps = conn.prepareStatement(passedSQL);
            int count =ps.executeUpdate();
            ps.close();
            conn.close();
            if(count>0)result=true;

        } catch (SQLException ex) {
            Log.e("An error occurred while trying to execute an update query", ex);
            return false;
        }
        return result;

    }

    /**
     * This boiler plate returns a float from a data source. It defaults to 0L if a record is not found or fails for any reason
     * @param passedSQL
     * @param field
     * @return
     */
    public static float DoReturnFloat(String passedSQL, String field) {

        float Retundouble = 0L;

        try {

            HikariDataSource ds = SafHikariConnection.getDataSource();
            Connection conn = ds.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(passedSQL.trim());
            while (rs.next()) {
                Retundouble = rs.getFloat(field);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            Log.e("An error occurred while trying to execute a select query", ex);
        }
        return Retundouble;
    }

    /**
     * This boiler plate returns a double from a data source. It defaults to 0 if a record is not found or fails for any reason
     * @param passedSQL
     * @param field
     * @return
     */
    public static Double DoReturnDoubleMpesa(String mysqlDriver, String connectionUrl, String user, String password, String passedSQL, String field) {

        double Retundouble = 0;
        try {

            HikariDataSource ds = SafHikariConnection.getDataSource();
            Connection conn = ds.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(passedSQL.trim());
            while (rs.next()) {
                Retundouble = rs.getDouble(field);
            }
            rs.close();
            stmt.close();
            conn.close();
        }  catch (SQLException ex) {
            Log.e("An error occurred while trying to execute a select query", ex);
        }
        return Retundouble;
    }

}

