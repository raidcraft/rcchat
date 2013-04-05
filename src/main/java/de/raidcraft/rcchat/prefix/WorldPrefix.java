package de.raidcraft.rcchat.prefix;

import de.raidcraft.util.SignUtil;

/**
 * @author Philip
 */
public class WorldPrefix {

    private String prefix;
    private String world;

    public WorldPrefix(String prefix, String world) {

        this.prefix = SignUtil.parseColor(prefix);
        this.world = world;
    }

    public String getPrefix() {

        return prefix;
    }

    public String getWorld() {

        return world;
    }
}
