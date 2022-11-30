package com.therift.fortgame.Core.Multiplayer.Commands;

import com.therift.fortgame.Core.Multiplayer.MultiPlayerInviteAndJoin;
import com.therift.fortgame.Main;

import net.minecraft.data.worldgen.placement.PlacementUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class InviteCommand implements CommandExecutor {
    private Main main;

    public InviteCommand(Main main){
        this.main = main;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        //-----------------------------------
        // Checks is commandSender is player
        //-----------------------------------
        if (commandSender instanceof Player){

            Player player = (Player) commandSender;

            //-----------------------------------
            //  Checks if there is a player
            //-----------------------------------

           if (strings.length == 1){
               if (Bukkit.getPlayer(strings[0]) != null){
                   Player player1 = Bukkit.getPlayer(strings[0]);

                   //-----------------------------------
                   //           Invite method
                   //-----------------------------------
                   main.getMultiPlayerInvite().invite(player.getUniqueId(), player1.getUniqueId());

               }else {
                   player.sendMessage(ChatColor.RED + "Can't find player");
               }

           }else {
               player.sendMessage(ChatColor.RED + "Invalid usage | /invite (Username)");
           }
        }
        return false;
    }
}
