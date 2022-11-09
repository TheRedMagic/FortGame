package com.therift.fortgame.Core.FortCreation;

import com.therift.fortgame.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class FortCreationListener implements Listener {
    private Main main;

    public FortCreationListener(Main main){
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        //-----------------------------------
        //         Loads Fort
        //-----------------------------------
        new FortCreation().onJoin(e, main);
    }
}
