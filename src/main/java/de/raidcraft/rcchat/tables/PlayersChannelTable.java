package de.raidcraft.rcchat.tables;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.database.Table;
import de.raidcraft.rcchat.channel.Channel;
import de.raidcraft.rcchat.player.AssignmentType;
import de.raidcraft.rcchat.player.ChannelAssignment;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Philip
 */
public class PlayersChannelTable extends Table {

    public PlayersChannelTable() {

        super("players_channel", "rcchat_");
    }

    @Override
    public void createTable() {

        try {
            executeUpdate(
                    "CREATE TABLE `" + getTableName() + "` (" +
                            "`id` INT NOT NULL AUTO_INCREMENT, " +
                            "`player` VARCHAR( 32 ) NOT NULL, " +
                            "`channel` VARCHAR( 64 ) DEFAULT NULL, " +
                            "`type` VARCHAR( 32 ) NOT NULL, " +
                            "PRIMARY KEY ( `id` )" +
                            ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ChannelAssignment> getChannels(Player player) {

        List<ChannelAssignment> channels = new ArrayList<>();

        try {
            ResultSet resultSet = executeQuery(
                    "SELECT * FROM " + getTableName() + " WHERE player = '" + player.getName() + "';");

            while (resultSet.next()) {

                channels.add(new ChannelAssignment(resultSet.getString("channel"), resultSet.getString("type")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channels;
    }

    public void addChannel(Player player, Channel channel) {

        removeChannel(player, channel);
        removeMainTag(player);

        try {
            executeUpdate("INSERT INTO " + getTableName() + " (player, channel, type) " +
                    "VALUES (" +
                    "'" + player.getName() + "'" + "," +
                    "'" + channel.getName() + "'" + "," +
                    "'" + AssignmentType.MAIN.name() + "'" +
                    ");");
        } catch (SQLException e) {
            RaidCraft.LOGGER.warning(e.getMessage());
            e.printStackTrace();
        }
    }

    public void removeChannel(Player player, Channel channel) {

        try {
            executeUpdate(
                    "DELETE FROM " + getTableName() + " WHERE player = '" + player.getName() + "' AND channel = '" + channel.getName() + "';");
        } catch (SQLException e) {
            RaidCraft.LOGGER.warning(e.getMessage());
            e.printStackTrace();
        }
    }

    public void removeMainTag(Player player) {

        try {
            executeUpdate(
                    "UPDATE " + getTableName() + " SET type = '" + AssignmentType.NORMAL.name() + "' " +
                            "WHERE player = '" + player.getName() + "' AND type = '" + AssignmentType.MAIN.name() + "';");
        } catch (SQLException e) {
            RaidCraft.LOGGER.warning(e.getMessage());
            e.printStackTrace();
        }
    }
}
