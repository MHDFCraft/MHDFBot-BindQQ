package cn.chengzhiya.mhdfbotbindqqbukkithook;

import cn.chengzhiya.mhdfbotbindqqbukkithook.command.Reload;
import cn.chengzhiya.mhdfbotbindqqbukkithook.entity.DatabaseConfig;
import cn.chengzhiya.mhdfbotbindqqbukkithook.hook.PlaceholderAPI;
import cn.chengzhiya.mhdfbotbindqqbukkithook.listener.PlayerChat;
import cn.chengzhiya.mhdfbotbindqqbukkithook.listener.PlayerJoin;
import cn.chengzhiya.mhdfbotbindqqbukkithook.listener.WebSocket;
import cn.chengzhiya.mhdfbotbindqqbukkithook.task.BindMessage;
import cn.chengzhiya.mhdfbotbindqqbukkithook.task.UpdateData;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

import static cn.chengzhiya.mhdfbotbindqqbukkithook.util.DatabaseUtil.connectDatabase;
import static cn.chengzhiya.mhdfbotbindqqbukkithook.util.DatabaseUtil.intiDatabase;
import static cn.chengzhiya.mhdfbotbindqqbukkithook.util.message.LogUtil.colorLog;

public final class Main extends JavaPlugin {
    public static Main instance;
    private final PlaceholderAPI placeholderAPI = new PlaceholderAPI();

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        saveDefaultConfig();
        reloadConfig();

        connectDatabase(new DatabaseConfig(
                getConfig().getString("databaseSettings.host"),
                getConfig().getString("databaseSettings.database"),
                getConfig().getString("databaseSettings.user"),
                getConfig().getString("databaseSettings.password")
        ));
        intiDatabase();

        Bukkit.getPluginManager().registerEvents(new PlayerChat(), this);
        Bukkit.getPluginManager().registerEvents(new WebSocket(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);

        Objects.requireNonNull(getCommand("bindqqreload")).setExecutor(new Reload());

        new BindMessage().runTaskTimerAsynchronously(this, 0L, 20L);
        new UpdateData().runTaskTimerAsynchronously(this, 0L, 20L);

        placeholderAPI.register();

        colorLog("&f============&6梦之Q绑-子服连接器&f============");
        colorLog("&e插件启动完成!");
        colorLog("&f============&6梦之Q绑-子服连接器&f============");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        instance = null;

        placeholderAPI.unregister();

        colorLog("&f============&6梦之Q绑-子服连接器&f============");
        colorLog("&e插件已卸载!");
        colorLog("&f============&6梦之Q绑-子服连接器&f============");
    }
}
