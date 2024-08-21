package cn.ChengZhiYa.MHDFBotBindQQBukkitHook;

import cn.ChengZhiYa.MHDFBotBindQQBukkitHook.command.GroupHook;
import cn.ChengZhiYa.MHDFBotBindQQBukkitHook.command.Reload;
import cn.ChengZhiYa.MHDFBotBindQQBukkitHook.entity.DatabaseConfig;
import cn.ChengZhiYa.MHDFBotBindQQBukkitHook.hook.PlaceholderAPI;
import cn.ChengZhiYa.MHDFBotBindQQBukkitHook.listener.PlayerChat;
import cn.ChengZhiYa.MHDFBotBindQQBukkitHook.listener.PlayerJoin;
import cn.ChengZhiYa.MHDFBotBindQQBukkitHook.listener.WebSocket;
import cn.ChengZhiYa.MHDFBotBindQQBukkitHook.task.BindMessage;
import cn.ChengZhiYa.MHDFBotBindQQBukkitHook.task.UpdateData;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFBotBindQQBukkitHook.util.DatabaseUtil.connectDatabase;
import static cn.ChengZhiYa.MHDFBotBindQQBukkitHook.util.DatabaseUtil.intiDatabase;
import static cn.ChengZhiYa.MHDFBotBindQQBukkitHook.util.message.LogUtil.colorLog;

public final class main extends JavaPlugin {
    public static main instance;
    @Getter
    public static PluginDescriptionFile descriptionFile;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        descriptionFile = getDescription();

        saveDefaultConfig();
        reloadConfig();

        connectDatabase(new DatabaseConfig(
                getConfig().getString("DatabaseSettings.Host"),
                getConfig().getString("DatabaseSettings.Database"),
                getConfig().getString("DatabaseSettings.User"),
                getConfig().getString("DatabaseSettings.Password")
        ));
        intiDatabase();

        Bukkit.getPluginManager().registerEvents(new PlayerChat(), this);
        Bukkit.getPluginManager().registerEvents(new WebSocket(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);

        Objects.requireNonNull(getCommand("bindqqreload")).setExecutor(new Reload());
        Objects.requireNonNull(getCommand("grouphook")).setExecutor(new GroupHook());

        new BindMessage().runTaskTimerAsynchronously(this, 0L, 20L);
        new UpdateData().runTaskTimerAsynchronously(this, 0L, 20L);

        PlaceholderAPI.registerPlaceholders();

        colorLog("&f============&6梦之Q绑-子服连接器&f============");
        colorLog("&e插件启动完成!");
        colorLog("&f============&6梦之Q绑-子服连接器&f============");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        instance = null;

        PlaceholderAPI.unregisterPlaceholders();

        colorLog("&f============&6梦之Q绑-子服连接器&f============");
        colorLog("&e插件已卸载!");
        colorLog("&f============&6梦之Q绑-子服连接器&f============");
    }
}
