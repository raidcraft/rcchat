package de.raidcraft.rcchat.commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import de.raidcraft.RaidCraft;
import de.raidcraft.rcchat.RCChatPlugin;
import de.raidcraft.rcchat.channel.Channel;
import de.raidcraft.rcchat.channel.ChannelManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * Author: Philip
 * Date: 16.10.12 - 19:46
 * Description:
 */
public class ChatPluginCommands {

    public ChatPluginCommands(RCChatPlugin module) {

    }

    @Command(
            aliases = {"ch", "chat", "rcchat"},
            desc = "Main command"
            )
    public void rcchat(CommandContext context, CommandSender sender) throws CommandException {

        // list channels
        if(context.argsLength() == 0) {
            //TODO list channels
        }

        String arg = context.getString(0);

        /* check for control parameters */

        // reload plugin
        if(arg.equalsIgnoreCase("reload")) {
            RaidCraft.getComponent(RCChatPlugin.class).reload();
            sender.sendMessage(ChatColor.GREEN + "RCChat wurde neugeladen!");
            return;
        }

        if(sender instanceof ConsoleCommandSender) {
            throw new CommandException("For non admin commands (e.g. channel changes) a player context is required!");
        }

        Player player = (Player)sender;

        // look if first argument is channel
        Channel channel = ChannelManager.INST.getChannel(context.getString(0));
        if(channel == null) {
            throw new CommandException("Unbekannter Channel '" + context.getString(0) + "' oder falscher Parameter!");
        }

        if(!channel.isCorrectWorld(player)) {
            throw new CommandException("Der gewählte Channel ist auf dieser Welt nicht verfügbar!");
        }

        if(channel.hasPermission() && !sender.hasPermission(channel.getPermission())) {
            throw new CommandException("Du hast keine Rechte um diesen Channel zu betreten!");
        }

        channel.join(player);
        sender.sendMessage(ChatColor.GREEN + "Du schreibst nun im Channel '" + ChatColor.YELLOW + channel.getName() + ChatColor.GREEN + "'");
    }
}
