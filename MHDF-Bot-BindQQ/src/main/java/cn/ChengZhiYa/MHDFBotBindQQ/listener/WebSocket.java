package cn.ChengZhiYa.MHDFBotBindQQ.listener;

import cn.ChengZhiYa.MHDFBot.api.MHDFBot;
import cn.ChengZhiYa.MHDFBot.api.enums.minecraft.ServerType;
import cn.ChengZhiYa.MHDFBot.api.interfaces.EventHandler;
import cn.ChengZhiYa.MHDFBot.api.manager.Listener;
import cn.ChengZhiYa.MHDFBot.event.minecraft.WebSocketEvent;
import cn.ChengZhiYa.MHDFBotBindQQ.main;
import com.alibaba.fastjson2.JSONObject;

public final class WebSocket implements Listener {
    @EventHandler
    public void onWebSocket(WebSocketEvent event) {
        if (event.getServerType() == ServerType.BUKKIT) {
            String action = event.getAction();
            JSONObject data = event.getData();
            switch (action) {
                case "chat": {
                    if (main.instance.getConfig().getBoolean("GameChatHook")) {
                        String playerName = data.getString("sender_name");
                        String message = data.getString("message");
                        for (String groupId : main.instance.getConfig().getStringList("AllowUseGroup")) {
                            MHDFBot.sendGroupMessage(Long.valueOf(groupId), main.instance.getConfig().getString("ChatHookMessage")
                                    .replaceAll("\\{name}", playerName)
                                    .replaceAll("\\{message}", message)
                            );
                        }
                    }
                }
            }
        }
    }
}
