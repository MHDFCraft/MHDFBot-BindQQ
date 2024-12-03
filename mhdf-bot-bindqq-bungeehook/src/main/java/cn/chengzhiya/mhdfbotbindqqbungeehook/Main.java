package cn.chengzhiya.mhdfbotbindqqbungeehook;

import cn.chengzhiya.mhdfbotbindqqbungeehook.entity.DatabaseConfig;
import cn.chengzhiya.mhdfbotbindqqbungeehook.listener.Chat;
import cn.chengzhiya.mhdfbotbindqqbungeehook.listener.ServerConnect;
import cn.chengzhiya.mhdfbotbindqqbungeehook.util.ConfigUtil;
import cn.chengzhiya.mhdfbotbindqqbungeehook.util.DatabaseUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.TimeUnit;

import static cn.chengzhiya.mhdfbotbindqqbungeehook.util.ConfigUtil.reloadConfig;
import static cn.chengzhiya.mhdfbotbindqqbungeehook.util.ConfigUtil.saveDefaultConfig;
import static cn.chengzhiya.mhdfbotbindqqbungeehook.util.DatabaseUtil.connectDatabase;
import static cn.chengzhiya.mhdfbotbindqqbungeehook.util.DatabaseUtil.intiDatabase;

public final class Main extends Plugin {
    public static Main instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        saveDefaultConfig();
        reloadConfig();

        connectDatabase(new DatabaseConfig(
                ConfigUtil.getConfig().getString("DatabaseSettings.Host"),
                ConfigUtil.getConfig().getString("DatabaseSettings.Database"),
                ConfigUtil.getConfig().getString("DatabaseSettings.User"),
                ConfigUtil.getConfig().getString("DatabaseSettings.Password")
        ));
        intiDatabase();

        getProxy().getScheduler().schedule(this, () -> {
            for (ProxiedPlayer player : Main.instance.getProxy().getPlayers()) {
                DatabaseUtil.updatePlayerVerify(player.getName());
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
