package com.therift.fortgame.Core.FortCreation;

import com.sk89q.worldedit.WorldEdit;
import com.therift.fortgame.Main;
import com.therift.theriftcore.Database.DatabaseManager.RiftPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class FortCreationListener implements Listener {
    private Main main;

    private FortCreation fortCreation;

    public FortCreationListener(Main main){
        this.main = main;
        Bukkit.getPluginManager().registerEvents(this, main);

        //-----------------------------------
        //      Getting Instance
        //-----------------------------------
        fortCreation = new FortCreation();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){

        RiftPlayer player = new RiftPlayer(e.getPlayer().getUniqueId());



        //-----------------------------------
        //         Loads Fort
        //-----------------------------------
        fortCreation.onJoin(e, main);


    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        //-----------------------------------
        //          Saves Fort
        //-----------------------------------
        fortCreation.onLeave(e);
    }

    public FortCreation getFortCreation() {
        return fortCreation;
    }
}
