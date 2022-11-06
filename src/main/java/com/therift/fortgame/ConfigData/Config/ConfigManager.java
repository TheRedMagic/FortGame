package com.therift.fortgame.ConfigData.Config;

import com.therift.fortgame.Main;

public class ConfigManager {
    private Main main;

    //Database Var
    private String Host;
    private String Username;
    private String Password;
    private String Port;


    public ConfigManager(Main main) {
        this.main = main;

        //Saving/Creating Config
        main.getConfig().options().copyDefaults();
        main.saveConfig();

        //Setting Database Var
        this.Host = main.getConfig().getString("Host");
        this.Username = main.getConfig().getString("Username");
        this.Password = main.getConfig().getString("Password");
        this.Port = main.getConfig().getString("Port");

    }

    public String getHost() {
        return Host;
    }

    public String getPassword() {
        return Password;
    }

    public String getPort() {
        return Port;
    }

    public String getUsername() {
        return Username;
    }
}
