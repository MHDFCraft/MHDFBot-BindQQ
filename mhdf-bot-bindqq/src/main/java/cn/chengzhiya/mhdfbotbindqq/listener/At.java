package cn.chengzhiya.mhdfbotbindqq.listener;

import cn.chengzhiya.mhdfbot.api.MHDFBot;
import cn.chengzhiya.mhdfbot.api.event.message.GroupMessageEvent;
import cn.chengzhiya.mhdfbot.api.listener.EventHandler;
import cn.chengzhiya.mhdfbot.api.listener.Listener;
import cn.chengzhiya.mhdfbotbindqq.Main;
import com.alibaba.fastjson2.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class At implements Listener {
    @EventHandler
    public void onGroupMessage(GroupMessageEvent event) {
        if (Main.instance.getConfig().getStringList("allowUseGroup").contains(String.valueOf(event.getGroupId()))) {
            return;
        }

        Pattern pattern = Pattern.compile("\\[CQ:at,qq=(\\d+)(?:,name=[^]]+)?]");
        Matcher matcher = pattern.matcher(event.getMessage());

        while (matcher.find()) {
            if (matcher.group(1).equals("all")) {
                MHDFBot.getMinecraftWebSocketServer().send("atAll", new JSONObject());
                continue;
            }

            Long qq = Long.parseLong(matcher.group(1));

            JSONObject data = new JSONObject();
            data.put("qq", qq);

            MHDFBot.getMinecraftWebSocketServer().send("at", data);
        }
    }
}
