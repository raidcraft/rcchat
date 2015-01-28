package de.raidcraft.rcchat.commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import de.raidcraft.rcchat.RCChatPlugin;
import de.raidcraft.util.SignUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;

/**
 * Author: Philip
 * Date: 16.10.12 - 19:46
 * Description:
 */
public class SayCommands {

    public SayCommands(RCChatPlugin module) {

    }

    @Command(
            aliases = {"say"},
            desc = "Say command",
            min = 1
    )
    @CommandPermissions("rcchat.say")
    public void say(CommandContext context, CommandSender sender) throws CommandException {

        if (sender instanceof BlockCommandSender) {

            BlockCommandSender commandBlock = (BlockCommandSender) sender;
            String senderName = commandBlock.getName();
            if (senderName.equalsIgnoreCase("@")) {
                senderName = ChatColor.LIGHT_PURPLE + "**SERVER**";
            } else {
                senderName = SignUtil.parseColor(senderName);
            }

            Bukkit.broadcastMessage(senderName + " " + SignUtil.parseColor(context.getJoinedStrings(0)));
            return;
        }

        Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "**SERVER** " + context.getString(0));
    }
}
