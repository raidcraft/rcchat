package de.raidcraft.rcchat.player;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcchat.RCChatPlugin;
import de.raidcraft.rcchat.bungeecord.messages.ChannelChatMessage;
import de.raidcraft.rcchat.bungeecord.messages.PrivateChatMessage;
import de.raidcraft.rcchat.channel.Channel;
import de.raidcraft.rcchat.namecolor.NameColorManager;
import de.raidcraft.rcchat.prefix.PrefixManager;
import de.raidcraft.rcmultiworld.BungeeManager;
import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;
import de.raidcraft.skills.SkillsPlugin;
import de.raidcraft.util.SignUtil;
import org.bukkit.Bukkit;
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
    private String chatPartner = null;

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
        if(chatPartner != null) {
            return true;
        }
        else {
            chatPartner = null;
            return false;
        }
    }

    public void enterPrivateChat(String recipient) {
        chatPartner = recipient;
    }

    public void leavePrivateChat() {
        chatPartner = null;
    }

    public String getChatPartner() {
        return chatPartner;
    }

    public void sendMessageToPartner(String message) {
        if(!hasPrivateChat()) {
            return;
        }
        Player recipient = Bukkit.getPlayer(chatPartner);
        if(recipient == null) {
            BungeeManager bungeeManager = RaidCraft.getComponent(RCMultiWorldPlugin.class).getBungeeManager();
            bungeeManager.sendMessage(player, new PrivateChatMessage(getName(), chatPartner, message));
        }
        else {
            sendPrivateMessage(recipient, getName(), message);
        }
        getPlayer().sendMessage(ChatColor.DARK_PURPLE + "An " + chatPartner + ": " + ChatColor.LIGHT_PURPLE + message);
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

        BungeeManager bungeeManager = RaidCraft.getComponent(RCMultiWorldPlugin.class).getBungeeManager();
        bungeeManager.sendMessage(player, new ChannelChatMessage(mainChannel.getName(), message));
    }

    /*
     * Static method because of incoming Bungee private messages
     */
    public static void sendPrivateMessage(Player recipient, String sender, String message) {

        RaidCraft.LOGGER.info(sender + " -> " + recipient.getName() + ": " + ChatColor.stripColor(message));
        recipient.sendMessage(ChatColor.DARK_PURPLE + "Von " + sender + ": " + ChatColor.LIGHT_PURPLE + message);
    }
}
