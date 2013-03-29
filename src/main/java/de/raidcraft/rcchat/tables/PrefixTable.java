package de.raidcraft.rcchat.tables;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.database.Table;
import de.raidcraft.rcchat.prefix.Prefix;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Philip
 */
public class PrefixTable extends Table {

    public PrefixTable() {

        super("prefixes", "rcchat_");
    }

    @Override
    public void createTable() {

        try {
            getConnection().prepareStatement(
                    "CREATE TABLE `" + getTableName() + "` (" +
                            "`id` INT NOT NULL AUTO_INCREMENT, " +
                            "`prefix` VARCHAR( 32 ) NOT NULL, " +
                            "`permission` VARCHAR( 64 ) DEFAULT NULL, " +
                            "`priority` INT( 11 ) DEFAULT 0, " +
                            "PRIMARY KEY ( `id` )" +
                            ")").execute();
        } catch (SQLException e) {
            RaidCraft.LOGGER.warning(e.getMessage());
        }
    }

    public List<Prefix> getPrefixes() {

        List<Prefix> prefixes = new ArrayList<>();
        try {
            ResultSet resultSet = getConnection().prepareStatement(
                    "SELECT * FROM " + getTableName() + ";").executeQuery();

            while (resultSet.next()) {

                Prefix prefix = new Prefix(
                    resultSet.getInt("id"),
                    resultSet.getString("prefix"),
                    resultSet.getString("permission"),
                    resultSet.getInt("priority")
                );
                prefixes.add(prefix);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prefixes;
    }
}
