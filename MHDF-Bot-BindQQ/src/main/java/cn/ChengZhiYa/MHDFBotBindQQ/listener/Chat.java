package cn.ChengZhiYa.MHDFBotBindQQ.listener;

import cn.ChengZhiYa.MHDFBot.api.interfaces.EventHandler;
import cn.ChengZhiYa.MHDFBot.api.manager.Listener;
import cn.ChengZhiYa.MHDFBot.event.message.GroupMessageEvent;
import cn.ChengZhiYa.MHDFBot.server.WebSocket;
import cn.ChengZhiYa.MHDFBotBindQQ.main;
import com.alibaba.fastjson2.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Chat implements Listener {
    @EventHandler
    public void onGroupMessage(GroupMessageEvent event) {
        if (main.instance.getConfig().getStringList("AllowUseGroup").contains(String.valueOf(event.getGroupId()))) {
            if (main.instance.getConfig().getBoolean("GroupChatHook")) {
                Pattern pattern = Pattern.compile("\\[.*?]");
                Matcher matcher = pattern.matcher(event.getMessage());
                String message = matcher.replaceAll("");
                if (!message.isEmpty()) {
                    JSONObject data = new JSONObject();
                    data.put("group_id", event.getGroupId());
                    data.put("sender_id", event.getSender().getUserId());
                    data.put("sender_name",
                            event.getSender().getCard() != null && !event.getSender().getCard().isEmpty() ? event.getSender().getCard() :
                                    event.getSender().getNickName());
                    data.put("message", message);
                    WebSocket.send("chat", data);
                }
            }
        }
    }
}
