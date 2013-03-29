package de.raidcraft.rcchat;

import de.raidcraft.api.BasePlugin;
import de.raidcraft.api.config.ConfigurationBase;
import de.raidcraft.rcchat.channel.ChannelManager;
import de.raidcraft.rcchat.commands.ChatPluginCommands;
import de.raidcraft.rcchat.listener.ChatListener;
import de.raidcraft.rcchat.listener.PlayerListener;
import de.raidcraft.rcchat.tables.ChannelsTable;
import de.raidcraft.rcchat.tables.PlayersTable;

/**
 * @author Philip
 */
public class RCChatPlugin extends BasePlugin {

    public LocalConfiguration config;

    @Override
    public void enable() {

        config = configure(new LocalConfiguration(this));

        registerEvents(new ChatListener());
        registerEvents(new PlayerListener());
        registerCommands(ChatPluginCommands.class);

        registerTable(ChannelsTable.class, new ChannelsTable());
        registerTable(PlayersTable.class, new PlayersTable());

        reload();
    }

    @Override
    public void disable() {
    }

    @Override
    public void reload() {

        config.reload();
        ChannelManager.INST.reload();
    }

    public class LocalConfiguration extends ConfigurationBase<RCChatPlugin> {

        public LocalConfiguration(RCChatPlugin plugin) {

            super(plugin, "config.yml");
        }
    }
}
