package cn.ChengZhiYa.MHDFBotBindQQ.listener;

import cn.ChengZhiYa.MHDFBot.api.interfaces.EventHandler;
import cn.ChengZhiYa.MHDFBot.api.manager.Listener;
import cn.ChengZhiYa.MHDFBot.event.message.GroupMessageEvent;
import cn.ChengZhiYa.MHDFBotBindQQ.entity.PlayerData;
import cn.ChengZhiYa.MHDFBotBindQQ.main;
import cn.ChengZhiYa.MHDFBotBindQQ.util.DatabaseUtil;

import java.util.Objects;

public final class ChatTimes implements Listener {
    @EventHandler
    public void onGroupMessage(GroupMessageEvent event) {
        if (main.instance.getConfig().getStringList("AllowUseGroup").contains(String.valueOf(event.getGroupId()))) {
            for (String playerName : DatabaseUtil.getQqBindList(event.getSender().getUserId())) {
                PlayerData playerData = DatabaseUtil.getPlayerData(playerName);
                Objects.requireNonNull(playerData).setChatTimes(playerData.getChatTimes() + 1);
                playerData.setDayChatTimes(playerData.getDayChatTimes() + 1);
                DatabaseUtil.setPlayerData(playerData);
            }
        }
    }
}
