package cn.chengzhiya.mhdfbotbindqqbukkithook.command;

import cn.chengzhiya.mhdfbotbindqqbukkithook.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static cn.chengzhiya.mhdfbotbindqqbukkithook.util.message.MessageUtil.colorMessage;

public final class Reload implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        Main.instance.reloadConfig();
        sender.sendMessage(colorMessage(Main.instance.getConfig().getString("messages.reloadDone")));
        return false;
    }
}
