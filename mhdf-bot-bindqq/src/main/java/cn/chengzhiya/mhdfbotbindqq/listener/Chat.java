package cn.chengzhiya.mhdfbotbindqq.listener;

import cn.chengzhiya.mhdfbot.api.MHDFBot;
import cn.chengzhiya.mhdfbot.api.event.message.GroupMessageEvent;
import cn.chengzhiya.mhdfbot.api.listener.EventHandler;
import cn.chengzhiya.mhdfbot.api.listener.Listener;
import cn.chengzhiya.mhdfbotbindqq.Main;
import com.alibaba.fastjson2.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Chat implements Listener {
    @EventHandler
    public void onGroupMessage(GroupMessageEvent event) {
        if (!Main.instance.getConfig().getStringList("allowUseGroup").contains(String.valueOf(event.getGroupId()))) {
            return;
        }
        if (!Main.instance.getConfig().getBoolean("groupChatHook")) {
            return;
        }

        Pattern pattern = Pattern.compile("\\[.*?]");
        Matcher matcher = pattern.matcher(event.getMessage());
        String message = matcher.toString();

        if (message.isEmpty()) {
            return;
        }

        JSONObject data = new JSONObject();
        data.put("group_id", event.getGroupId());
        data.put("sender_id", event.getSender().getUserId());
        data.put("sender_name",
                event.getSender().getCard() != null && !event.getSender().getCard().isEmpty() ? event.getSender().getCard() :
                        event.getSender().getNickName());
        data.put("message", message);

        MHDFBot.getMinecraftWebSocketServer().send("chat", data);
    }
}
