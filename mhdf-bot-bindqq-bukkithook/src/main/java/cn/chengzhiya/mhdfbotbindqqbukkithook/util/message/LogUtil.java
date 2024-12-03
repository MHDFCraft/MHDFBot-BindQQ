package cn.chengzhiya.mhdfbotbindqqbukkithook.util.message;

import org.bukkit.Bukkit;

import static cn.chengzhiya.mhdfbotbindqqbukkithook.util.message.MessageUtil.colorMessage;

public final class LogUtil {
    /**
     * 日志消息
     *
     * @param message 内容
     */
    public static void colorLog(String message) {
        Bukkit.getConsoleSender().sendMessage(colorMessage(message));
    }
}
