package de.raidcraft.rcchat.prefix;

import de.raidcraft.util.SignUtil;

/**
 * @author Philip
 */
public class PlayerPrefix {

    private int id;
    private String prefix;
    private String permission;
    private int priority;

    public PlayerPrefix(int id, String prefix, String permission, int priority) {

        this.id = id;
        this.prefix = prefix;
        this.permission = permission;
        this.priority = priority;
    }

    public int getId() {

        return id;
    }

    public String getParsedPrefix() {

        return SignUtil.parseColor(prefix);
    }

    public int getPriority() {

        return priority;
    }

    public boolean hasPermission() {

        return (permission != null);
    }

    public String getPermission() {

        return permission;
    }
}
