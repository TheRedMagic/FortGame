package com.therift.fortgame;

import com.therift.fortgame.ConfigData.Config.ConfigManager;
import com.therift.fortgame.ConfigData.Database.Database;
import com.therift.theriftcore.CorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.structure.Structure;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class Main extends JavaPlugin {

    private CorePlugin api;

    //Data
    private ConfigManager config;
    private Database database;

    @Override
    public void onEnable() {

        //CorePlugin API
        api = (CorePlugin) Bukkit.getPluginManager().getPlugin("CorePlugin");

        //Data
        config = new ConfigManager(this);
        database = new Database(this);

    }

    @Override
    public void onDisable() {



        //Database Disconnection
        if (database.isConnected()){
            database.disconnect();
        }
    }

    public ConfigManager getConfigManager(){return config;}
    public Database getDatabase(){return database;}
}
