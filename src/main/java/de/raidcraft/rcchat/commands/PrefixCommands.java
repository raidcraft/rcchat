package de.raidcraft.rcchat.commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.NestedCommand;
import de.raidcraft.rcchat.RCChatPlugin;
import de.raidcraft.rcchat.prefix.PlayerPrefix;
import de.raidcraft.rcchat.prefix.PrefixManager;
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

        player.sendMessage(ChatColor.GREEN + "Wählbare Präfixe:");
        int i = 0;
        for(PlayerPrefix prefix : PrefixManager.INST.getPossiblePrefixes(player)) {
            i++;
            player.sendMessage(ChatColor.YELLOW + String.valueOf(i) + ": " + ChatColor.WHITE + prefix.getParsedPrefix());
        }
        player.sendMessage(ChatColor.GREEN + "Wähle dein Wunschpräfix mit /prefix change <ID>");
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
                    player.sendMessage(ChatColor.GREEN + "Präfix erfolgreich geändert! Neues Präfix: " + ChatColor.WHITE + prefix.getParsedPrefix());
                    return;
                }
            }
            throw new CommandException("Falsche Präfix-ID gewählt! (/prefix zeigt dir alle verfügbaren an)");
        }
    }
}
