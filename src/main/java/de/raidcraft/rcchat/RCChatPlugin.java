package de.raidcraft.rcchat;

import de.raidcraft.api.BasePlugin;
import de.raidcraft.api.config.ConfigurationBase;
import de.raidcraft.rcchat.listener.ChatListener;

/**
 * @author Philip
 */
public class RCChatPlugin extends BasePlugin {

    public LocalConfiguration config;

    @Override
    public void enable() {

        config = configure(new LocalConfiguration(this));

        registerEvents(new ChatListener());
    }

    @Override
    public void disable() {
    }

    @Override
    public void reload() {

        config.reload();
    }

    public class LocalConfiguration extends ConfigurationBase<RCChatPlugin> {

        public LocalConfiguration(RCChatPlugin plugin) {

            super(plugin, "config.yml");
        }
    }
}
