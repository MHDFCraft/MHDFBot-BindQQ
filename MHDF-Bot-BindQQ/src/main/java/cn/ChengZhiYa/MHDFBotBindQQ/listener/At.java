package cn.ChengZhiYa.MHDFBotBindQQ.listener;

import cn.ChengZhiYa.MHDFBot.api.interfaces.EventHandler;
import cn.ChengZhiYa.MHDFBot.api.manager.Listener;
import cn.ChengZhiYa.MHDFBot.event.message.GroupMessageEvent;
import cn.ChengZhiYa.MHDFBot.server.WebSocket;
import cn.ChengZhiYa.MHDFBotBindQQ.main;
import com.alibaba.fastjson2.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class At implements Listener {
    @EventHandler
    public void onGroupMessage(GroupMessageEvent event) {
        if (main.instance.getConfig().getStringList("AllowUseGroup").contains(String.valueOf(event.getGroupId()))) {
            Pattern pattern = Pattern.compile("\\[CQ:at,qq=(.*?)]");
            Matcher matcher = pattern.matcher(event.getMessage());

            while (matcher.find()) {
                if (!matcher.group(1).equals("all")) {
                    Long qq = Long.parseLong(matcher.group(1));

                    JSONObject data = new JSONObject();
                    data.put("qq", qq);

                    WebSocket.send("at", data);
                } else {
                    WebSocket.send("atAll", new JSONObject());
                }
            }
        }
    }
}
