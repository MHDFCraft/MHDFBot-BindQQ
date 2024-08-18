package cn.ChengZhiYa.MHDFBotBindQQ.listener;

import cn.ChengZhiYa.MHDFBot.api.MHDFBot;
import cn.ChengZhiYa.MHDFBot.api.builder.MessageBuilder;
import cn.ChengZhiYa.MHDFBot.api.interfaces.EventHandler;
import cn.ChengZhiYa.MHDFBot.api.manager.Listener;
import cn.ChengZhiYa.MHDFBot.event.message.PrivateMessageEvent;
import cn.ChengZhiYa.MHDFBotBindQQ.util.ChangePasswordUtil;

import static cn.ChengZhiYa.MHDFBotBindQQ.util.LangUtil.i18n;

public final class PrivateMessage implements Listener {
    @EventHandler
    public void onPrivateMessage(PrivateMessageEvent event) {
        if (ChangePasswordUtil.getChangePasswordHashMap().get(event.getSender().getUserId()) != null) {
            String playerName = ChangePasswordUtil.getChangePasswordHashMap().get(event.getSender().getUserId());
            String password = event.getMessage();
            ChangePasswordUtil.changePassword(playerName, password);
            ChangePasswordUtil.getChangePasswordHashMap().remove(event.getSender().getUserId());
            MHDFBot.sendMessage(event, MessageBuilder.builder().text(
                    i18n("Messages.ChangePassword.ChangeDone").replaceAll("\\{Player}", playerName).replaceAll("\\{Password}", password)
            ).build());
        }
    }
}
