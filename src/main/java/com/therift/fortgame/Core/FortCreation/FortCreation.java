package com.therift.fortgame.Core.FortCreation;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.*;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.DataException;
import com.sk89q.worldedit.world.World;
import com.therift.fortgame.ConfigData.Database.PlayerManager;
import com.therift.fortgame.Main;
import com.therift.theriftcore.Database.DatabaseManager.RiftPlayer;
import jdk.dynalink.Operation;
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

    public void onJoin(PlayerJoinEvent e){
        PlayerManager playerManager = new PlayerManager(main, e.getPlayer().getUniqueId());
        if (playerManager.getSoloStructerName().equals("0")){
            CreateSoloFort(e.getPlayer().getUniqueId(), playerManager, true);
        }
    }

    private void CreateSoloFort(UUID uuid, PlayerManager playerManager, boolean newFort){
        RiftPlayer player = new RiftPlayer(uuid);

        Location spawnLocation = new Location(player.getWorld(), 0, 0, 10000);
        Boolean foundSpot = false;
        int amount = 0;

        while (!foundSpot){
            Location location = new Location(player.getWorld(), 0, 100, main.getConfigManager().getBlocksBetweenForts()*amount);
            if (location.getBlock().getType() != Material.AIR){
                spawnLocation = location;
                foundSpot = true;
            }
            amount++;
        }


        File defaultSchematic = new File(main.getDataFolder() + File.separator + main.getConfigManager().getDefaultFortPath());

        Clipboard clipboard;

        ClipboardFormat format = ClipboardFormats.findByFile(defaultSchematic);
        try (ClipboardReader reader = format.getReader(new FileInputStream(defaultSchematic))){
            clipboard = reader.read();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (EditSession editSession = main.getWorldEdit().newEditSession((World) player.getWorld())) {
            Operation operation = (Operation) new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(BlockVector3.at(spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ()))
                    .build();
            Operations.complete((com.sk89q.worldedit.function.operation.Operation) operation);
        } catch (WorldEditException e) {
            throw new RuntimeException(e);
        }

        FortLocations.put(player.getUuid(), spawnLocation);

    }

    private void saveFort(Location FortLocation, org.bukkit.World world, UUID uuid){

        Double SecondLocX = FortLocation.getX()+main.getConfigManager().getFortSizeX();
        Double SecondLocY = FortLocation.getY()+main.getConfigManager().getFortSizeY();
        Double SecondLocZ = FortLocation.getZ()+main.getConfigManager().getFortSizeZ();

        CuboidRegion region = new CuboidRegion(BlockVector3.at(
                FortLocation.getX(),
                FortLocation.getY(),
                FortLocation.getZ()),
                BlockVector3.at(SecondLocX, SecondLocY, SecondLocZ));

        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);

        try(EditSession editSession = WorldEdit.getInstance().newEditSession((World) world)){
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

    }

}
