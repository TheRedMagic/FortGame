package com.therift.fortgame.ConfigData.Database;

import com.therift.fortgame.Main;
import com.therift.theriftcore.Database.DatabaseManager.RiftPlayer;
import org.bukkit.Bukkit;

import javax.xml.transform.Result;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PlayerManager {
    private Main main;
    private UUID uuid;
    private String username;
    private String SoloStructerName;


    public PlayerManager(Main main, UUID uuid){

        this.main = main;
        this.uuid = uuid;

        RiftPlayer player = new RiftPlayer(uuid);

        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT * FROM PlayerData WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                this.username = rs.getString("Username");
                this.SoloStructerName = rs.getString("SoloStructurName");
            }else {
                PreparedStatement preparedStatement = main.getDatabase().getConnection().prepareStatement("INSERT INTO PLayerData (UUID, Username, SoloStructurName) VALUES (?,?,?)");
                preparedStatement.setString(1, uuid.toString());
                preparedStatement.setString(2, player.getName());
                preparedStatement.setString(3, "0");
                preparedStatement.executeUpdate();
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    public void setSoloStructureName(String soloStructureName){
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("UPDATE PlayerData SET SoloStructurName = ? WHERE UUID = ?");
            ps.setString(1, soloStructureName);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
            this.SoloStructerName = soloStructureName;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public String getSoloStructerName() {
        return SoloStructerName;
    }

    public UUID getUuid() {
        return uuid;
    }
}
