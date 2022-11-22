package com.therift.fortgame.Core.Multiplayer.Commands;

import com.therift.fortgame.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.eclipse.aether.util.listener.ChainedTransferListener;
import org.jetbrains.annotations.NotNull;

import java.io.Console;

public class JoinCommand implements CommandExecutor {

    private Main main;
    public JoinCommand(Main main){
        this.main = main;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player){
            Player player = (Player) commandSender;

            if (strings.length == 1){

                if (Bukkit.getPlayer(strings[0]) != null){
                    Player player1 = Bukkit.getPlayer(strings[0]);

                    main.getMultiPlayerInvite().joinGroup(player1.getUniqueId(), player.getUniqueId());
                }else {
                    player.sendMessage(ChatColor.RED + "Can't find player");
                }

            }else {
                player.sendMessage(ChatColor.RED + "Invalid usage | /join (Username)");
            }

        }else {
            Bukkit.getLogger().info("Needs to be send by a player");
        }
        return false;
    }
}
