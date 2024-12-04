package cn.chengzhiya.mhdfbotbindqqbungeehook.listener;

import cn.chengzhiya.mhdfbotbindqqbungeehook.entity.PlayerVerify;
import cn.chengzhiya.mhdfbotbindqqbungeehook.util.ConfigUtil;
import cn.chengzhiya.mhdfbotbindqqbungeehook.util.DatabaseUtil;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Random;

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
            DatabaseUtil.updatePlayerVerify(new PlayerVerify(
                    event.getPlayer().getName(),
                    String.valueOf(new Random().nextInt(1000, 9999)))
            );
        }
    }
}
