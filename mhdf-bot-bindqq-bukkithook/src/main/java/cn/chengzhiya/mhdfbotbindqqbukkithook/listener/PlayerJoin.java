package cn.chengzhiya.mhdfbotbindqqbukkithook.listener;

import cn.chengzhiya.mhdfbotbindqqbukkithook.entity.PlayerVerify;
import cn.chengzhiya.mhdfbotbindqqbukkithook.util.DatabaseUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Random;

public final class PlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        DatabaseUtil.updatePlayerVerify(new PlayerVerify(
                event.getPlayer().getName(),
                String.valueOf(new Random().nextInt(1000, 9999)))
        );

        DatabaseUtil.updatePlayerDataCache(event.getPlayer().getName());
        DatabaseUtil.updatePlayerVerifyCache(event.getPlayer().getName());
    }
}
