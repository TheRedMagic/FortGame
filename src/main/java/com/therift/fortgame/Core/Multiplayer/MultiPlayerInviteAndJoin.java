package com.therift.fortgame.Core.Multiplayer;

import com.therift.fortgame.ConfigData.Database.PlayerManager;
import com.therift.fortgame.Main;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class MultiPlayerInviteAndJoin {
    private Main main;
    private HashMap<UUID, UUID> invitedMap = new HashMap<>();

    public MultiPlayerInviteAndJoin(Main main){

        this.main = main;

    }

    public void invite(@NotNull UUID inviting, @NotNull UUID invited){
        //-----------------------------------
        //          Player Vars
        //-----------------------------------
        OfflinePlayer invitingPlayer = Bukkit.getOfflinePlayer(inviting);
        OfflinePlayer invitedPlayer = Bukkit.getOfflinePlayer(invited);

        if (!invitedPlayer.isOnline()) {
            return;
        }

        //-----------------------------------
        // Checks if player has multiplayer fort
        //-----------------------------------
        PlayerManager InvitingPm = new PlayerManager(main, invitedPlayer.getUniqueId());
        PlayerManager InvitedPm = new PlayerManager(main, invitedPlayer.getUniqueId());

        if (!InvitingPm.getMultiplayerStructerName().equals("0")){
            invitingPlayer.getPlayer().sendMessage(ChatColor.RED + invitedPlayer.getName() + " already have a multiplayer fort");
            return;
        }

        //-----------------------------------
        //      Adds uuids to map
        //-----------------------------------
        invitedMap.put(invitedPlayer.getUniqueId(), invitingPlayer.getUniqueId());

        //-----------------------------------
        //  Removes from map after 5 min
        //-----------------------------------
        Bukkit.getScheduler().runTaskLater(main, () -> {
            if (invitedMap.containsKey(invited)){
                invitedMap.remove(invited);
            }
        }, 6000);

        //-----------------------------------
        //        TextComponents
        //-----------------------------------

        TextComponent start = new TextComponent("§7▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n" +
                "§f" + invitingPlayer.getName() + " has invited you to ");

        TextComponent join = new TextComponent("§6§ljoin");
        join.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "join " + invitingPlayer.getName()));
        join.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to join " + invitedPlayer.getName() + " fort")));

        TextComponent end = new TextComponent("§r join there fort\n" +
                "§7▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

        start.addExtra(join);
        start.addExtra(end);

        //-----------------------------------
        //      Sending messages
        //-----------------------------------

        invitingPlayer.getPlayer().spigot().sendMessage(start);

        invitedPlayer.getPlayer().sendMessage(ChatColor.GRAY + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n" +
                ChatColor.GREEN + invitedPlayer.getName() + " has been invited to join your fort\n" +
                ChatColor.GRAY + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

    }

    public void joinGroup(UUID inviting, UUID invited){
        //-----------------------------------
        //          Player Vars
        //-----------------------------------
        OfflinePlayer invitingPlayer = Bukkit.getOfflinePlayer(inviting);
        OfflinePlayer invitedPlayer = Bukkit.getOfflinePlayer(invited);

        //-----------------------------------
        //  Checks if player was invited
        //-----------------------------------
        if (invitedMap.containsKey(invited)){
            //-----------------------------------
            //Checks if the inviting player is the same
            //-----------------------------------
            if (invitedMap.get(invitedPlayer.getUniqueId()) == invitingPlayer.getUniqueId()){
                //-----------------------------------
                //      PlayerManager Vars
                //-----------------------------------

                PlayerManager InvitingPm = new PlayerManager(main, invitedPlayer.getUniqueId());
                PlayerManager InvitedPm = new PlayerManager(main, invitedPlayer.getUniqueId());

                //-----------------------------------
                // Checks if inviting has a multiplayer fort
                //-----------------------------------
                if (InvitingPm.getMultiplayerStructerName().equals("0")){
                    InvitingPm.setMultiplayerStructerName(invitingPlayer.getUniqueId() + "_MultiPlayer_Fort.schem");
                }

                InvitedPm.setMultiplayerStructerName(InvitingPm.getMultiplayerStructerName());


                //-----------------------------------
                //           Sends Messages
                //-----------------------------------
                invitedPlayer.getPlayer().sendMessage(ChatColor.GRAY + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n" +
                        ChatColor.GREEN + invitedPlayer.getName() + " has join the fort\n" +
                        ChatColor.GRAY + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

                invitingPlayer.getPlayer().sendMessage(ChatColor.GRAY + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n" +
                        ChatColor.GREEN + "You have joined the fort'n" +
                        ChatColor.GRAY + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

            }else {
                invitedPlayer.getPlayer().sendMessage(ChatColor.RED + "That player didn't send you a invite");
            }
        }else {
            invitedPlayer.getPlayer().sendMessage(ChatColor.RED + "There are none invites to accept");
        }
    }
}
