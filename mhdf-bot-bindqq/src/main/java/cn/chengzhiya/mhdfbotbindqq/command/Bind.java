package cn.chengzhiya.mhdfbotbindqq.command;

import cn.chengzhiya.mhdfbot.api.command.CommandExecutor;
import cn.chengzhiya.mhdfbotbindqq.Main;
import cn.chengzhiya.mhdfbotbindqq.entity.PlayerData;
import cn.chengzhiya.mhdfbotbindqq.util.DatabaseUtil;

import static cn.chengzhiya.mhdfbotbindqq.util.LangUtil.i18n;

public final class Bind implements CommandExecutor {
    @Override
    public void onCommand(String command, String[] args) {
        if (args.length < 2) {
            Main.instance.getLogger().error("{}{}", i18n("prefix"), i18n("commands.Bind.Usage"));
            return;
        }

        Long qq = Long.parseLong(args[0]);
        String playerName = args[1];

        if (DatabaseUtil.ifPlayerDataExist(playerName)) {
            Main.instance.getLogger().error("{}{}", i18n("prefix"), i18n("commands.Bind.NoBind")
                    .replace("{player}", playerName)
                    .replace("{qq}", String.valueOf(qq)));
        }

        DatabaseUtil.bind(new PlayerData(playerName, qq));
        Main.instance.getLogger().info("{}{}", i18n("prefix"), i18n("commands.Bind.BindDone")
                .replace("{player}", playerName)
                .replace("{qq}", String.valueOf(qq)));
    }
}
