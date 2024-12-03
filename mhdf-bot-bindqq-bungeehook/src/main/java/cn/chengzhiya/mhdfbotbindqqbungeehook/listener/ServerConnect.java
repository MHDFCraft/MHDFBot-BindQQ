package cn.chengzhiya.mhdfbotbindqqbungeehook.listener;

import cn.chengzhiya.mhdfbotbindqqbungeehook.util.ConfigUtil;
import cn.chengzhiya.mhdfbotbindqqbungeehook.util.DatabaseUtil;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public final class ServerConnect implements Listener {
    @EventHandler
    public void onServerConnect(ServerConnectEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!ConfigUtil.getConfig().getStringList("allowJoinServerList").contains(event.getTarget().getName())) {
            if (!DatabaseUtil.ifPlayerDataExist(event.getPlayer().getName())) {
                event.setCancelled(true);
                return;
            }
        }

        if (event.getReason() == ServerConnectEvent.Reason.JOIN_PROXY) {
            DatabaseUtil.updatePlayerVerify(event.getPlayer().getName());
        }
    }
}
