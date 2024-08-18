package cn.ChengZhiYa.MHDFBotBindQQBukkitHook.listener;

import cn.ChengZhiYa.MHDFBotBindQQBukkitHook.entity.PlayerData;
import cn.ChengZhiYa.MHDFBotBindQQBukkitHook.main;
import cn.ChengZhiYa.MHDFBotBindQQBukkitHook.util.ChangePasswordUtil;
import cn.ChengZhiYa.MHDFBotBindQQBukkitHook.util.DatabaseUtil;
import cn.ChengZhiYa.MHDFBotBukkitHook.event.WebSocketEvent;
import com.alibaba.fastjson2.JSONObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Objects;

import static cn.ChengZhiYa.MHDFBotBindQQBukkitHook.util.ActionUtil.runAction;
import static cn.ChengZhiYa.MHDFBotBindQQBukkitHook.util.DatabaseUtil.getQqBindList;
import static cn.ChengZhiYa.MHDFBotBukkitHook.util.MessageUtil.colorMessage;

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
                    runAction(Bukkit.getPlayer(playerName), main.instance.getConfig().getStringList("Actions.BindDone"));
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
                        runAction(Bukkit.getPlayer(playerName), main.instance.getConfig().getStringList("Actions.AtQQ"));
                    }
                }
                break;
            }
            case "atAll": {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    runAction(player, main.instance.getConfig().getStringList("Actions.AtQQ"));
                }
                break;
            }
            case "chat": {
                Long groupId = data.getLong("group_id");
                Long senderId = data.getLong("sender_id");
                String senderName = data.getString("sender_name");
                String message = data.getString("message");

                Bukkit.broadcast(
                        colorMessage(Objects.requireNonNull(main.instance.getConfig().getString("ChatHookMessage")))
                                .replaceAll("\\{name}", senderName)
                                .replaceAll("\\{qq}", String.valueOf(senderId))
                                .replaceAll("\\{group}", String.valueOf(groupId))
                                .replaceAll("\\{message}", message)
                        , "MHDFBotBindQQ.default"
                );
                break;
            }
            case "mannounce": {
                String text = data.getString("text");

                Bukkit.broadcast(
                        colorMessage(Objects.requireNonNull(main.instance.getConfig().getString("MannounceHookMessage"))).replaceAll("\\{text}", text)
                        , "MHDFBotBindQQ.default"
                );
                break;
            }
            case "changePassword": {
                String playerName = data.getString("playerName");
                String password = data.getString("password");
                ChangePasswordUtil.changePassword(playerName, password);
                break;
            }
        }
    }
}
