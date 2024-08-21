package cn.ChengZhiYa.MHDFBotBindQQBukkitHook.command;

import cn.ChengZhiYa.MHDFBotBindQQBukkitHook.main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFBotBindQQBukkitHook.util.DatabaseUtil.ifPlayerDisableGroupHook;
import static cn.ChengZhiYa.MHDFBotBindQQBukkitHook.util.DatabaseUtil.setGroupHook;
import static cn.ChengZhiYa.MHDFBotBindQQBukkitHook.util.message.MessageUtil.colorMessage;

public final class GroupHook implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        String message = main.instance.getConfig().getString("Messages.GroupHook");
        if (ifPlayerDisableGroupHook(sender.getName())) {
            setGroupHook(sender.getName(),true);
            sender.sendMessage(colorMessage(Objects.requireNonNull(message).replaceAll("\\{status}", Objects.requireNonNull(main.instance.getConfig().getString("Messages.Enable")))));
        }else {
            setGroupHook(sender.getName(),false);
            sender.sendMessage(colorMessage(Objects.requireNonNull(message).replaceAll("\\{status}", Objects.requireNonNull(main.instance.getConfig().getString("Messages.Disable")))));
        }
        return false;
    }
}
