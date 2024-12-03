package cn.chengzhiya.mhdfbotbindqqbukkithook.task;

import cn.chengzhiya.mhdfbotbindqqbukkithook.Main;
import cn.chengzhiya.mhdfbotbindqqbukkithook.util.ActionUtil;
import cn.chengzhiya.mhdfbotbindqqbukkithook.util.DatabaseUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public final class BindMessage extends BukkitRunnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (DatabaseUtil.getPlayerData(player.getName()) == null) {
                ActionUtil.runAction(player, Main.instance.getConfig().getStringList("Actions.BindMessage"));
            }
        }
    }
}
