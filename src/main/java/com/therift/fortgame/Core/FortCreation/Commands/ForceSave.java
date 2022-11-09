package com.therift.fortgame.Core.FortCreation.Commands;

import com.therift.fortgame.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ForceSave implements CommandExecutor {
    private Main main;
    public ForceSave(Main main){
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1){
            if (Bukkit.getPlayer(strings[0]) != null){
                Player player = Bukkit.getPlayer(strings[0]);
                main.getFortCreationListener().getFortCreation().saveFort(player.getUniqueId());

                player.sendMessage(ChatColor.GREEN + "Players fort saved");
            }else {
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
