package de.raidcraft.rcchat.player;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcchat.channel.Channel;
import de.raidcraft.rcchat.namecolor.NameColorManager;
import de.raidcraft.rcchat.prefix.PrefixManager;
import de.raidcraft.skills.SkillsPlugin;
import de.raidcraft.util.SignUtil;
import org.bukkit.ChatColor;
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

    public String getSuffix() {

        if(suffix == null) {
            if(player.hasPermission("rcchat.suffix.admin")) {
                suffix = ChatColor.GREEN + "#";
            }
            else {
                int heroLevel = RaidCraft.getComponent(SkillsPlugin.class).getCharacterManager().getHero(player).getAttachedLevel().getLevel();
                suffix = "[" + ChatColor.YELLOW + heroLevel + ChatColor.RESET + "]";
            }
        }
        return suffix;
    }

    public String getNameColor() {

        if(nameColor == null) {
            nameColor = NameColorManager.getColor(player);
        }
        return nameColor;
    }
}
