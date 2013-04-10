package de.raidcraft.rcchat.tables;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.database.Table;
import de.raidcraft.rcchat.channel.Channel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Philip
 */
public class ChannelsTable extends Table {

    public ChannelsTable() {

        super("channels", "rcchat_");
    }

    @Override
    public void createTable() {

        try {
            executeUpdate(
                    "CREATE TABLE `" + getTableName() + "` (" +
                            "`id` INT NOT NULL AUTO_INCREMENT, " +
                            "`name` VARCHAR( 32 ) NOT NULL, " +
                            "`permission` VARCHAR( 64 ) DEFAULT NULL, " +
                            "`prefix` VARCHAR ( 32 ) DEFAULT NULL, " +
                            "`color` VARCHAR ( 32 ) DEFAULT NULL," +
                            "`aliases` VARCHAR ( 128 ) NOT NULL, " +
                            "`type` VARCHAR ( 32 ) NOT NULL, " +
                            "PRIMARY KEY ( `id` )" +
                            ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Channel> getChannels() {

        List<Channel> channels = new ArrayList<>();
        try {
            ResultSet resultSet = executeQuery(
                    "SELECT * FROM " + getTableName() + ";");

            while (resultSet.next()) {
                String[] aliases = resultSet.getString("aliases").split(",");

                Channel channel = new Channel(
                    resultSet.getString("name"),
                    resultSet.getString("permission"),
                    resultSet.getString("prefix"),
                    resultSet.getString("color"),
                    aliases,
                    resultSet.getString("type"),
                    RaidCraft.getTable(ChannelWorldsTable.class).getWorlds(resultSet.getInt("id"))
                );
                channels.add(channel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channels;
    }
}
