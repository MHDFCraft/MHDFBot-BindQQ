package cn.chengzhiya.mhdfbotbindqq.command;

import cn.chengzhiya.mhdfbot.api.command.CommandExecutor;
import cn.chengzhiya.mhdfbotbindqq.Main;
import cn.chengzhiya.mhdfbotbindqq.util.DatabaseUtil;

import static cn.chengzhiya.mhdfbotbindqq.util.LangUtil.i18n;

public final class InfoQQ implements CommandExecutor {
    @Override
    public void onCommand(String command, String[] args) {
        if (args.length == 0) {
            Main.instance.getLogger().error("{}{}", i18n("prefix"), i18n("commands.infoQq.usage"));
            return;
        }

        Long qq = Long.valueOf(args[0]);

        if (!DatabaseUtil.ifPlayerDataExist(qq)) {
            Main.instance.getLogger().error("{}{}", i18n("prefix"), i18n("commands.infoQq.noBind")
                    .replace("{qq}", String.valueOf(qq)));
            return;
        }

        Main.instance.getLogger().info("{}{}", i18n("prefix"), i18n("commands.infoQq.done")
                .replace("{player}", String.valueOf(DatabaseUtil.getQqBindList(qq)))
                .replace("{qq}", String.valueOf(qq)));
    }
}
