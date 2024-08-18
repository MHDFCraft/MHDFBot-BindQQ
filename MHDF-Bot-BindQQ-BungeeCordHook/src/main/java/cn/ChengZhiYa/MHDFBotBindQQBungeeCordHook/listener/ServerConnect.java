package cn.ChengZhiYa.MHDFBotBindQQBungeeCordHook.listener;

import cn.ChengZhiYa.MHDFBotBindQQBungeeCordHook.util.ConfigUtil;
import cn.ChengZhiYa.MHDFBotBindQQBungeeCordHook.util.DatabaseUtil;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public final class ServerConnect implements Listener {
    @EventHandler
    public void onServerConnect(ServerConnectEvent event) {
        if (event.getReason() == ServerConnectEvent.Reason.JOIN_PROXY) {
            DatabaseUtil.updatePlayerVerify(event.getPlayer().getName());
        }
        if (!event.isCancelled()) {
            if (!ConfigUtil.getConfig().getStringList("AllowJoinServerList").contains(event.getTarget().getName())) {
                if (!DatabaseUtil.ifPlayerDataExist(event.getPlayer().getName())) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
