package de.raidcraft.rcchat;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Philip
 */
public class Channel {

    private String name;
    private String permission;
    private String prefix;
    private Map<String, ChatPlayer> members = new HashMap<>();

    public Channel(String name, String permission, String prefix) {

        this.name = name;
        this.permission = permission;
        this.prefix = prefix;
    }

    public void sendMessage(String message) {

        for(Map.Entry<String, ChatPlayer> entry : members.entrySet()) {
            if(entry.getValue().isMuted()) return;

            entry.getValue().getPlayer().sendMessage(message);
        }
    }

    public void join(Player player) {

        if(members.containsKey(player.getName())) return;
        members.put(player.getName(), new ChatPlayer(player));
    }

    public void leave(Player player) {
        members.remove(player.getName());
    }

    public boolean hasPermission() {

        return (permission != null) ? false : true;
    }

    public String getName() {

        return name;
    }

    public String getPermission() {

        return permission;
    }

    public String getPrefix() {

        return prefix;
    }
}
