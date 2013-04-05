package de.raidcraft.rcchat.tables;

import de.raidcraft.api.database.Table;
import de.raidcraft.rcchat.prefix.WorldPrefix;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Philip
 */
public class WorldPrefixTable extends Table {

    public WorldPrefixTable() {

        super("world_prefixes", "rcchat_");
    }

    @Override
    public void createTable() {

        try {
            getConnection().prepareStatement(
                    "CREATE TABLE `" + getTableName() + "` (" +
                            "`id` INT NOT NULL AUTO_INCREMENT, " +
                            "`world` VARCHAR( 32 ) NOT NULL, " +
                            "`prefix` VARCHAR( 32 ) DEFAULT NULL, " +
                            "PRIMARY KEY ( `id` )" +
                            ")").execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<WorldPrefix> getPrefixes() {

        List<WorldPrefix> worldPrefixes = new ArrayList<>();
        try {
            ResultSet resultSet = getConnection().prepareStatement(
                    "SELECT * FROM " + getTableName() + ";").executeQuery();

            while (resultSet.next()) {

                WorldPrefix worldPrefix = new WorldPrefix(
                    resultSet.getString("prefix"),
                    resultSet.getString("world")
                );
                worldPrefixes.add(worldPrefix);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return worldPrefixes;
    }
}
