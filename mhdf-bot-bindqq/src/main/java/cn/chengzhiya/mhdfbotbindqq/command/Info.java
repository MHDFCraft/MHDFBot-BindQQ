package cn.chengzhiya.mhdfbotbindqq.command;

import cn.chengzhiya.mhdfbot.api.command.CommandExecutor;
import cn.chengzhiya.mhdfbotbindqq.Main;
import cn.chengzhiya.mhdfbotbindqq.entity.PlayerData;
import cn.chengzhiya.mhdfbotbindqq.util.DatabaseUtil;

import java.util.Objects;

import static cn.chengzhiya.mhdfbotbindqq.util.LangUtil.i18n;

public final class Info implements CommandExecutor {
    @Override
    public void onCommand(String command, String[] args) {
        if (args.length == 0) {
            Main.instance.getLogger().error("{}{}", i18n("prefix"), i18n("commands.info.usage"));
            return;
        }

        String playerName = args[0];

        if (!DatabaseUtil.ifPlayerDataExist(playerName)) {
            Main.instance.getLogger().error("{}{}", i18n("prefix"), i18n("commands.info.noBind").replace("{player}", playerName));
        }

        PlayerData playerData = DatabaseUtil.getPlayerData(playerName);
        Main.instance.getLogger().info("{}{}", i18n("prefix"), i18n("commands.info.done")
                .replace("{player}", playerName)
                .replace("{qq}", String.valueOf(Objects.requireNonNull(playerData).getQQ())));
    }
}
