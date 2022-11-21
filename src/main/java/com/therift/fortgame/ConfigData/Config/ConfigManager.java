package com.therift.fortgame.ConfigData.Config;

import com.therift.fortgame.Main;

public class ConfigManager {
    private Main main;

    //Database Var
    private String Host;
    private String Username;
    private String Password;
    private String Port;

    //Schematics
    private String defaultPath;
    private String soloFortPath;
    private String soloConfigPath;

    //Fort Settings
    private int BlocksBetweenForts;

    private Double FortSizeX;
    private Double FortSizeY;
    private Double FortSizeZ;


    public ConfigManager(Main main) {
        this.main = main;

        //-----------------------------------
        //      Saving/Creating Config
        //-----------------------------------
        main.getConfig().options().copyDefaults();
        main.saveConfig();


        //-----------------------------------
        //      Setting Database Var
        //-----------------------------------
        this.Host = main.getConfig().getString("Host");
        this.Username = main.getConfig().getString("Username");
        this.Password = main.getConfig().getString("Password");
        this.Port = main.getConfig().getString("Port");



        //-----------------------------------
        //             Schematics
        //-----------------------------------
        this.defaultPath = main.getConfig().getString("DefaultFortPath");
        this.soloFortPath = main.getConfig().getString("SoloFortPath");

        //-----------------------------------
        //           Configs
        //-----------------------------------
        this.soloConfigPath = main.getConfig().getString("SoloFortConfigPath");


        //-----------------------------------
        //          Fort Settings
        //-----------------------------------
        this.BlocksBetweenForts = main.getConfig().getInt("BlocksBetweenForts");

        this.FortSizeX = main.getConfig().getDouble("FortSizeX");
        this.FortSizeY = main.getConfig().getDouble("FortSizeY");
        this.FortSizeZ = main.getConfig().getDouble("FortSizeZ");

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

    public String getDefaultFortPath() {
        return defaultPath;
    }

    public int getBlocksBetweenForts() {
        return BlocksBetweenForts;
    }

    public Double getFortSizeX() {
        return FortSizeX;
    }
    public Double getFortSizeY() {
        return FortSizeY;
    }
    public Double getFortSizeZ() {
        return FortSizeZ;
    }

    public String getSoloFortPath() {
        return soloFortPath;
    }

    public String getSoloConfigPath() {
        return soloConfigPath;
    }
}
