package cn.ChengZhiYa.MHDFBotBindQQBungeeCordHook;

import cn.ChengZhiYa.MHDFBotBindQQBungeeCordHook.entity.DatabaseConfig;
import cn.ChengZhiYa.MHDFBotBindQQBungeeCordHook.util.ConfigUtil;
import cn.ChengZhiYa.MHDFBotBindQQBungeeCordHook.util.DatabaseUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.concurrent.TimeUnit;

import static cn.ChengZhiYa.MHDFBotBindQQBungeeCordHook.util.ConfigUtil.reloadConfig;
import static cn.ChengZhiYa.MHDFBotBindQQBungeeCordHook.util.ConfigUtil.saveDefaultConfig;
import static cn.ChengZhiYa.MHDFBotBindQQBungeeCordHook.util.DatabaseUtil.connectDatabase;
import static cn.ChengZhiYa.MHDFBotBindQQBungeeCordHook.util.DatabaseUtil.intiDatabase;

public final class main extends Plugin {
    public static main instance;

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
            for (ProxiedPlayer player : main.instance.getProxy().getPlayers()) {
                DatabaseUtil.updatePlayerVerify(player.getName());
            }
        }, 0L, 1L, TimeUnit.SECONDS);

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
