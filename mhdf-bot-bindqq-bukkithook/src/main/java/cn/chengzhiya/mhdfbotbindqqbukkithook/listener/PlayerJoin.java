package cn.chengzhiya.mhdfbotbindqqbukkithook.listener;

import cn.chengzhiya.mhdfbotbindqqbukkithook.util.DatabaseUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        DatabaseUtil.updatePlayerData(event.getPlayer().getName());
        DatabaseUtil.updatePlayerVerify(event.getPlayer().getName());
    }
}
