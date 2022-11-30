package com.therift.fortgame.Util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class UtilManager {

    public ItemStack createItemStack(Material material, String DisplayName, List<String> lore, String localizedName, int CustomModelData){

        //-----------------------------------
        //          Create ItemStack
        //-----------------------------------
        ItemStack itemStack = new ItemStack(material);

        //-----------------------------------
        //      Getting ItemMeta
        //-----------------------------------
        ItemMeta itemMeta = itemStack.getItemMeta();

        //-----------------------------------
        //          Setting Metas
        //-----------------------------------
        itemMeta.setDisplayName(DisplayName);
        itemMeta.setLore(lore);

        if (CustomModelData != 0){
            itemMeta.setCustomModelData(CustomModelData);
        }

        if (localizedName != null){
            itemMeta.setLocalizedName(localizedName);
        }


        itemStack.setItemMeta(itemMeta);

        //-----------------------------------
        //      Return ItemStack
        //-----------------------------------
        return itemStack;
    }
}
