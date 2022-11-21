package com.therift.fortgame.Core.FortCreation.Commands;

import com.therift.fortgame.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ForceSoloLoad implements CommandExecutor {
    private Main main;
    public ForceSoloLoad(Main main) {this.main = main;}

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        //-----------------------------------
        // Checks if command has agrs
        //-----------------------------------
        if (strings.length == 1){

            //-----------------------------------
            // Checks if player in not null
            //-----------------------------------
            if (Bukkit.getPlayer(strings[0]) != null){

                //-----------------------------------
                //          Runs method
                //-----------------------------------
                main.getFortCreationListener().getFortCreation().LoadSoloFort(Bukkit.getPlayer(strings[0]).getUniqueId(), false);

                //-----------------------------------
                //          Sends Message
                //-----------------------------------
                if (commandSender instanceof Player) {
                    Player player = (Player) commandSender;
                    player.sendMessage(ChatColor.GREEN + "Fort Loaded");
                }
            }else {

                //-----------------------------------
                //      Can't find player
                //-----------------------------------
                if (commandSender instanceof Player){
                    Player player = (Player) commandSender;
                    player.sendMessage(ChatColor.RED + "Can't find player");
                }else {
                    Bukkit.getLogger().info("Can't find player");
                }
            }
        }
        return false;
    }
}
