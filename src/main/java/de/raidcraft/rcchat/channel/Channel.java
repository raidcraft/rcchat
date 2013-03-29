package de.raidcraft.rcchat.channel;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcchat.player.ChatPlayer;
import de.raidcraft.rcchat.player.PlayerManager;
import de.raidcraft.rcchat.tables.PlayersChannelTable;
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

            if(chatPlayer.getPlayer().isOnline()) {
                chatPlayer.getPlayer().sendMessage(message);
            }
        }
    }

    public void join(Player player) {

        ChatPlayer chatPlayer = PlayerManager.INST.getPlayer(player);
        join(chatPlayer);
    }

    public void join(ChatPlayer chatPlayer) {

        if(!members.containsKey(chatPlayer.getName())) {
            members.put(chatPlayer.getName(), chatPlayer);
        }
        Channel main = chatPlayer.getMainChannel();
        if(main == null || !main.getName().equalsIgnoreCase(name)) {
            chatPlayer.setMainChannel(this);
            RaidCraft.getTable(PlayersChannelTable.class).addChannel(chatPlayer.getPlayer(), this);
        }
    }

    public void leave(Player player) {

        logout(player);
        RaidCraft.getTable(PlayersChannelTable.class).removeChannel(player, this);
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
