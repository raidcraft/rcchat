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
import de.raidcraft.rcchat.listener.ChatListener;
import de.raidcraft.rcchat.listener.PlayerListener;
import de.raidcraft.rcchat.player.ChatPlayerManager;
import de.raidcraft.rcchat.prefix.PrefixManager;
import de.raidcraft.rcchat.tables.*;
import de.raidcraft.rcmultiworld.BungeeManager;
import de.raidcraft.rcmultiworld.RCMultiWorldPlugin;

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
        registerCommands(ChatPluginCommands.class);
        registerCommands(PrefixCommands.class);

        registerTable(ChannelsTable.class, new ChannelsTable());
        registerTable(PlayersChannelTable.class, new PlayersChannelTable());
        registerTable(PlayerPrefixTable.class, new PlayerPrefixTable());
        registerTable(WorldPrefixTable.class, new WorldPrefixTable());
        registerTable(PlayersPrefixTable.class, new PlayersPrefixTable());
        registerTable(ChannelWorldsTable.class, new ChannelWorldsTable());

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

    public class LocalConfiguration extends ConfigurationBase<RCChatPlugin> {

        public LocalConfiguration(RCChatPlugin plugin) {

            super(plugin, "config.yml");
        }

        @Setting("use-colored-names") public boolean coloredNames = false;
    }
}
