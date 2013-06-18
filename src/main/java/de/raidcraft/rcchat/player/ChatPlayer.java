package de.raidcraft.rcchat.player;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcchat.RCChatPlugin;
import de.raidcraft.rcchat.bungeecord.messages.ChannelChatMessage;
import de.raidcraft.rcchat.bungeecord.messages.PrivateChatMessage;
import de.raidcraft.rcchat.channel.Channel;
import de.raidcraft.rcchat.namecolor.NameColorManager;
import de.raidcraft.rcchat.prefix.PlayerPrefix;
import de.raidcraft.rcchat.prefix.PrefixManager;
import de.raidcraft.rcmultiworld.BungeeManager;
import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;
import de.raidcraft.skills.SkillsPlugin;
import de.raidcraft.skills.api.hero.Hero;
import de.raidcraft.skills.api.profession.Profession;
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
    private PlayerPrefix prefix;
    private String suffix;
    private String nameColor;
    private String chatPartner = null;
    private String lastPrivateSender = null;

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

    public PlayerPrefix getPrefix() {

        Hero hero = RaidCraft.getComponent(SkillsPlugin.class).getCharacterManager().getHero(player);
        if(prefix == null && !player.hasPermission("rcchat.admin") && hero.getAttachedLevel().getLevel() >= 5) {
            this.prefix = PrefixManager.INST.getPrefix(player);
        }
        if(prefix == null) {
            return PrefixManager.GUEST_PREFIX;
        }
        return prefix;
    }

    public void setPrefix(PlayerPrefix prefix) {

        this.prefix = prefix;
    }

    public String getSuffix() {

        if(suffix == null) {
                try {
                    if(getPrefix().hasPermission()) {
                        Hero hero = RaidCraft.getComponent(SkillsPlugin.class).getCharacterManager().getHero(player);
                        String[] professionName = getPrefix().getPermission().split("\\.");
                        Profession profession = hero.getProfession(professionName[professionName.length - 1]);
                        if(profession != null) {

                            int level = profession.getAttachedLevel().getLevel();
                            suffix = "[" + ChatColor.YELLOW + level + ChatColor.RESET + "]";
                        }
                    }
                } catch (Throwable e) {
//                    e.printStackTrace();
                }
            }
        if(suffix == null) {
            suffix = ChatColor.GREEN + "#";
        }
        return suffix;
    }

    public void setSuffix(String suffix) {

        this.suffix = suffix;
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

    public void setLastPrivateSender(String sender) {

        lastPrivateSender = sender;
    }

    public String getLastPrivateSender() {
        return lastPrivateSender;
    }

    public void sendMessage(String message) {

        if(player.hasPermission("rcchat.message.colorized")) {
            message = SignUtil.parseColor(message);
        }

        if(getMainChannel() == null) {
            player.sendMessage(ChatColor.RED + "Du schreibst in keinem Channel!");
            return;
        }
        String channelPrefix = mainChannel.getPrefix();
        if(channelPrefix.length() > 0) {
            channelPrefix += " ";
        }

        String worldPrefix = PrefixManager.INST.getWorldPrefix(player.getLocation().getWorld().getName());
        String prefix = this.getPrefix().getParsedPrefix();
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
        ChatPlayer chatPlayer = ChatPlayerManager.INST.getPlayer(recipient);
        chatPlayer.setLastPrivateSender(sender);
    }
}
