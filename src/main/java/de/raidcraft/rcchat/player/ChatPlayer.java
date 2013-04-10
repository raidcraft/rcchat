package de.raidcraft.rcchat.player;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcchat.RCChatPlugin;
import de.raidcraft.rcchat.bungeecord.BungeeCordManager;
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
    private Player chatPartner = null;

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
                try {
                    int heroLevel = 0;
                    heroLevel = RaidCraft.getComponent(SkillsPlugin.class).getCharacterManager().getHero(player).getAttachedLevel().getLevel();
                    suffix = "[" + ChatColor.YELLOW + heroLevel + ChatColor.RESET + "]";
                } catch (Throwable e) {
                    suffix = "";
                }
            }
        }
        return suffix;
    }

    public String getNameColor() {

        if(nameColor == null) {
            if(RaidCraft.getComponent(RCChatPlugin.class).config.coloredNames) {
                nameColor = NameColorManager.getColor(player);
            }
            else {
                nameColor = "";
            }
        }
        return nameColor;
    }

    public boolean hasPrivateChat() {
        if(chatPartner != null && chatPartner.isOnline()) {
            return true;
        }
        else {
            chatPartner = null;
            return false;
        }
    }

    public void enterPrivateChat(Player recipient) {
        chatPartner = recipient;
    }

    public void leavePrivateChat() {
        chatPartner = null;
    }

    public Player getChatPartner() {
        return chatPartner;
    }

    public void sendMessageToPartner(String message) {
        if(!hasPrivateChat()) {
            return;
        }
        RaidCraft.LOGGER.info(getName() + " -> " + chatPartner.getName() + ": " + ChatColor.stripColor(message));
        getChatPartner().sendMessage(ChatColor.DARK_PURPLE + "Von " + getName() + ": " + ChatColor.LIGHT_PURPLE + message);
        getPlayer().sendMessage(ChatColor.DARK_PURPLE + "An " + getChatPartner().getName() + ": " + ChatColor.LIGHT_PURPLE + message);
    }

    public void sendMessage(String message) {

        if(getMainChannel() == null) {
            player.sendMessage(ChatColor.RED + "Du schreibst in keinem Channel!");
            return;
        }
        String channelPrefix = mainChannel.getPrefix();
        if(channelPrefix.length() > 0) {
            channelPrefix += " ";
        }

        String worldPrefix = PrefixManager.INST.getWorldPrefix(player.getLocation().getWorld().getName());
        String prefix = getPrefix();
        String suffix = getSuffix();
        String nameColor = getNameColor();
        String channelColor = mainChannel.getColor();

        message = worldPrefix + ChatColor.RESET + channelPrefix + ChatColor.RESET  + prefix + ChatColor.RESET + nameColor +
                player.getName() + ChatColor.RESET + suffix + ChatColor.RESET + ": " + channelColor + message;

        RaidCraft.LOGGER.info(ChatColor.stripColor(message));
        mainChannel.sendMessage(message);
        BungeeCordManager.INST.sendMessage(player, mainChannel, message, BungeeCordManager.MessageType.CHAT_MESSAGE);
    }
}
