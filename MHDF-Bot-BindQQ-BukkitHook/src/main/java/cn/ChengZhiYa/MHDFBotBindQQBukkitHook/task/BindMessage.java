package cn.ChengZhiYa.MHDFBotBindQQBukkitHook.task;

import cn.ChengZhiYa.MHDFBotBindQQBukkitHook.main;
import cn.ChengZhiYa.MHDFBotBindQQBukkitHook.util.ActionUtil;
import cn.ChengZhiYa.MHDFBotBindQQBukkitHook.util.DatabaseUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public final class BindMessage extends BukkitRunnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (DatabaseUtil.getPlayerData(player.getName()) == null) {
                ActionUtil.runAction(player, main.instance.getConfig().getStringList("Actions.BindMessage"));
            }
        }
    }
}
