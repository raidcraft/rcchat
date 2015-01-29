package de.raidcraft.rcchat.tables;

import de.raidcraft.api.database.Table;
import de.raidcraft.rcchat.prefix.PlayerPrefix;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Philip
 */
public class PlayerPrefixTable extends Table {

    public PlayerPrefixTable() {

        super("player_prefixes", "rcchat_");
    }

    @Override
    public void createTable() {

        try {
            executeUpdate(
                    "CREATE TABLE `" + getTableName() + "` (" +
                            "`id` INT NOT NULL AUTO_INCREMENT, " +
                            "`prefix` VARCHAR( 32 ) NOT NULL, " +
                            "`permission` VARCHAR( 64 ) DEFAULT NULL, " +
                            "`priority` INT( 11 ) DEFAULT 0, " +
                            "PRIMARY KEY ( `id` )" +
                            ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<PlayerPrefix> getPrefixes() {

        List<PlayerPrefix> playerPrefixes = new ArrayList<>();
        try {
            ResultSet resultSet = executeQuery(
                    "SELECT * FROM " + getTableName() + ";");

            while (resultSet.next()) {

                PlayerPrefix playerPrefix = new PlayerPrefix(
                        resultSet.getInt("id"),
                        resultSet.getString("prefix"),
                        resultSet.getString("permission"),
                        resultSet.getInt("priority")
                );
                playerPrefixes.add(playerPrefix);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return playerPrefixes;
    }
}
