package com.therift.fortgame.ConfigData.Database;

import com.therift.fortgame.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerManager {
    private Main main;
    private UUID uuid;
    private String username;
    private String SoloStructerName;
    private String MultiplayerStructerName;


    public PlayerManager(Main main, UUID uuid){

        this.main = main;
        this.uuid = uuid;

        Player player = Bukkit.getPlayer(uuid);

        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("SELECT * FROM PlayerData WHERE UUID = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                this.username = rs.getString("Username");
                this.SoloStructerName = rs.getString("SoloStructurName");
                this.MultiplayerStructerName = rs.getString("MultiplayerFortName");
            }else {
                PreparedStatement preparedStatement = main.getDatabase().getConnection().prepareStatement("INSERT INTO PlayerData (UUID, Username, SoloStructurName) VALUES (?,?,?)");
                preparedStatement.setString(1, uuid.toString());
                preparedStatement.setString(2, player.getName());
                preparedStatement.setString(3, "0");
                preparedStatement.executeUpdate();
                this.username = player.getName();
                this.SoloStructerName = "0";
                this.MultiplayerStructerName = "0";
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

    public String getMultiplayerStructerName() {
        return MultiplayerStructerName;
    }

    public void setMultiplayerStructerName(String multiplayerStructerNameChange) {
        try {
            PreparedStatement ps = main.getDatabase().getConnection().prepareStatement("UPDATE PlayerData SET MultiplayerFortName = ? WHERE UUID = ?");
            ps.setString(1, multiplayerStructerNameChange);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
            this.MultiplayerStructerName = multiplayerStructerNameChange;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public List<UUID> getAllInMultiPlayerGroup(){
        PreparedStatement ps = null;
        try {
            ps = main.getDatabase().getConnection().prepareStatement("SELECT UUID FROM PlayerData WHERE MultiplayerFortName = ?");
            ps.setString(1, MultiplayerStructerName);
            ResultSet rs = ps.executeQuery();
            List<UUID> a = new ArrayList<>();
            while (rs.next()){
                a.add(UUID.fromString(rs.getString("UUID")));
            }
            return a;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UUID getUuid() {
        return uuid;
    }
}
