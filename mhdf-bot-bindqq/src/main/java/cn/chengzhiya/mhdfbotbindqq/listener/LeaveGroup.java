package cn.chengzhiya.mhdfbotbindqq.listener;

import cn.chengzhiya.mhdfbot.api.event.notice.GroupDecreaseEvent;
import cn.chengzhiya.mhdfbot.api.listener.EventHandler;
import cn.chengzhiya.mhdfbot.api.listener.Listener;
import cn.chengzhiya.mhdfbotbindqq.Main;
import cn.chengzhiya.mhdfbotbindqq.entity.PlayerData;
import cn.chengzhiya.mhdfbotbindqq.util.DatabaseUtil;

public final class LeaveGroup implements Listener {
    @EventHandler
    public void onGroupDecrease(GroupDecreaseEvent event) {
        if (!Main.instance.getConfig().getStringList("allowUseGroup").contains(String.valueOf(event.getGroupId()))) {
            return;
        }

        if (!Main.instance.getConfig().getBoolean("leaveGroupUnBind")) {
            return;
        }

        if (!DatabaseUtil.ifPlayerDataExist(event.getUserId())) {
            return;
        }

        for (String playerName : DatabaseUtil.getQqBindList(event.getUserId())) {
            DatabaseUtil.unbind(new PlayerData(playerName, event.getUserId()));
        }
    }
}
