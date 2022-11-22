package com.therift.fortgame;

import com.sk89q.worldedit.WorldEdit;
import com.therift.fortgame.ConfigData.Config.ConfigManager;
import com.therift.fortgame.ConfigData.Database.Database;
import com.therift.fortgame.Core.FortCreation.Commands.ForceSoloDelete;
import com.therift.fortgame.Core.FortCreation.Commands.ForceSoloLoad;
import com.therift.fortgame.Core.FortCreation.Commands.ForceSoloSave;
import com.therift.fortgame.Core.FortCreation.FortCreationListener;
import com.therift.fortgame.Core.Multiplayer.Commands.InviteCommand;
import com.therift.fortgame.Core.Multiplayer.Commands.JoinCommand;
import com.therift.fortgame.Core.Multiplayer.MultiPlayerInviteAndJoin;
import com.therift.theriftcore.TheRiftCore;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    //-----------------------------------
    //            Classes
    //-----------------------------------
    private TheRiftCore riftCore;
    private FortCreationListener fortCreationListener;
    private WorldEdit worldEdit;
    private MultiPlayerInviteAndJoin multiPlayerInvite;

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

        riftCore = (TheRiftCore) Bukkit.getPluginManager().getPlugin("TheRiftCore");
        worldEdit = WorldEdit.getInstance();

        //-----------------------------------
        //              Classes
        //-----------------------------------

        multiPlayerInvite = new MultiPlayerInviteAndJoin(this);

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

    public ConfigManager getConfigManager(){return config;}
    public Database getDatabase(){return database;}
    public FortCreationListener getFortCreationListener() {
        return fortCreationListener;
    }

    public MultiPlayerInviteAndJoin getMultiPlayerInvite() {
        return multiPlayerInvite;
    }
}
