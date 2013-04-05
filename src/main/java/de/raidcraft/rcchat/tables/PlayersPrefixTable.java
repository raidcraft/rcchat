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
            getConnection().prepareStatement(
                    "CREATE TABLE `" + getTableName() + "` (" +
                            "`id` INT NOT NULL AUTO_INCREMENT, " +
                            "`player` VARCHAR( 32 ) NOT NULL, " +
                            "`prefix` INT( 11 ) NOT NULL, " +
                            "PRIMARY KEY ( `id` )" +
                            ")").execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PlayerPrefix getPrefix(Player player) {

        try {
            ResultSet resultSet = getConnection().prepareStatement(
                    "SELECT * FROM " + getTableName() + " WHERE player = '" + player.getName() + "';").executeQuery();

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
            getConnection().prepareStatement("INSERT INTO " + getTableName() + " (player, prefix) " +
                    "VALUES (" +
                    "'" + player.getName() + "'" + "," +
                    "'" + playerPrefix.getId() + "'" +
                    ");").executeUpdate();
        } catch (SQLException e) {
            RaidCraft.LOGGER.warning(e.getMessage());
            e.printStackTrace();
        }
    }

    public void removePlayer(Player player) {

        try {
            getConnection().prepareStatement(
                    "DELETE FROM " + getTableName() + " WHERE player = '" + player.getName() + "';").execute();
        } catch (SQLException e) {
            RaidCraft.LOGGER.warning(e.getMessage());
            e.printStackTrace();
        }
    }
}
