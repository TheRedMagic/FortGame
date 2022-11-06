package com.therift.fortgame.ConfigData.Database;

import com.therift.fortgame.Main;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.nio.channels.ConnectionPendingException;


public class Database {

    private Main main;
    private Connection connection;


    public Database(Main main){
        this.main = main;


        //Database connection
        Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> {
            try {
                connection = DriverManager.getConnection("jdbc:mysql://" + main.getConfigManager().getHost() + ":" + main.getConfigManager().getPort() + "/" + main.getConfigManager().getUsername() + "?autoReconnect=true",
                        main.getConfigManager().getUsername(),
                        main.getConfigManager().getPassword());

                connection.prepareStatement("SET SESSION idle_transaction_timeout=0;");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 0, 432000);

    }

    public boolean isConnected(){return connection != null;}
    public Connection getConnection(){return connection;}

    public void disconnect(){
        //Database Disconnection
        if (isConnected()){
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
