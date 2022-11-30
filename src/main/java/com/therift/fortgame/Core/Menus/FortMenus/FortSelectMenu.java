package com.therift.fortgame.Core.Menus.FortMenus;

import com.therift.fortgame.ConfigData.Database.PlayerManager;
import com.therift.fortgame.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class FortSelectMenu {

    private Main main;

    public FortSelectMenu(Main main){this.main = main;}

    public void openMenu(UUID uuid){

        //-----------------------------------
        //          Default Vars
        //-----------------------------------
        Player player = Bukkit.getPlayer(uuid);
        PlayerManager playerManager = new PlayerManager(main, uuid);


        //-----------------------------------
        //      ItemStack Creator
        //-----------------------------------
        ItemStack quit = main.getUtilManager().createItemStack(Material.BARRIER,
                ChatColor.RED + "Leave Menu",
                Arrays.asList(ChatColor.GRAY + "Right click to leave the menu"),
                "leaveFortMenu",
                0);

        ItemStack soloFort = main.getUtilManager().createItemStack(Material.BRICK_WALL,
                ChatColor.AQUA + "Solo Fort",
                Arrays.asList(ChatColor.GRAY + "Left click to load this fort\nRight click to open settings"),
                "soloFort", 0);

        //-----------------------------------
        //    Create Multiplayer item
        //-----------------------------------
        ItemStack multiplayerFort = new ItemStack(Material.STONE_BRICKS);

        ItemMeta multiplayerMeta = multiplayerFort.getItemMeta();

        if (!playerManager.getMultiplayerStructerName().equals("0")){
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerManager.getMultiplayerStructerName().split("_")[0]);
            multiplayerMeta.setDisplayName(ChatColor.AQUA + offlinePlayer.getName() + " fort");
        }else {
            multiplayerMeta.setDisplayName(ChatColor.RED + "Don't have a multiplayer fort");
            multiplayerFort.setType(Material.BARRIER);
        }

        multiplayerMeta.setLocalizedName("multiplayerFort");
        multiplayerFort.setItemMeta(multiplayerMeta);

        //-----------------------------------
        //      Sets item in inv
        //-----------------------------------

        player.getInventory().setItem(0, quit);
        player.getInventory().setItem(2, soloFort);
        player.getInventory().setItem(5, multiplayerFort);


    }

    public void closeMenu(UUID uuid){
        //-----------------------------------
        //          Default Vars
        //-----------------------------------
        Player player = Bukkit.getPlayer(uuid);

        ItemStack quit = player.getInventory().getItem(0);
        ItemStack soloFort = player.getInventory().getItem(2);
        ItemStack multiplayerFort = player.getInventory().getItem(5);

        //-----------------------------------
        //  Checking and clearing items
        //-----------------------------------
        if (quit.getItemMeta().getLocalizedName().equals("leaveFortMenu")){player.getInventory().remove(quit);}
        if (soloFort.getItemMeta().getLocalizedName().equals("soloFort")){player.getInventory().remove(soloFort);}
        if (multiplayerFort.getItemMeta().getLocalizedName().equals("multiplayerFort")){player.getInventory().remove(multiplayerFort);}
    }

    public void interactEvent(PlayerInteractEvent e){
        //-----------------------------------
        //          Getting Vars
        //-----------------------------------
        ItemStack item = e.getItem();
        Player player = e.getPlayer();
        Action action = e.getAction();

        //-----------------------------------
        //   Checks if item has local Name
        //-----------------------------------
        if (item.hasItemMeta()){
            if (item.getItemMeta().getLocalizedName() == null){return;}
        }else {
            return;
        }


        //-----------------------------------
        //          Left Click
        //-----------------------------------
        if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)){
            //-----------------------------------
            //    Checks what item it is
            //-----------------------------------
            if (item.getItemMeta().getLocalizedName().equals("leaveFortMenu")){
                //-----------------------------------
                //          Closes Menu
                //-----------------------------------
                closeMenu(player.getUniqueId());
            }
            if (item.getItemMeta().getLocalizedName().equals("soloFort")){
                //-----------------------------------
                //      SoloFort Settings
                //-----------------------------------
            }
            if (item.getItemMeta().getLocalizedName().equals("multiplayerFort")){
                //-----------------------------------
                //      Multiplayer Settings
                //-----------------------------------
            }
        }

        //-----------------------------------
        //          Right Click
        //-----------------------------------
        if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)){
            if (item.getItemMeta().getLocalizedName().equals("leaveFortMenu")){
                //-----------------------------------
                //          Close Menu
                //-----------------------------------
                closeMenu(player.getUniqueId());
            }
            if (item.getItemMeta().getLocalizedName().equals("soloFort")){
                //-----------------------------------
                //      Loads Solo Fort
                //-----------------------------------

            }
            if (item.getItemMeta().getLocalizedName().equals("multiplayerFort")){
                //-----------------------------------
                //    Loads Multiplayer Fort
                //-----------------------------------
            }
        }
    }
}
