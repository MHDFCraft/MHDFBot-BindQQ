package cn.ChengZhiYa.MHDFBotBindQQ;

import cn.ChengZhiYa.MHDFBot.api.MHDFBotPlugin;
import cn.ChengZhiYa.MHDFBotBindQQ.command.Bind;
import cn.ChengZhiYa.MHDFBotBindQQ.command.Info;
import cn.ChengZhiYa.MHDFBotBindQQ.command.InfoQQ;
import cn.ChengZhiYa.MHDFBotBindQQ.command.UnBind;
import cn.ChengZhiYa.MHDFBotBindQQ.entity.DatabaseConfig;
import cn.ChengZhiYa.MHDFBotBindQQ.listener.*;
import cn.ChengZhiYa.MHDFBotBindQQ.task.ClearPlayerVerify;
import cn.ChengZhiYa.MHDFBotBindQQ.task.UpdatePlayerVerify;

import static cn.ChengZhiYa.MHDFBotBindQQ.util.DatabaseUtil.connectDatabase;
import static cn.ChengZhiYa.MHDFBotBindQQ.util.DatabaseUtil.intiDatabase;
import static cn.ChengZhiYa.MHDFBotBindQQ.util.LangUtil.i18n;
import static cn.ChengZhiYa.MHDFBotBindQQ.util.LangUtil.reloadLang;

public final class main extends MHDFBotPlugin {
    public static main instance;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        reloadConfig();
        saveResource(getDataFolder().getPath(), "lang.yml", "lang.yml", false);
        reloadLang();

        connectDatabase(new DatabaseConfig(
                getConfig().getString("DatabaseSettings.Host"),
                getConfig().getString("DatabaseSettings.Database"),
                getConfig().getString("DatabaseSettings.User"),
                getConfig().getString("DatabaseSettings.Password")
        ));
        intiDatabase();

        getCommand("bind").setCommandExecutor(new Bind()).setDescription(i18n("Command.Bind.Description")).setUsage(i18n("Command.Bind.Usage")).register();
        getCommand("unbind").setCommandExecutor(new UnBind()).setDescription(i18n("Command.UnBind.Description")).setUsage(i18n("Command.UnBind.Usage")).register();
        getCommand("info").setCommandExecutor(new Info()).setDescription(i18n("Command.Info.Description")).setUsage(i18n("Command.Info.Usage")).register();
        getCommand("infoqq").setCommandExecutor(new InfoQQ()).setDescription(i18n("Command.InfoQQ.Description")).setUsage(i18n("Command.InfoQQ.Usage")).register();

        registerListener(new At());
        registerListener(new Chat());
        registerListener(new ChatTimes());
        registerListener(new Mannounce());
        registerListener(new FriendAddRequest());
        registerListener(new GroupMessage());
        registerListener(new PrivateMessage());
        registerListener(new WebSocket());

        new UpdatePlayerVerify().runTaskAsynchronouslyTimer(0L, 1L);
        new ClearPlayerVerify().runTaskAsynchronouslyTimer(0L, 60L);

        colorLog("&r===========&6梦之Q绑&r============");
        colorLog("&a插件加载成功!");
        colorLog("&r===========&6梦之Q绑&r============");
    }

    @Override
    public void onDisable() {
        instance = null;

        colorLog("&r===========&6梦之Q绑&r============");
        colorLog("&a插件卸载成功!");
        colorLog("&r===========&6梦之Q绑&r============");
    }
}