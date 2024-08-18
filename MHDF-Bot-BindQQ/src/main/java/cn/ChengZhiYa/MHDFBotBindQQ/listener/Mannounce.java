package cn.ChengZhiYa.MHDFBotBindQQ.listener;

import cn.ChengZhiYa.MHDFBot.api.interfaces.EventHandler;
import cn.ChengZhiYa.MHDFBot.api.manager.Listener;
import cn.ChengZhiYa.MHDFBot.api.util.MessageUtil;
import cn.ChengZhiYa.MHDFBot.event.message.GroupMessageEvent;
import cn.ChengZhiYa.MHDFBot.server.WebSocket;
import cn.ChengZhiYa.MHDFBotBindQQ.main;
import cn.ChengZhiYa.MHDFBotBindQQ.util.Base64Util;
import com.alibaba.fastjson2.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Mannounce implements Listener {
    @EventHandler
    public void onGroupMessage(GroupMessageEvent event) {
        if (main.instance.getConfig().getStringList("AllowUseGroup").contains(String.valueOf(event.getGroupId()))) {
            if (main.instance.getConfig().getBoolean("MannounceHook")) {
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
                        WebSocket.send("mannounce", data);
                    }
                }
            }
        }
    }
}
