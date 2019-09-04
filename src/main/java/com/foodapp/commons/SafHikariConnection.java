package com.foodapp.commons;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Created by khisahamphrey
 */
public class SafHikariConnection {

    private static volatile SafHikariConnection hikariCP = null;
    private static HikariDataSource hikariDataSource = null;
    private static HikariDataSource reportsDbHikariDataSource = null;


    private SafHikariConnection(HikariConfig config) {
        hikariDataSource = new HikariDataSource(config);
    }

    public static void initialize(HikariConfig hikariConfig) {
        if (hikariCP == null) {
            synchronized (SafHikariConnection.class)
            {
                if (hikariCP == null)
                {
                    hikariCP = new SafHikariConnection (hikariConfig);
                }
            }
        }
    }

    public static SafHikariConnection getInstance() {
        return hikariCP;
    }

    public static HikariDataSource getDataSource() {
        hikariCP = getInstance();
        return hikariDataSource;
    }

    public static void connectReportsDb(HikariConfig config) {
        hikariCP = getInstance();
        reportsDbHikariDataSource = new HikariDataSource(config);
    }

    public static HikariDataSource getReportsDbDataSource() {
        hikariCP = getInstance();
        return reportsDbHikariDataSource;
    }

}
