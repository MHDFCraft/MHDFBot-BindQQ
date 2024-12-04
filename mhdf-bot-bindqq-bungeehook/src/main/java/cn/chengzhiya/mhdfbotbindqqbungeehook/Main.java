package cn.chengzhiya.mhdfbotbindqqbungeehook;

import cn.chengzhiya.mhdfbotbindqqbungeehook.entity.DatabaseConfig;
import cn.chengzhiya.mhdfbotbindqqbungeehook.listener.Chat;
import cn.chengzhiya.mhdfbotbindqqbungeehook.listener.ServerConnect;
import cn.chengzhiya.mhdfbotbindqqbungeehook.util.ConfigUtil;
import cn.chengzhiya.mhdfbotbindqqbungeehook.util.DatabaseUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.TimeUnit;

public final class Main extends Plugin {
    public static Main instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        ConfigUtil.saveDefaultConfig();
        ConfigUtil.reloadConfig();

        DatabaseUtil.connectDatabase(new DatabaseConfig(
                ConfigUtil.getConfig().getString("databaseSettings.host"),
                ConfigUtil.getConfig().getString("databaseSettings.database"),
                ConfigUtil.getConfig().getString("databaseSettings.user"),
                ConfigUtil.getConfig().getString("databaseSettings.password")
        ));
        DatabaseUtil.intiDatabase();

        getProxy().getScheduler().schedule(this, () -> {
            for (ProxiedPlayer player : Main.instance.getProxy().getPlayers()) {
                DatabaseUtil.updatePlayerVerifyCache(player.getName());
            }
        }, 0L, 1L, TimeUnit.SECONDS);

        getProxy().getPluginManager().registerListener(this, new Chat());
        getProxy().getPluginManager().registerListener(this, new ServerConnect());

        getLogger().info("============梦之Q绑-BC连接器============");
        getLogger().info("插件启动完成!");
        getLogger().info("============梦之Q绑-BC连接器============");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        instance = null;

        getLogger().info("============梦之Q绑-BC连接器============");
        getLogger().info("插件已卸载!");
        getLogger().info("============梦之Q绑-BC连接器============");
    }
}
