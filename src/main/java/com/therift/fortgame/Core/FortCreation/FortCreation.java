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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;

public class FortCreation {

    private Main main;
    private HashMap<UUID, Location> FortLocations = new HashMap<>();

    public void onJoin(PlayerJoinEvent e, Main main){
        System.out.println("Join event ran");
        Bukkit.getScheduler().runTaskLater(main, () -> {
            this.main = main;
            PlayerManager playerManager = new PlayerManager(main, e.getPlayer().getUniqueId());

            //-----------------------------------
            //Checks if player has a saved fort
            //-----------------------------------

            if (playerManager.getSoloStructerName().equals("0")){
                System.out.println("Player doesn't have save");

                //-----------------------------------
                //Player doesn't have a fort | Creating a new fort
                //-----------------------------------

                CreateSoloFort(e.getPlayer().getUniqueId(), true);
            }
        }, 2);
    }

    private void CreateSoloFort(UUID uuid, boolean newFort){
        RiftPlayer player = new RiftPlayer(uuid);

        World world = BukkitAdapter.adapt(player.getWorld());

        Location spawnLocation = new Location(player.getWorld(), 0, 0, 10000);
        Boolean foundSpot = false;
        int amount = 0;


        //-----------------------------------
        //Finding Fort location
        //-----------------------------------

        while (!foundSpot){
            Location location = new Location(player.getWorld(), 0, 100, main.getConfigManager().getBlocksBetweenForts()*amount);
            if (location.getBlock().getType() != Material.AIR){
                spawnLocation = location;
                foundSpot = true;
            }
            amount++;
        }


        File defaultSchematic = new File(main.getDataFolder() + main.getConfigManager().getDefaultFortPath());

        //-----------------------------------
        //Loading Schematic
        //-----------------------------------

        ClipboardFormat format = ClipboardFormats.findByFile(defaultSchematic);
        try (ClipboardReader reader = format.getReader(new FileInputStream(defaultSchematic))){
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
                e.printStackTrace();
            }


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        FortLocations.put(player.getUuid(), spawnLocation);
        player.teleport(spawnLocation);

    }

    private void saveFort(Location FortLocation, org.bukkit.World world, UUID uuid){

        World world1 = main.getWorldEdit().newEditSessionBuilder().getWorld();

        Double SecondLocX = FortLocation.getX()+main.getConfigManager().getFortSizeX();
        Double SecondLocY = FortLocation.getY()+main.getConfigManager().getFortSizeY();
        Double SecondLocZ = FortLocation.getZ()+main.getConfigManager().getFortSizeZ();

        CuboidRegion region = new CuboidRegion(BlockVector3.at(
                FortLocation.getX(),
                FortLocation.getY(),
                FortLocation.getZ()),
                BlockVector3.at(SecondLocX, SecondLocY, SecondLocZ));

        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);

        try(EditSession editSession = WorldEdit.getInstance().newEditSession(world1)){
            ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(
                    editSession, region, clipboard, region.getMinimumPoint()
            );
            forwardExtentCopy.setCopyingEntities(true);
            try {
                Operations.complete(forwardExtentCopy);
            } catch (WorldEditException e) {
                throw new RuntimeException(e);
            }
        }

        File file = new File(main.getDataFolder() + File.separator + main.getConfigManager().getSoloFortPath() + uuid.toString() + "_fort.schematic");
        if (file.exists()){
            file.delete();
        }

        try (ClipboardWriter writer = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(new FileOutputStream(file))) {
            writer.write(clipboard);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FortLocations.remove(uuid);

    }

}
