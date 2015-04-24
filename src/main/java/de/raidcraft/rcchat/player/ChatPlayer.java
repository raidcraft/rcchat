package de.raidcraft.rcchat.player;

import de.raidcraft.RaidCraft;
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
import de.raidcraft.skills.SkillsPlugin;
import de.raidcraft.skills.api.hero.Hero;
import de.raidcraft.skills.api.profession.Profession;
import de.raidcraft.util.SignUtil;
import mkremins.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Philip
 */
public class ChatPlayer {

    private static final Pattern ITEM_COMPLETE_PATTERN = Pattern.compile("(.*)\\?\"([a-zA-ZüöäÜÖÄß\\s]+)?\"");

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
            try {
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
            } catch (Throwable e) {
                //                    e.printStackTrace();
            }
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

        message = worldPrefix + ChatColor.RESET + channelPrefix + ChatColor.RESET  + prefix + ChatColor.RESET + nameColor +
                player.getName() + ChatColor.RESET + suffix + ChatColor.RESET + ": " + channelColor + message;

        FancyMessage result = matchAndReplaceItem(message);

        RaidCraft.LOGGER.info(ChatColor.stripColor(message));
        getMainChannel().sendMessage(result);

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

        autocompleteItems.add(itemStack);
    }

    public List<CustomItemStack> getAutocompleteItems() {

        return autocompleteItems;
    }

    private FancyMessage matchAndReplaceItem(String message) {

        Matcher matcher = ITEM_COMPLETE_PATTERN.matcher(message);
        FancyMessage msg = new FancyMessage("");
        while (matcher.find()) {
            msg.text(matcher.group(1));
            if (matcher.group(2) != null) {
                String itemName = matcher.group(2);
                Optional<CustomItemStack> first = autocompleteItems.stream()
                        .filter(i -> i.getItem().getName().equals(itemName))
                        .findFirst();
                if (first.isPresent()) {
                    CustomItemStack item = first.get();
                    msg.then()
                            .text("[" + item.getItem().getName() + "]")
                            .color(item.getItem().getQuality().getColor())
                            .itemTooltip(item);
                    if (!matcher.hitEnd()) {
                        msg.then();
                    } else {
                        return msg;
                    }
                }
            } else {
                return msg;
            }
        }
        return new FancyMessage(message);
    }
}
