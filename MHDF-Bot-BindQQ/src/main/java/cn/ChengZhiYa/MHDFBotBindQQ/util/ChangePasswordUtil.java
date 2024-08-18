package cn.ChengZhiYa.MHDFBotBindQQ.util;

import cn.ChengZhiYa.MHDFBot.server.WebSocket;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

import java.util.HashMap;

public final class ChangePasswordUtil {
    @Getter
    static HashMap<Long, String> changePasswordHashMap = new HashMap<>();

    public static void changePassword(String playerName, String password) {
        JSONObject data = new JSONObject();
        data.put("playerName", playerName);
        data.put("password", password);
        WebSocket.send("changePassword", data);
    }
}
