package de.raidcraft.rcchat.tables;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.database.Table;
import de.raidcraft.rcchat.prefix.PlayerPrefix;
import de.raidcraft.rcchat.prefix.PrefixManager;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Philip
 */
public class PlayersPrefixTable extends Table {

    public PlayersPrefixTable() {

        super("players_prefix", "rcchat_");
    }

    @Override
    public void createTable() {

        try {
            executeUpdate(
                    "CREATE TABLE `" + getTableName() + "` (" +
                            "`id` INT NOT NULL AUTO_INCREMENT, " +
                            "`player` VARCHAR( 32 ) NOT NULL, " +
                            "`prefix` INT( 11 ) NOT NULL, " +
                            "PRIMARY KEY ( `id` )" +
                            ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PlayerPrefix getPrefix(Player player) {

        try {
            ResultSet resultSet = executeQuery(
                    "SELECT * FROM " + getTableName() + " WHERE player = '" + player.getName() + "';");

            while (resultSet.next()) {
                return PrefixManager.INST.getPrefix(resultSet.getInt("prefix"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void savePrefix(Player player, PlayerPrefix playerPrefix) {

        removePlayer(player);
        try {
            executeUpdate("INSERT INTO " + getTableName() + " (player, prefix) " +
                    "VALUES (" +
                    "'" + player.getName() + "'" + "," +
                    "'" + playerPrefix.getId() + "'" +
                    ");");
        } catch (SQLException e) {
            RaidCraft.LOGGER.warning(e.getMessage());
            e.printStackTrace();
        }
    }

    public void removePlayer(Player player) {

        try {
            executeUpdate(
                    "DELETE FROM " + getTableName() + " WHERE player = '" + player.getName() + "';");
        } catch (SQLException e) {
            RaidCraft.LOGGER.warning(e.getMessage());
            e.printStackTrace();
        }
    }
}
