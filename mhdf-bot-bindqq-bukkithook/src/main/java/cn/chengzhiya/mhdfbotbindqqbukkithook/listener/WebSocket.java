package cn.chengzhiya.mhdfbotbindqqbukkithook.listener;

import cn.chengzhiya.mhdfbotbindqqbukkithook.Main;
import cn.chengzhiya.mhdfbotbindqqbukkithook.entity.PlayerData;
import cn.chengzhiya.mhdfbotbindqqbukkithook.util.ChangePasswordUtil;
import cn.chengzhiya.mhdfbotbindqqbukkithook.util.DatabaseUtil;
import cn.chengzhiya.mhdfbotbukkithook.event.WebSocketEvent;
import com.alibaba.fastjson2.JSONObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Objects;

import static cn.chengzhiya.mhdfbotbindqqbukkithook.util.ActionUtil.runAction;
import static cn.chengzhiya.mhdfbotbindqqbukkithook.util.DatabaseUtil.getQqBindList;
import static cn.chengzhiya.mhdfbotbindqqbukkithook.util.message.MessageUtil.colorMessage;

public final class WebSocket implements Listener {
    @EventHandler
    public void onWebSocket(WebSocketEvent event) {
        String action = event.getData().getString("action");
        JSONObject data = event.getData().getJSONObject("data");
        switch (action) {
            case "bindDone": {
                String playerName = data.getString("playerName");
                Long qq = data.getLong("qq");

                DatabaseUtil.getPlayerDataHashMap().put(playerName, new PlayerData(playerName, qq));
                List<String> qqBindList = getQqBindList(qq);
                qqBindList.add(playerName);
                DatabaseUtil.getQqBindHashMap().put(qq, qqBindList);

                if (Bukkit.getPlayer(playerName) != null) {
                    runAction(Bukkit.getPlayer(playerName), Main.instance.getConfig().getStringList("actions.bindDone"));
                }
                break;
            }
            case "unBindDone": {
                String playerName = data.getString("playerName");
                Long qq = data.getLong("qq");

                DatabaseUtil.getPlayerDataHashMap().remove(playerName);

                List<String> qqBindList = getQqBindList(qq);
                qqBindList.remove(playerName);
                DatabaseUtil.getQqBindHashMap().put(qq, qqBindList);
                break;
            }
            case "at": {
                Long qq = data.getLong("qq");

                for (String playerName : getQqBindList(qq)) {
                    if (Bukkit.getPlayer(playerName) != null) {
                        runAction(Bukkit.getPlayer(playerName), Main.instance.getConfig().getStringList("actions.atQQ"));
                    }
                }
                break;
            }
            case "atAll": {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    runAction(player, Main.instance.getConfig().getStringList("actions.atQQ"));
                }
                break;
            }
            case "chat": {
                Long groupId = data.getLong("group_id");
                Long senderId = data.getLong("sender_id");
                String senderName = data.getString("sender_name");
                String message = data.getString("message");

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage(colorMessage(Objects.requireNonNull(Main.instance.getConfig().getString("chatHookMessage")))
                            .replace("{name}", senderName)
                            .replace("{qq}", String.valueOf(senderId))
                            .replace("{group}", String.valueOf(groupId))
                            .replace("{message}", message)
                    );
                }
                break;
            }
            case "mannounce": {
                String text = data.getString("text");

                Bukkit.broadcast(
                        colorMessage(Objects.requireNonNull(Main.instance.getConfig().getString("mannounceHookMessage")))
                                .replace("{text}", text)
                        , "mhdfbotbindqq.default"
                );
                break;
            }
            case "changePassword": {
                String playerName = data.getString("player_name");
                String password = data.getString("password");

                ChangePasswordUtil.changePassword(playerName, password);
                break;
            }
        }
    }
}
