package de.raidcraft.rcchat.tables;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.database.Table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Philip
 */
public class ChannelWorldsTable extends Table {

    public ChannelWorldsTable() {

        super("channel_worlds", "rcchat_");
    }

    @Override
    public void createTable() {

        try {
            getConnection().prepareStatement(
                    "CREATE TABLE `" + getTableName() + "` (" +
                            "`id` INT NOT NULL AUTO_INCREMENT, " +
                            "`channel` INT( 11 ) NOT NULL, " +
                            "`world` VARCHAR( 32 ) DEFAULT NULL, " +
                            "PRIMARY KEY ( `id` )" +
                            ")").execute();
        } catch (SQLException e) {
            RaidCraft.LOGGER.warning(e.getMessage());
        }
    }

    public List<String> getWorlds(int channelId) {

        List<String> worlds = new ArrayList<>();
        try {
            ResultSet resultSet = getConnection().prepareStatement(
                    "SELECT * FROM " + getTableName() + " WHERE channel = '" + channelId + "';").executeQuery();

            while (resultSet.next()) {

                worlds.add(resultSet.getString("world"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return worlds;
    }
}
