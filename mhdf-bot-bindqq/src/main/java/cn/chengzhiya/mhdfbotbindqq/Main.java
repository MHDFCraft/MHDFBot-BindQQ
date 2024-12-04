package cn.chengzhiya.mhdfbotbindqq;

import cn.chengzhiya.mhdfbot.api.entity.plugin.Command;
import cn.chengzhiya.mhdfbot.api.plugin.JavaPlugin;
import cn.chengzhiya.mhdfbotbindqq.command.Bind;
import cn.chengzhiya.mhdfbotbindqq.command.Info;
import cn.chengzhiya.mhdfbotbindqq.command.InfoQQ;
import cn.chengzhiya.mhdfbotbindqq.command.UnBind;
import cn.chengzhiya.mhdfbotbindqq.entity.DatabaseConfig;
import cn.chengzhiya.mhdfbotbindqq.listener.*;
import cn.chengzhiya.mhdfbotbindqq.task.ClearPlayerVerify;
import cn.chengzhiya.mhdfbotbindqq.task.UpdatePlayerVerify;
import cn.chengzhiya.mhdfbotbindqq.util.DatabaseUtil;
import cn.chengzhiya.mhdfbotbindqq.util.LangUtil;

import static cn.chengzhiya.mhdfbotbindqq.util.LangUtil.i18n;

public final class Main extends JavaPlugin {
    public static Main instance;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        reloadConfig();

        LangUtil.saveDefaultLang();
        LangUtil.reloadLang();

        DatabaseUtil.connectDatabase(new DatabaseConfig(
                getConfig().getString("databaseSettings.host"),
                getConfig().getString("databaseSettings.database"),
                getConfig().getString("databaseSettings.user"),
                getConfig().getString("databaseSettings.password")
        ));
        DatabaseUtil.intiDatabase();

        registerCommand(
                new Command("bind")
                        .executor(new Bind())
                        .description(i18n("commands.bind.description"))
                        .usage(i18n("commands.bind.usage"))
        );

        registerCommand(
                new Command("unbind")
                        .executor(new UnBind())
                        .description(i18n("commands.unBind.description"))
                        .usage(i18n("commands.unBind.usage"))
        );

        registerCommand(
                new Command("info")
                        .executor(new Info())
                        .description(i18n("commands.info.description"))
                        .usage(i18n("commands.info.usage"))
        );

        registerCommand(
                new Command("infoqq")
                        .executor(new InfoQQ())
                        .description(i18n("commands.infoQq.description"))
                        .usage(i18n("commands.infoQq.usage"))
        );

        registerListener(new At());
        registerListener(new Chat());
        registerListener(new ChatTimes());
        registerListener(new Mannounce());
        registerListener(new FriendAddRequest());
        registerListener(new GroupMessage());
        registerListener(new LeaveGroup());
        registerListener(new PrivateMessage());
        registerListener(new WebSocket());

        new UpdatePlayerVerify().runTaskAsynchronouslyTimer(0L, 1L);
        new ClearPlayerVerify().runTaskAsynchronouslyTimer(0L, 60L);

        getLogger().info("===========梦之Q绑============");
        getLogger().info("插件加载成功!");
        getLogger().info("===========梦之Q绑============");
    }

    @Override
    public void onDisable() {
        instance = null;

        getLogger().info("===========梦之Q绑============");
        getLogger().info("插件卸载成功!");
        getLogger().info("===========梦之Q绑============");
    }
}