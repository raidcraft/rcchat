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
import de.raidcraft.rcchat.tables.TMute;
import de.raidcraft.rcmultiworld.BungeeManager;
import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;
import de.raidcraft.util.SignUtil;
import de.raidcraft.util.UUIDUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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
    private List<UUID> mutedPlayers = null;

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

            suffix = "[" + ChatColor.YELLOW + RaidCraft.getHeroProvider().getLevel(player.getUniqueId()) + ChatColor.RESET + "]";
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

        if (player.hasPermission("rcchat.colorize")) {
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
            getMainChannel().sendMessage(getName(), message);
        }

        BungeeManager bungeeManager = RaidCraft.getComponent(RCMultiWorldPlugin.class).getBungeeManager();
        bungeeManager.sendMessage(player, new ChannelChatMessage(getMainChannel().getName(), getName(), message));
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


    // MUTES

    private void loadMutes() {

        RCChatPlugin plugin = RaidCraft.getComponent(RCChatPlugin.class);

        if(mutedPlayers == null) {
            mutedPlayers = new ArrayList<>();
        }
        mutedPlayers.clear();

        List<TMute> tMutes = plugin.getDatabase().find(TMute.class).where()
                .eq("player_id", getPlayer().getUniqueId()).findList();
        if(tMutes == null) {
            return;
        }

        for(TMute tMute : tMutes) {
            mutedPlayers.add(tMute.getMutedPlayer());
        }
    }

    public boolean mute(String player) {

        if(mutedPlayers == null) loadMutes();

        UUID uuid = UUIDUtil.convertPlayer(player);

        if(uuid == null) {
            return false;
        }

        // check if already muted
        if(mutedPlayers.contains(uuid)) {
            return true;
        }

        // add to cache
        mutedPlayers.add(uuid);

        // add to database
        TMute tMute = new TMute();
        tMute.setPlayerId(getPlayer().getUniqueId());
        tMute.setCreated(new Date(System.currentTimeMillis()));
        tMute.setMutedPlayer(uuid);
        RaidCraft.getComponent(RCChatPlugin.class).getDatabase().save(tMute);

        return true;
    }

    public boolean unmute(String player) {

        return unmute(UUIDUtil.convertPlayer(player));
    }

    public boolean unmute(UUID uuid) {

        RCChatPlugin plugin = RaidCraft.getComponent(RCChatPlugin.class);

        if(mutedPlayers == null) loadMutes();

        if(uuid == null) {
            return false;
        }

        // remove from cache
        mutedPlayers.remove(uuid);

        // remove from database
        TMute tMute = plugin.getDatabase().find(TMute.class).where()
                .eq("player_id", getPlayer().getUniqueId()).eq("muted_player", uuid).findUnique();
        if(tMute == null) {
            return true;
        }

        plugin.getDatabase().delete(tMute);

        return true;
    }

    public void unmuteAll() {

        if(mutedPlayers == null) loadMutes();

        List<UUID> mutedCopy = new ArrayList<>(mutedPlayers);
        for(UUID uuid : mutedCopy) {
            unmute(uuid);
        }
    }

    public boolean isMuted(String sender) {

        if(mutedPlayers == null) loadMutes();

        if(mutedPlayers.contains(UUIDUtil.convertPlayer(sender))) {
            return true;
        }
        return false;
    }

    public List<String> getMutedNames() {

        if(mutedPlayers == null) loadMutes();

        List<String> mutedNames = new ArrayList<>();

        for(UUID uuid : mutedPlayers) {
            mutedNames.add(UUIDUtil.getNameFromUUID(uuid));
        }

        return mutedNames;
    }
}
