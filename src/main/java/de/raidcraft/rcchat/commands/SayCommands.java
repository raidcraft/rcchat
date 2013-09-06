package de.raidcraft.rcchat.commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import de.raidcraft.rcchat.RCChatPlugin;
import de.raidcraft.util.SignUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        if(sender instanceof BlockCommandSender) {

            if(context.argsLength() < 2) return;
            CommandBlock commandBlock = (CommandBlock)sender;
            String senderName = SignUtil.parseColor(commandBlock.getName());
            Player player = Bukkit.getPlayer(context.getString(0));
            if(player == null) return;
            player.sendMessage(senderName + " " + SignUtil.parseColor(context.getString(1)));
            return;
        }

        Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "**SERVER** " + context.getString(0));
    }
}
