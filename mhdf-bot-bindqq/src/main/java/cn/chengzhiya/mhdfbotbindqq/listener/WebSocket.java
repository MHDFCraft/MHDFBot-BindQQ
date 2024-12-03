package cn.chengzhiya.mhdfbotbindqq.listener;

import cn.chengzhiya.mhdfbot.api.MHDFBot;
import cn.chengzhiya.mhdfbot.api.enums.minecraft.ServerType;
import cn.chengzhiya.mhdfbot.api.event.minecraft.MinecraftWebsocketMessageEvent;
import cn.chengzhiya.mhdfbot.api.listener.EventHandler;
import cn.chengzhiya.mhdfbot.api.listener.Listener;
import cn.chengzhiya.mhdfbotbindqq.Main;
import com.alibaba.fastjson2.JSONObject;

public final class WebSocket implements Listener {
    @EventHandler
    public void onWebSocket(MinecraftWebsocketMessageEvent event) {
        if (event.getServerType() != ServerType.BUKKIT) {
            return;
        }

        String action = event.getAction();
        JSONObject data = event.getData();

        // 操作类型不是聊天
        if (!action.equals("chat")) {
            return;
        }

        // 功能未开启
        if (!Main.instance.getConfig().getBoolean("gameChatHook")) {
            return;
        }

        String playerName = data.getString("sender_name");
        String message = data.getString("message");

        for (String groupId : Main.instance.getConfig().getStringList("allowUseGroup")) {
            MHDFBot.sendGroupMsg(Long.valueOf(groupId), Main.instance.getConfig().getString("chatHookMessage")
                    .replace("{name}", playerName)
                    .replace("{message}", message)
            );
        }
    }
}
