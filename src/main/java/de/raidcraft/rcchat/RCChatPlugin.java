package de.raidcraft.rcchat;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.BasePlugin;
import de.raidcraft.api.config.ConfigurationBase;
import de.raidcraft.api.config.Setting;
import de.raidcraft.rcchat.bungeecord.messages.ChannelChatMessage;
import de.raidcraft.rcchat.bungeecord.messages.PrivateChatMessage;
import de.raidcraft.rcchat.channel.ChannelManager;
import de.raidcraft.rcchat.commands.ChatPluginCommands;
import de.raidcraft.rcchat.commands.PrefixCommands;
import de.raidcraft.rcchat.commands.SayCommands;
import de.raidcraft.rcchat.listener.ApiListener;
import de.raidcraft.rcchat.listener.ChatListener;
import de.raidcraft.rcchat.listener.PlayerListener;
import de.raidcraft.rcchat.player.ChatPlayerManager;
import de.raidcraft.rcchat.prefix.PrefixManager;
import de.raidcraft.rcchat.tables.*;
import de.raidcraft.rcmultiworld.BungeeManager;
import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Philip
 */
public class RCChatPlugin extends BasePlugin {

    public final static String BUNGEECORD_CHANNEL = "RCChat";
    public LocalConfiguration config;

    @Override
    public void enable() {

        config = configure(new LocalConfiguration(this));

        registerEvents(new ChatListener());
        registerEvents(new PlayerListener());
        registerEvents(new ApiListener());
        registerCommands(ChatPluginCommands.class);
        registerCommands(PrefixCommands.class);
        registerCommands(SayCommands.class);

        BungeeManager bungeeManager = RaidCraft.getComponent(RCMultiWorldPlugin.class).getBungeeManager();
        bungeeManager.registerBungeeMessage(ChannelChatMessage.class);
        bungeeManager.registerBungeeMessage(PrivateChatMessage.class);

        reload();
    }

    @Override
    public void disable() {
    }

    @Override
    public void reload() {

        config.reload();
        ChannelManager.INST.reload();
        PrefixManager.INST.reload();
        ChatPlayerManager.INST.reload();
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> classes = new ArrayList<>();
        classes.add(TMute.class);
        classes.add(TPlayersChannel.class);
        classes.add(TWorldPrefix.class);
        classes.add(TChannel.class);
        classes.add(TChannelWorld.class);
        classes.add(TPlayerPrefix.class);
        classes.add(TPlayersPrefix.class);
        return classes;
    }

    public class LocalConfiguration extends ConfigurationBase<RCChatPlugin> {

        public LocalConfiguration(RCChatPlugin plugin) {

            super(plugin, "config.yml");
        }

        @Setting("use-colored-names")
        public boolean coloredNames = false;
        @Setting("display-guest-prefix")
        public boolean displayGuestPrefix = false;
    }
}
