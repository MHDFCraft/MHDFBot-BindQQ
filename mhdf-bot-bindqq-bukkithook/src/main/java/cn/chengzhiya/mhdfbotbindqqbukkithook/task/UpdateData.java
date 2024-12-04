package cn.chengzhiya.mhdfbotbindqqbukkithook.task;

import cn.chengzhiya.mhdfbotbindqqbukkithook.util.DatabaseUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public final class UpdateData extends BukkitRunnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            DatabaseUtil.updatePlayerDataCache(player.getName());
            DatabaseUtil.updatePlayerVerifyCache(player.getName());
        }
    }
}
