package com.foodapp;

import com.foodapp.akka.ActorSystemSingletone;
import com.foodapp.commons.Log;
import com.foodapp.commons.SafHikariConnection;
import com.zaxxer.hikari.HikariConfig;

/**
 *
 */
public class Main {
    public static int HIKARI_MAX_POOL_SIZE=400;
    private static int HIKARI_MIN_POOL_SIZE = 0;
    public static void main(String[] args) {startApp();
    }

    /** This method starts the app */
    public static void startApp() {

        Log.d("Starting app....");
        String parcelConUrl ="jdbc:mysql://localhost:3306/foodapp?useConfigs=maxPerformance";
        String mysqlUser = "root";
        String mysqlPassword = "tumaxpr355";

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(parcelConUrl);
        hikariConfig.setUsername(mysqlUser);
        hikariConfig.setPassword(mysqlPassword);
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "400");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.setAutoCommit(true);
        hikariConfig.setPoolName("foodapp_server");
        hikariConfig.setRegisterMbeans(true);
        hikariConfig.setMinimumIdle(HIKARI_MIN_POOL_SIZE); //for maximum performance and responsiveness to spike demands, let HikariCP  act as a fixed size connection pool
        hikariConfig.setMaximumPoolSize(HIKARI_MAX_POOL_SIZE);

        SafHikariConnection.initialize(hikariConfig);

        //initialize the actor system
        ActorSystemSingletone.init();

    }
}
