package cn.chengzhiya.mhdfbotbindqq.command;

import cn.chengzhiya.mhdfbot.api.command.CommandExecutor;
import cn.chengzhiya.mhdfbotbindqq.Main;
import cn.chengzhiya.mhdfbotbindqq.util.DatabaseUtil;

import java.util.Objects;

import static cn.chengzhiya.mhdfbotbindqq.util.LangUtil.i18n;

public final class UnBind implements CommandExecutor {
    @Override
    public void onCommand(String command, String[] args) {
        if (args.length == 0) {
            Main.instance.getLogger().error("{}{}", i18n("prefix"), i18n("commands.unBind.usage"));
            return;
        }

        String playerName = args[0];

        if (!DatabaseUtil.ifPlayerDataExist(playerName)) {
            Main.instance.getLogger().error("{}{}", i18n("prefix"), i18n("commands.unBind.noBind")
                    .replace("{player}", playerName));
            return;
        }

        DatabaseUtil.unbind(Objects.requireNonNull(DatabaseUtil.getPlayerData(playerName)));
        Main.instance.getLogger().info("{}{}", i18n("prefix"), i18n("commands.unBind.done")
                .replace("{player}", playerName));
    }
}
