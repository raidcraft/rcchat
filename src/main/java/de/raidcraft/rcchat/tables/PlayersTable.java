package de.raidcraft.rcchat.tables;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.database.Table;
import de.raidcraft.rcchat.channel.Channel;
import de.raidcraft.rcchat.player.ChannelAssignment;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Philip
 */
public class PlayersTable extends Table {

    public PlayersTable() {

        super("channels", "rcchat_");
    }

    @Override
    public void createTable() {

        try {
            getConnection().prepareStatement(
                    "CREATE TABLE `" + getTableName() + "` (" +
                            "`id` INT NOT NULL AUTO_INCREMENT, " +
                            "`player` VARCHAR( 32 ) NOT NULL, " +
                            "`channel` VARCHAR( 64 ) DEFAULT NULL, " +
                            "`type` TINYINT( 1 ) NOT NULL, " +
                            "PRIMARY KEY ( `id` )" +
                            ")").execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ChannelAssignment> getChannels(Player player) {

        List<ChannelAssignment> channels = new ArrayList<>();

        try {
            ResultSet resultSet = getConnection().prepareStatement(
                    "SELECT * FROM " + getTableName() + " WHERE player = '" + player.getName() + "';").executeQuery();

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

        try {
            getConnection().prepareStatement("INSERT INTO " + getTableName() + " (player, channel) " +
                    "VALUES (" +
                    "'" + player.getName() + "'" + "," +
                    "'" + channel.getName() + "'" +
                    ");").executeUpdate();
        } catch (SQLException e) {
            RaidCraft.LOGGER.warning(e.getMessage());
            e.printStackTrace();
        }
    }

    public void removeChannel(Player player, Channel channel) {

        try {
            getConnection().prepareStatement(
                    "DELETE FROM " + getTableName() + " WHERE player = '" + player.getName() + "' AND channel = '" + channel.getName() + "';").execute();
        } catch (SQLException e) {
            RaidCraft.LOGGER.warning(e.getMessage());
            e.printStackTrace();
        }
    }
}
