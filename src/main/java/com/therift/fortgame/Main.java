package com.therift.fortgame;

import com.sk89q.worldedit.WorldEdit;
import com.therift.fortgame.ConfigData.Config.ConfigManager;
import com.therift.fortgame.ConfigData.Database.Database;
import com.therift.fortgame.Core.FortCreation.Commands.ForceSoloDelete;
import com.therift.fortgame.Core.FortCreation.Commands.ForceSoloLoad;
import com.therift.fortgame.Core.FortCreation.Commands.ForceSoloSave;
import com.therift.fortgame.Core.FortCreation.FortCreationListener;
import com.therift.fortgame.Core.Menus.MenuManager;
import com.therift.fortgame.Core.Multiplayer.Commands.InviteCommand;
import com.therift.fortgame.Core.Multiplayer.Commands.JoinCommand;
import com.therift.fortgame.Core.Multiplayer.MultiPlayerInviteAndJoin;
import com.therift.fortgame.Util.UtilManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    //-----------------------------------
    //            Classes
    //-----------------------------------
    private FortCreationListener fortCreationListener;
    private WorldEdit worldEdit;
    private MultiPlayerInviteAndJoin multiPlayerInvite;
    private MenuManager menuManager;
    private UtilManager utilManager;

    //-----------------------------------
    //          Data Var
    //-----------------------------------
    private ConfigManager config;
    private Database database;

    @Override
    public void onEnable() {

        //-----------------------------------
        //              API's
        //-----------------------------------

        worldEdit = WorldEdit.getInstance();

        //-----------------------------------
        //              Classes
        //-----------------------------------

        multiPlayerInvite = new MultiPlayerInviteAndJoin(this);
        menuManager = new MenuManager(this);
        utilManager = new UtilManager();

        //-----------------------------------
        //              Data
        //-----------------------------------

        config = new ConfigManager(this);
        database = new Database(this);

        //-----------------------------------
        //              Listener
        //-----------------------------------

        fortCreationListener = new FortCreationListener(this);


        //-----------------------------------
        //          Commands
        //-----------------------------------
        getCommand("ForceSoloSave").setExecutor(new ForceSoloSave(this));
        getCommand("ForceSoloLoad").setExecutor(new ForceSoloLoad(this));
        getCommand("ForceSoloDelete").setExecutor(new ForceSoloDelete(this));
        getCommand("Invite").setExecutor(new InviteCommand(this));
        getCommand("Join").setExecutor(new JoinCommand(this));
    }

    @Override
    public void onDisable() {

        //-----------------------------------
        //      Database Disconnection
        //-----------------------------------
        if (database.isConnected()){
            database.disconnect();
        }
    }

    //-----------------------------------
    //      Class Getters
    //-----------------------------------
    public ConfigManager getConfigManager(){return config;}
    public Database getDatabase(){return database;}
    public FortCreationListener getFortCreationListener() {
        return fortCreationListener;
    }
    public MultiPlayerInviteAndJoin getMultiPlayerInvite() {
        return multiPlayerInvite;
    }
    public MenuManager getMenuManager() {
        return menuManager;
    }
    public UtilManager getUtilManager() {
        return utilManager;
    }
}
