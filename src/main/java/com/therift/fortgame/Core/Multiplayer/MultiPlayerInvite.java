package com.therift.fortgame.Core.Multiplayer;

import com.therift.fortgame.ConfigData.Database.PlayerManager;
import com.therift.fortgame.Main;
import com.therift.theriftcore.Database.DatabaseManager.RiftPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

import java.util.UUID;

public class MultiPlayerInvite {
    private Main main;

    public MultiPlayerInvite(Main main){

        this.main = main;

    }

    public void invite(UUID inviting, UUID invited){
        //-----------------------------------
        //          Player Vars
        //-----------------------------------
        RiftPlayer invitingPlayer = new RiftPlayer(inviting);
        RiftPlayer invitedPlayer = new RiftPlayer(invited);

        if (!invitedPlayer.isOnline()) {
            return;
        }

        //-----------------------------------
        // Checks if player has multiplayer fort
        //-----------------------------------
        PlayerManager InvitingPm = new PlayerManager(main, invitedPlayer.getUuid());
        PlayerManager InvitedPm = new PlayerManager(main, invitedPlayer.getUuid());

        if (!InvitingPm.getMultiplayerStructerName().equals("0")){
            invitingPlayer.sendMessage(ChatColor.RED + invitedPlayer.getName() + " already have a multiplayer fort");
            return;
        }

        //-----------------------------------
        //        TextComponents
        //-----------------------------------

        TextComponent start = new TextComponent("§7▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n" +
                "§f" + invitingPlayer.getName() + " has invited you to ");

        TextComponent join = new TextComponent("§6§ljoin");
        join.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ""));
        join.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to join " + invitedPlayer.getName() + " fort")));

        TextComponent end = new TextComponent("§r join there fort\n" +
                "§7▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

        start.addExtra(join);
        start.addExtra(end);

        //-----------------------------------
        //      Sending messages
        //-----------------------------------

        invitingPlayer.getOnlinePlayer().spigot().sendMessage(start);

        invitedPlayer.sendMessage(ChatColor.GRAY + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n" +
                ChatColor.GREEN + invitedPlayer.getName() + " has been invited to join your fort\n" +
                ChatColor.GRAY + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

    }
}
