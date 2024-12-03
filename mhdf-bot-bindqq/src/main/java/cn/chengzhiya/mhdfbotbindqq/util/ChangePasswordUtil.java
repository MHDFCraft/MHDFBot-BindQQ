package cn.chengzhiya.mhdfbotbindqq.util;

import cn.chengzhiya.mhdfbot.api.MHDFBot;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

import java.util.HashMap;

public final class ChangePasswordUtil {
    @Getter
    static HashMap<Long, String> changePasswordHashMap = new HashMap<>();

    /**
     * 发送修改密码请求
     *
     * @param playerName 目标游戏ID
     * @param password   要修改的密码
     */
    public static void changePassword(String playerName, String password) {
        JSONObject data = new JSONObject();
        data.put("player_name", playerName);
        data.put("password", password);

        MHDFBot.getMinecraftWebSocketServer().send("changePassword", data);
    }
}
