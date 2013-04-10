package de.raidcraft.rcchat.commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import de.raidcraft.RaidCraft;
import de.raidcraft.rcchat.RCChatPlugin;
import de.raidcraft.rcchat.channel.Channel;
import de.raidcraft.rcchat.channel.ChannelManager;
import de.raidcraft.rcchat.player.ChatPlayer;
import de.raidcraft.rcchat.player.PlayerManager;
import org.bukkit.Bukkit;
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
        ChatPlayer chatPlayer = PlayerManager.INST.getPlayer(player);

        if(context.getString(0).equalsIgnoreCase("leave")) {
            Channel channel = null;
            if(context.argsLength() > 1) {
                channel = ChannelManager.INST.getChannel(context.getString(1));
            }
            if(channel == null) {
                channel = chatPlayer.getMainChannel();
            }
            channel.leave(player);
            player.sendMessage(ChatColor.GREEN + "Du hast den Channel " + channel.getName() + " verlassen!");
            return;
        }

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

        chatPlayer.leavePrivateChat();
        Channel oldChannel = chatPlayer.getMainChannel();
        channel.join(player);
        if(context.argsLength() > 1) {
            chatPlayer.sendMessage(context.getJoinedStrings(1));
            oldChannel.join(player);
        }
        else {
            sender.sendMessage(ChatColor.GREEN + "Du schreibst nun im Channel '" + ChatColor.YELLOW + channel.getName() + ChatColor.GREEN + "'");
        }

    }

    @Command(
            aliases = {"tell", "@"},
            desc = "Private chat command"
    )
    public void tell(CommandContext context, CommandSender sender) throws CommandException {

        ChatPlayer chatPlayer = PlayerManager.INST.getPlayer((Player)sender);

        if(context.argsLength() == 0) {
            chatPlayer.leavePrivateChat();
            sender.sendMessage(ChatColor.GREEN + "Du schreibst nun im Channel '" +
                    ChatColor.YELLOW + chatPlayer.getMainChannel().getName() + ChatColor.GREEN + "'");
            return;
        }

        Player recipient = Bukkit.getPlayer(context.getString(0));
        if(recipient == null) {
            throw new CommandException("Spieler '" + context.getString(0) + "' nicht gefunden!");
        }

        chatPlayer.enterPrivateChat(recipient);

        if(context.argsLength() < 2) {
            chatPlayer.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "Du chattest nun mit '" + recipient.getName() + "'");
        }

        if(context.argsLength() > 1 && chatPlayer.hasPrivateChat()) {
            chatPlayer.sendMessageToPartner(context.getJoinedStrings(1));
            chatPlayer.leavePrivateChat();
        }
    }
}
