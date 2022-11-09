package com.therift.fortgame.Core.FortCreation;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.*;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import com.therift.fortgame.ConfigData.Database.PlayerManager;
import com.therift.fortgame.Main;
import com.therift.theriftcore.Database.DatabaseManager.RiftPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;

public class FortCreation {

    private Main main;
    private HashMap<UUID, Location> FortLocations = new HashMap<>();

    public void onJoin(PlayerJoinEvent e, Main main){
        Bukkit.getScheduler().runTaskLater(main, () -> {
            this.main = main;
            PlayerManager playerManager = new PlayerManager(main, e.getPlayer().getUniqueId());

            //-----------------------------------
            //Checks if player has a saved fort
            //-----------------------------------

            if (playerManager.getSoloStructerName().equals("0")){
                //-----------------------------------
                //Player doesn't have a fort | Creating a new fort
                //-----------------------------------

                CreateSoloFort(e.getPlayer().getUniqueId(), true);
            }else {
                //-----------------------------------
                //Player Has a save
                //-----------------------------------

                CreateSoloFort(e.getPlayer().getUniqueId(), false);
            }
        }, 2);
    }

    public void onLeave(PlayerQuitEvent e){
        //-----------------------------------
        //  Checks if player has fort loaded
        //-----------------------------------
        if (FortLocations.containsKey(e.getPlayer().getUniqueId())){
            saveFort(e.getPlayer().getUniqueId());
        }
    }

    public void CreateSoloFort(UUID uuid, boolean newFort){
        Player player = Bukkit.getPlayer(uuid);

        World world = BukkitAdapter.adapt(player.getWorld());

        Location spawnLocation = new Location(player.getWorld(), 0, 0, 10000);
        Boolean foundSpot = false;
        int amount = 0;


        //-----------------------------------
        //      Finding Fort location
        //-----------------------------------

        while (!foundSpot){
            Location location = new Location(player.getWorld(), 0, 100, main.getConfigManager().getBlocksBetweenForts()*amount);
            if (location.subtract(0, 2, 0).getBlock().getType() == Material.AIR){
                spawnLocation = location;
                foundSpot = true;
            }
            amount++;
        }


        //-----------------------------------
        //  Check if it is a new fort or not
        //-----------------------------------
        File fortFile;
        if (newFort) {
            File defaultSchematic = new File(main.getDataFolder() + main.getConfigManager().getDefaultFortPath());
            fortFile = defaultSchematic;
            spawnLocation.add(0, 1, 0);
        }else {
            File saveSchematic = new File(main.getDataFolder() + main.getConfigManager().getSoloFortPath() + uuid + "_fort.schem");
            fortFile = saveSchematic;

            if (!fortFile.exists()){
                player.kickPlayer(ChatColor.RED + "Can't find your fort\nPlease try again");
            }
        }



        player.teleport(spawnLocation);

        //-----------------------------------
        //Loading Schematic
        //-----------------------------------

        ClipboardFormat format = ClipboardFormats.findByFile(fortFile);
        try (ClipboardReader reader = format.getReader(new FileInputStream(fortFile))){
            Clipboard clipboard = reader.read();

            //-----------------------------------
            //Pasting Schematic
            //-----------------------------------

            try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1)) {
                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .to(BlockVector3.at(spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ()))
                        .ignoreAirBlocks(true)
                        .build();
                Operations.complete(operation);
            } catch (WorldEditException e) {
                throw new RuntimeException(e);
            }


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        FortLocations.put(uuid, spawnLocation);


    }

    public void saveFort(UUID uuid){

        //-----------------------------------
        //          Default Var
        //-----------------------------------

        Player player = Bukkit.getPlayer(uuid);

        org.bukkit.World world = player.getWorld();
        Location FortLocation = FortLocations.get(uuid);


        World world1 = BukkitAdapter.adapt(world);



        //-----------------------------------
        //      Getting second Location
        //-----------------------------------
        Double SecondLocX = FortLocation.getX()+main.getConfigManager().getFortSizeX();
        Double SecondLocY = FortLocation.getY()+main.getConfigManager().getFortSizeY();
        Double SecondLocZ = FortLocation.getZ()+main.getConfigManager().getFortSizeZ();


        //-----------------------------------
        //          Getting region
        //-----------------------------------
        CuboidRegion region = new CuboidRegion(world1 ,BlockVector3.at(FortLocation.getX(), FortLocation.getY()-1.0, FortLocation.getZ()), BlockVector3.at(SecondLocX, SecondLocY, SecondLocZ));

        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);

        //-----------------------------------
        //    Saving schematic to clipboard
        //-----------------------------------

        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(region.getWorld(), -1);

        ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(editSession, region, clipboard, region.getMinimumPoint());
        forwardExtentCopy.setCopyingEntities(true);

        try {
            Operations.complete(forwardExtentCopy);
        } catch (WorldEditException e) {
            throw new RuntimeException(e);
        }


        //-----------------------------------
        //          Saving Schematic
        //-----------------------------------
        File file = new File(main.getDataFolder() + main.getConfigManager().getSoloFortPath() + uuid + "_fort.schem");

        if (file.exists()){file.delete();}

        try (ClipboardWriter writer = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(new FileOutputStream(file))) {
            writer.write(clipboard);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FortLocations.remove(uuid);
        PlayerManager pl  = new PlayerManager(main, uuid);
        pl.setSoloStructureName(uuid + "_fort.schem");

    }

}
