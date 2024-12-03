package cn.chengzhiya.mhdfbotbindqq.listener;

import cn.chengzhiya.mhdfbot.api.MHDFBot;
import cn.chengzhiya.mhdfbot.api.event.message.GroupMessageEvent;
import cn.chengzhiya.mhdfbot.api.listener.EventHandler;
import cn.chengzhiya.mhdfbot.api.listener.Listener;
import cn.chengzhiya.mhdfbot.api.util.MessageUtil;
import cn.chengzhiya.mhdfbotbindqq.Main;
import cn.chengzhiya.mhdfbotbindqq.util.Base64Util;
import com.alibaba.fastjson2.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Mannounce implements Listener {
    @EventHandler
    public void onGroupMessage(GroupMessageEvent event) {
        if (!Main.instance.getConfig().getStringList("allowUseGroup").contains(String.valueOf(event.getGroupId()))) {
            return;
        }
        if (!Main.instance.getConfig().getBoolean("mannounceHook")) {
            return;
        }

        Pattern pattern = Pattern.compile("\\[CQ:json,data=(.*?)]");
        Matcher matcher = pattern.matcher(event.getMessage());

        while (matcher.find()) {
            String dataString = MessageUtil.unescape(matcher.group(1));
            JSONObject appData = JSONObject.parseObject(dataString);

            if (appData.getString("app").equals("com.tencent.mannounce")) {
                JSONObject meta = appData.getJSONObject("meta");
                JSONObject mannounce = meta.getJSONObject("mannounce");
                String text = new String(Base64Util.decode(mannounce.getString("text")));

                JSONObject data = new JSONObject();
                data.put("text", text);
                MHDFBot.getMinecraftWebSocketServer().send("mannounce", data);
            }
        }
    }
}
