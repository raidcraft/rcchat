package de.raidcraft.rcchat.prefix;

/**
 * @author Philip
 */
public class Prefix {

    private int id;
    private String prefix;
    private String permission;
    private int priority;

    public Prefix(int id, String prefix, String permission, int priority) {

        this.id = id;
        this.prefix = prefix;
        this.permission = permission;
        this.priority = priority;
    }

    public int getId() {

        return id;
    }

    public String getPrefix() {

        return prefix;
    }

    public int getPriority() {

        return priority;
    }

    public boolean hasPermission() {

        return (permission != null) ? true : false;
    }

    public String getPermission() {

        return permission;
    }
}
