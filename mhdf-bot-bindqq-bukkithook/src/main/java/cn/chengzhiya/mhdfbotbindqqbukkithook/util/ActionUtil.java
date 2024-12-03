package cn.chengzhiya.mhdfbotbindqqbukkithook.util;

import cn.chengzhiya.mhdfbotbindqqbukkithook.Main;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

import static cn.chengzhiya.mhdfbotbindqqbukkithook.util.message.LogUtil.colorLog;
import static cn.chengzhiya.mhdfbotbindqqbukkithook.util.message.MessageUtil.colorMessage;

public final class ActionUtil {
    /**
     * 执行操作列表
     *
     * @param player     玩家实例
     * @param actionList 操作列表实例
     */
    public static void runAction(Player player, List<String> actionList) {
        for (String actions : actionList) {
            String[] action = actions.split("\\|");
            switch (action[0]) {
                case "[player]":
                    Bukkit.getScheduler().runTask(Main.instance, () ->
                            player.chat("/" + PlaceholderAPI.setPlaceholders(player, action[1]))
                    );
                    continue;
                case "[console]":
                    Bukkit.getScheduler().runTask(Main.instance, () ->
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(player, action[1]))
                    );
                    player.closeInventory();
                    continue;
                case "[playsound]":
                    try {
                        player.playSound(player, Sound.valueOf(action[1]), Float.parseFloat(action[2]), Float.parseFloat(action[3]));
                    } catch (Exception e) {
                        colorLog("&c[MHDF-PluginAPI]不存在" + action[1] + "这个音频");
                    }
                    continue;
                case "[playsound_pack]":
                    try {
                        player.playSound(player, action[1], Float.parseFloat(action[2]), Float.parseFloat(action[3]));
                    } catch (Exception e) {
                        colorLog("&c[MHDF-PluginAPI]不存在" + action[1] + "这个音频");
                    }
                    continue;
                case "[message]":
                    player.sendMessage(colorMessage(PlaceholderAPI.setPlaceholders(player, action[1]))
                            .replace(action[0] + "|", "")
                            .replace("|", "\n")
                    );
                    continue;
                case "[broadcast]": {
                    Bukkit.broadcast(colorMessage(PlaceholderAPI.setPlaceholders(player, action[1]))
                            .replace(action[0] + "|", "")
                            .replace("|", "\n"), "mhdfbotbindqq.default");
                    continue;
                }
                case "[title]":
                    player.sendTitle(
                            colorMessage(PlaceholderAPI.setPlaceholders(player, action[1])),
                            colorMessage(PlaceholderAPI.setPlaceholders(player, action[2])),
                            Integer.parseInt(action[3]),
                            Integer.parseInt(action[4]),
                            Integer.parseInt(action[5])
                    );
                    continue;
                case "[actionbar]":
                    player.spigot().sendMessage(
                            ChatMessageType.ACTION_BAR,
                            new TextComponent(colorMessage(PlaceholderAPI.setPlaceholders(player, action[1])))
                    );
                    continue;
            }
            colorLog("&c[MHDF-PluginAPI]不存在" + action[0] + "这个操作");
        }
    }
}
