package cn.ChengZhiYa.MHDFBotBindQQBukkitHook.listener;

import cn.ChengZhiYa.MHDFBotBukkitHook.client.WebSocket;
import com.alibaba.fastjson2.JSONObject;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public final class PlayerChat implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        JSONObject data = new JSONObject();
        data.put("sender_name", player.getName());
        data.put("message", message);
        WebSocket.send("chat", data);
    }
}
