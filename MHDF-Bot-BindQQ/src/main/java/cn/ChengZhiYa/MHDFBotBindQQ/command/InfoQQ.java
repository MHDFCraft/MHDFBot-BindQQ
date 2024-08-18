package cn.ChengZhiYa.MHDFBotBindQQ.command;

import cn.ChengZhiYa.MHDFBot.api.manager.CommandExecutor;
import cn.ChengZhiYa.MHDFBotBindQQ.main;
import cn.ChengZhiYa.MHDFBotBindQQ.util.DatabaseUtil;

import static cn.ChengZhiYa.MHDFBotBindQQ.util.LangUtil.i18n;

public final class InfoQQ implements CommandExecutor {
    @Override
    public void onCommand(String label, String[] args) {
        if (args.length == 1) {
            Long qq = Long.valueOf(args[0]);
            if (DatabaseUtil.ifPlayerDataExist(qq)) {
                main.instance.colorLog(i18n("Command.Bind.Prefix") + i18n("Command.Info.Message").replaceAll("\\{Player}", String.valueOf(DatabaseUtil.getQqBindList(qq))).replaceAll("\\{QQ}", String.valueOf(qq)));
            } else {
                main.instance.colorLog(i18n("Command.Bind.Prefix") + i18n("Command.Info.NoBind").replaceAll("\\{QQ}", String.valueOf(qq)));
            }
        } else {
            main.instance.colorLog(i18n("Command.Bind.Prefix") + i18n("Command.Info.Usage"));
        }
    }
}
