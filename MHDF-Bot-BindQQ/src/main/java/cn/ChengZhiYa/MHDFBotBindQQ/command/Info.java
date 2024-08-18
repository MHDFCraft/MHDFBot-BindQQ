package cn.ChengZhiYa.MHDFBotBindQQ.command;

import cn.ChengZhiYa.MHDFBot.api.manager.CommandExecutor;
import cn.ChengZhiYa.MHDFBotBindQQ.entity.PlayerData;
import cn.ChengZhiYa.MHDFBotBindQQ.main;
import cn.ChengZhiYa.MHDFBotBindQQ.util.DatabaseUtil;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFBotBindQQ.util.LangUtil.i18n;

public final class Info implements CommandExecutor {
    @Override
    public void onCommand(String label, String[] args) {
        if (args.length == 1) {
            String playerName = args[0];
            if (DatabaseUtil.ifPlayerDataExist(playerName)) {
                PlayerData playerData = DatabaseUtil.getPlayerData(playerName);
                main.instance.colorLog(i18n("Command.Bind.Prefix") + i18n("Command.Info.Message").replaceAll("\\{Player}", playerName).replaceAll("\\{QQ}", String.valueOf(Objects.requireNonNull(playerData).getQQ())));
            } else {
                main.instance.colorLog(i18n("Command.Bind.Prefix") + i18n("Command.Info.NoBind").replaceAll("\\{Player}", playerName));
            }
        } else {
            main.instance.colorLog(i18n("Command.Bind.Prefix") + i18n("Command.Info.Usage"));
        }
    }
}
