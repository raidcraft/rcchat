package de.raidcraft.rcchat.channel;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcchat.player.ChatPlayer;
import de.raidcraft.rcchat.player.PlayerManager;
import de.raidcraft.rcchat.tables.PlayersTable;
import de.raidcraft.util.SignUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Philip
 */
public class Channel {

    private String name;
    private String permission;
    private String prefix;
    private List<String> aliases = new ArrayList<>();
    private ChannelType type;

    private Map<String, ChatPlayer> members = new HashMap<>();

    public Channel(String name, String permission, String prefix, String[] aliases, String type) {

        this.name = name;
        this.permission = permission;
        if(prefix != null) {
            this.prefix = SignUtil.parseColor(prefix);
        } else {
            this.prefix = "";
        }

        for(String alias : aliases) {
            this.aliases.add(alias);
        }
        this.type = ChannelType.valueOf(type);
    }

    public void sendMessage(String message) {

        for(Map.Entry<String, ChatPlayer> entry : members.entrySet()) {

            ChatPlayer chatPlayer = entry.getValue();

            if(chatPlayer.isMuted()) return;

            if(chatPlayer.getPlayer().isOnline()) {
                chatPlayer.getPlayer().sendMessage(message);
            }
        }
    }

    public void join(Player player) {

        if(!members.containsKey(player.getName())) {
            members.put(player.getName(), new ChatPlayer(player));
        }
        Channel main = PlayerManager.INST.getMainChannel(player);
        if(main == null || !main.getName().equalsIgnoreCase(name)) {
            PlayerManager.INST.setMainChannel(player, this);
            RaidCraft.getTable(PlayersTable.class).addChannel(player, this);
        }
    }

    public void leave(Player player) {

        logout(player);
        RaidCraft.getTable(PlayersTable.class).removeChannel(player, this);
    }

    public void logout(Player player) {

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

    public List<String> getAliases() {

        return aliases;
    }

    public ChannelType getType() {

        return type;
    }
}
