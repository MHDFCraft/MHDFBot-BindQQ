package cn.chengzhiya.mhdfbotbindqq.listener;

import cn.chengzhiya.mhdfbot.api.event.message.GroupMessageEvent;
import cn.chengzhiya.mhdfbot.api.listener.EventHandler;
import cn.chengzhiya.mhdfbot.api.listener.Listener;
import cn.chengzhiya.mhdfbotbindqq.Main;
import cn.chengzhiya.mhdfbotbindqq.entity.PlayerData;
import cn.chengzhiya.mhdfbotbindqq.util.DatabaseUtil;

import java.util.Objects;

public final class ChatTimes implements Listener {
    @EventHandler
    public void onGroupMessage(GroupMessageEvent event) {
        if (!Main.instance.getConfig().getStringList("allowUseGroup").contains(String.valueOf(event.getGroupId()))) {
            return;
        }
        for (String playerName : DatabaseUtil.getQqBindList(event.getSender().getUserId())) {
            PlayerData playerData = DatabaseUtil.getPlayerData(playerName);
            Objects.requireNonNull(playerData).setChatTimes(playerData.getChatTimes() + 1);
            playerData.setDayChatTimes(playerData.getDayChatTimes() + 1);
            DatabaseUtil.updatePlayerData(playerData);
        }
    }
}
