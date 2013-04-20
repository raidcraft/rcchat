package de.raidcraft.rcchat.commands;

import com.sk89q.minecraft.util.commands.*;
import de.raidcraft.rcchat.RCChatPlugin;
import de.raidcraft.rcchat.prefix.PlayerPrefix;
import de.raidcraft.rcchat.prefix.PrefixManager;
import de.raidcraft.util.SignUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * Author: Philip
 * Date: 16.10.12 - 19:46
 * Description:
 */
public class PrefixCommands {

    public PrefixCommands(RCChatPlugin module) {

    }

    @Command(
            aliases = {"prefix", "pre"},
            desc = "Main command"
    )
    @NestedCommand(
            value = NestedPrefixCommands.class,
            executeBody = true
    )
    public void prefix(CommandContext context, CommandSender sender) throws CommandException {

        if(sender instanceof ConsoleCommandSender) {
            throw new CommandException("Player context required!");
        }
        Player player = (Player)sender;

        player.sendMessage(ChatColor.GREEN + "Wählbare Prefixe:");
        int i = 0;
        for(PlayerPrefix prefix : PrefixManager.INST.getPossiblePrefixes(player)) {
            i++;
            player.sendMessage(ChatColor.YELLOW + String.valueOf(i) + ": " + SignUtil.parseColor(prefix.getPrefix()));
        }
        player.sendMessage(ChatColor.GREEN + "Wähle dein Wunschprefix mit /prefix change <nummer>");
    }

    public static class NestedPrefixCommands {

        private final RCChatPlugin module;

        public NestedPrefixCommands(RCChatPlugin module) {

            this.module = module;
        }

        @Command(
                aliases = {"change", "c", "choose"},
                desc = "Change prefix",
                min = 1
        )
        public void change(CommandContext context, CommandSender sender) throws CommandException {

            if(sender instanceof ConsoleCommandSender) {
                throw new CommandException("Player context required!");
            }
            Player player = (Player)sender;
            int i = 0;
            for(PlayerPrefix prefix : PrefixManager.INST.getPossiblePrefixes(player)) {
                i++;
                if(i == context.getInteger(0)) {
                    PrefixManager.INST.setPlayerPrefix(player, prefix);
                    player.sendMessage(ChatColor.GREEN + "Prefix erfolgreich geändert! Neues Prefix: " + SignUtil.parseColor(prefix.getPrefix()));
                    return;
                }
            }
            throw new CommandException("Falsche Prefix-ID gewählt! (/prefix zeigt dir alle verfügbaren an)");
        }

        @Command(
                aliases = {"set"},
                desc = "Set prefix",
                min = 2
        )
        @CommandPermissions("rcchat.prefix.set")
        public void set(CommandContext context, CommandSender sender) throws CommandException {

        }
    }
}
