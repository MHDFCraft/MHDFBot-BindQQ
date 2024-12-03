package cn.chengzhiya.mhdfbotbindqqbungeehook.util.message;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MessageUtil {
    /**
     * RGB彩色符号(例如: #ffffff)处理
     *
     * @param message 文本
     */
    public static String translateHexCodes(String message) {
        Matcher matcher = Pattern.compile("#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})").matcher(message);
        StringBuilder sb = new StringBuilder(message.length());
        while (matcher.find()) {
            String hex = matcher.group(1);
            ChatColor color = ChatColor.of("#" + hex);
            matcher.appendReplacement(sb, color.toString());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 彩色符号处理
     *
     * @param message 文本
     */
    public static String colorMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', translateHexCodes(message));
    }
}