package de.raidcraft.rcchat;

import de.raidcraft.api.BasePlugin;
import de.raidcraft.api.config.ConfigurationBase;
import de.raidcraft.rcchat.channel.ChannelManager;
import de.raidcraft.rcchat.commands.ChatPluginCommands;
import de.raidcraft.rcchat.listener.ChatListener;
import de.raidcraft.rcchat.listener.PlayerListener;
import de.raidcraft.rcchat.prefix.PrefixManager;
import de.raidcraft.rcchat.tables.ChannelsTable;
import de.raidcraft.rcchat.tables.PlayersChannelTable;
import de.raidcraft.rcchat.tables.PlayersPrefixTable;
import de.raidcraft.rcchat.tables.PrefixTable;

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
        registerTable(PlayersChannelTable.class, new PlayersChannelTable());
        registerTable(PrefixTable.class, new PrefixTable());
        registerTable(PlayersPrefixTable.class, new PlayersPrefixTable());

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
    }

    public class LocalConfiguration extends ConfigurationBase<RCChatPlugin> {

        public LocalConfiguration(RCChatPlugin plugin) {

            super(plugin, "config.yml");
        }
    }
}
