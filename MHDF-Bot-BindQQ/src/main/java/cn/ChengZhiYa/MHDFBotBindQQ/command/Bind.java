package cn.ChengZhiYa.MHDFBotBindQQ.command;

import cn.ChengZhiYa.MHDFBot.api.manager.CommandExecutor;
import cn.ChengZhiYa.MHDFBotBindQQ.entity.PlayerData;
import cn.ChengZhiYa.MHDFBotBindQQ.main;
import cn.ChengZhiYa.MHDFBotBindQQ.util.DatabaseUtil;

import static cn.ChengZhiYa.MHDFBotBindQQ.util.LangUtil.i18n;

public final class Bind implements CommandExecutor {
    @Override
    public void onCommand(String label, String[] args) {
        if (args.length == 2) {
            Long qq = Long.parseLong(args[0]);
            String playerName = args[1];
            if (!DatabaseUtil.ifPlayerDataExist(playerName)) {
                DatabaseUtil.bind(new PlayerData(playerName, qq));
                main.instance.colorLog(i18n("Command.Bind.Prefix") + i18n("Command.Bind.BindDone").replaceAll("\\{Player}", playerName).replaceAll("\\{QQ}", String.valueOf(qq)));
            } else {
                main.instance.colorLog(i18n("Command.Bind.Prefix") + i18n("Command.Bind.NoBind").replaceAll("\\{Player}", playerName).replaceAll("\\{QQ}", String.valueOf(qq)));
            }
        } else {
            main.instance.colorLog(i18n("Command.Bind.Prefix") + i18n("Command.Bind.Usage"));
        }
    }
}
