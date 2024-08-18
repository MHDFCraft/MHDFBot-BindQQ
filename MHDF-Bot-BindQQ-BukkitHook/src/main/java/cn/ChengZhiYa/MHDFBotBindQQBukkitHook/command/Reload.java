package cn.ChengZhiYa.MHDFBotBindQQBukkitHook.command;

import cn.ChengZhiYa.MHDFBotBindQQBukkitHook.main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import static cn.ChengZhiYa.MHDFBotBindQQBukkitHook.util.message.MessageUtil.colorMessage;

public final class Reload implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        main.instance.reloadConfig();
        sender.sendMessage(colorMessage("Messages.ReloadDone"));
        return false;
    }
}
