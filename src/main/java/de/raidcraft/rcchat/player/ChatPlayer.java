package de.raidcraft.rcchat.player;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.chat.Chat;
import de.raidcraft.api.items.CustomItemStack;
import de.raidcraft.rcchat.RCChatPlugin;
import de.raidcraft.rcchat.bungeecord.messages.ChannelChatMessage;
import de.raidcraft.rcchat.bungeecord.messages.PrivateChatMessage;
import de.raidcraft.rcchat.channel.Channel;
import de.raidcraft.rcchat.channel.ChannelManager;
import de.raidcraft.rcchat.namecolor.NameColorManager;
import de.raidcraft.rcchat.prefix.PlayerPrefix;
import de.raidcraft.rcchat.prefix.PrefixManager;
import de.raidcraft.rcmultiworld.BungeeManager;
import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;
import de.raidcraft.util.SignUtil;
import mkremins.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Philip
 */
public class ChatPlayer {

    public static final char ITEM_COMPLETE_CHAR = '#';
    private static final Pattern ITEM_COMPLETE_PATTERN = Pattern.compile("(.*)\\?\"([a-zA-ZüöäÜÖÄß\\s\\d]+)\"(.*)");
    public static final char QUEST_COMPLETE_CHAR = '?';
    public static final char ACHIEVEMENT_COMPLETE_CHAR = '!';

    private Player player;
    private Channel mainChannel;
    private PlayerPrefix prefix;
    private String suffix;
    private String nameColor;
    private String chatPartner = null;
    private String lastPrivateSender = null;
    private final List<CustomItemStack> autocompleteItems = new ArrayList<>();

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

        if (mainChannel == null) mainChannel = ChannelManager.INST.getDefaultChannel();
        return mainChannel;
    }

    public void setMainChannel(Channel mainChannel) {

        this.mainChannel = mainChannel;
    }

    public PlayerPrefix getPrefix() {

        if (prefix == null) {
            this.prefix = PrefixManager.INST.getPrefix(player);
        }
        if (prefix == null && RaidCraft.getComponent(RCChatPlugin.class).config.displayGuestPrefix) {
            return PrefixManager.GUEST_PREFIX;
        }
        return prefix;
    }

    public void setPrefix(PlayerPrefix prefix) {

        this.prefix = prefix;
    }

    public String getSuffix() {

        if (suffix == null && !player.hasPermission("rcchat.suffix.admin")) {
/*            try {
                if (getPrefix().hasPermission() && RaidCraft.getComponent(SkillsPlugin.class) != null) {
                    Hero hero = RaidCraft.getComponent(SkillsPlugin.class).getCharacterManager().getHero(player);
                    // lets try to parse the profession from the prefix permission
                    String[] split = getPrefix().getPermission().split("\\.");
                    String last = split[split.length - 1];
                    Profession profession = hero.getProfession(last);
                    int level;
                    if (profession != null) {
                        level = profession.getPath().getTotalPathLevel(hero);
                    } else {
                        level = hero.getPlayerLevel();
                    }
                    suffix = "[" + ChatColor.YELLOW + level + ChatColor.RESET + "]";
                }
            } catch (UnknownSkillException | UnknownProfessionException ignored) {
            }*/
        }
        if (suffix == null) {
            suffix = ChatColor.GREEN + "#";
        }
        return suffix;
    }

    public void setSuffix(String suffix) {

        this.suffix = suffix;
    }

    public String getNameColor() {

        if (nameColor == null) {
            if (RaidCraft.getComponent(RCChatPlugin.class).config.coloredNames) {
                nameColor = NameColorManager.getColor(player);
            } else {
                nameColor = "";
            }
        }
        return nameColor;
    }

    public boolean hasPrivateChat() {
        if (chatPartner != null) {
            return true;
        } else {
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
        if (!hasPrivateChat()) {
            return;
        }
        Player recipient = Bukkit.getPlayer(chatPartner);
        if(recipient == null) {
            BungeeManager bungeeManager = RaidCraft.getComponent(RCMultiWorldPlugin.class).getBungeeManager();
            bungeeManager.sendMessage(player, new PrivateChatMessage(getName(), chatPartner, message));
        } else {
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

        //        if(!player.hasPermission("raidcraft.player")) {
        //
        //            message = WeblinkUtil.obfuscateWeblinks(message);
        //        }

        if (player.hasPermission("rcchat.message.colorized")) {
            message = SignUtil.parseColor(message);
        }

        if (getMainChannel() == null) {
            player.sendMessage(ChatColor.RED + "Du schreibst in keinem Channel!");
            return;
        }
        String channelPrefix = getMainChannel().getPrefix();
        if (channelPrefix.length() > 0) {
            channelPrefix += " ";
        }

        String worldPrefix = PrefixManager.INST.getWorldPrefix(player.getLocation().getWorld().getName());
        String prefix = PrefixManager.GUEST_PREFIX.getParsedPrefix();
        String suffix = "";
        String nameColor = ChatColor.DARK_GRAY.toString();
        String channelColor = ChatColor.DARK_GRAY.toString();

        if (player.hasPermission("raidcraft.player")) {
            prefix = (getPrefix() != null ? getPrefix().getParsedPrefix() : "");
            suffix = getSuffix();
            nameColor = getNameColor();
            channelColor = getMainChannel().getColor();
        }

        // check if the hero is cached to prevent async scoreboard creation
        boolean cached = false;
/*      cached = HeroUtil.isCachedHero(player.getUniqueId());

        if (cached) {
            FancyMessage result = new FancyMessage(worldPrefix)
                    .then(channelPrefix)
                    .command("/ch " + getMainChannel().getName())
                    .tooltip("Klicke hier um in den Channel " + getMainChannel().getName() + " zu wechseln.")
                    .then(prefix).then(nameColor + player.getName())
                    .suggest("/tell " + player.getName() + " ")
                    .formattedTooltip(HeroUtil.getHeroTooltip(player, null, true))
                    .then(suffix).then(": ").then(channelColor);

            result = Chat.replaceMatchingAutoCompleteItems(player, message, result);
            getMainChannel().sendMessage(result);
        }*/

        message = worldPrefix + ChatColor.RESET + channelPrefix + ChatColor.RESET + prefix + ChatColor.RESET + nameColor +
                player.getName() + ChatColor.RESET + suffix + ChatColor.RESET + ": " + channelColor + message;

        RaidCraft.LOGGER.info(ChatColor.stripColor(message));

        if (!cached) {
            getMainChannel().sendMessage(message);
        }

        BungeeManager bungeeManager = RaidCraft.getComponent(RCMultiWorldPlugin.class).getBungeeManager();
        bungeeManager.sendMessage(player, new ChannelChatMessage(getMainChannel().getName(), message));
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

    public void addAutoCompleteItem(CustomItemStack itemStack) {

        if (autocompleteItems.contains(itemStack)) {
            autocompleteItems.remove(itemStack);
        }
        autocompleteItems.add(itemStack);
    }

    public List<CustomItemStack> getAutocompleteItems() {

        return autocompleteItems;
    }
}
