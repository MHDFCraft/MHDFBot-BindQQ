package cn.ChengZhiYa.MHDFBotBindQQBukkitHook.util.message;

import org.bukkit.Bukkit;

import static cn.ChengZhiYa.MHDFBotBindQQBukkitHook.util.message.MessageUtil.colorMessage;

public final class LogUtil {
    public static void colorLog(String message) {
        Bukkit.getConsoleSender().sendMessage(colorMessage(message));
    }
}
