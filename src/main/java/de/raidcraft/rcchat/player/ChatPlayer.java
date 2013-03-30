package de.raidcraft.rcchat.player;

import de.raidcraft.rcchat.channel.Channel;
import de.raidcraft.rcchat.namecolor.NameColorManager;
import de.raidcraft.rcchat.prefix.PrefixManager;
import de.raidcraft.util.SignUtil;
import org.bukkit.entity.Player;

/**
 * @author Philip
 */
public class ChatPlayer {

    private Player player;
    private Channel mainChannel;
    private String prefix;
    private String suffix;
    private String nameColor;

    public ChatPlayer(Player player) {

        this.player = player;
    }

    public Player getPlayer() {

        return player;
    }

    public String getName() {

        return player.getName();
    }

    public Channel getMainChannel() {

        return mainChannel;
    }

    public void setMainChannel(Channel mainChannel) {

        this.mainChannel = mainChannel;
    }

    public String getPrefix() {

        if(prefix == null) {
            prefix = SignUtil.parseColor(PrefixManager.INST.getPrefix(player));
        }
        return prefix;
    }

    public String getNameColor() {

        if(nameColor == null) {
            nameColor = NameColorManager.getColor(player);
        }
        return nameColor;
    }
}
