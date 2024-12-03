package cn.chengzhiya.mhdfbotbindqq.listener;

import cn.chengzhiya.mhdfbot.api.MHDFBot;
import cn.chengzhiya.mhdfbot.api.builder.MessageBuilder;
import cn.chengzhiya.mhdfbot.api.event.message.PrivateMessageEvent;
import cn.chengzhiya.mhdfbot.api.listener.EventHandler;
import cn.chengzhiya.mhdfbot.api.listener.Listener;
import cn.chengzhiya.mhdfbotbindqq.util.ChangePasswordUtil;

import static cn.chengzhiya.mhdfbotbindqq.util.LangUtil.i18n;

public final class PrivateMessage implements Listener {
    @EventHandler
    public void onPrivateMessage(PrivateMessageEvent event) {
        if (ChangePasswordUtil.getChangePasswordHashMap().get(event.getSender().getUserId()) == null) {
            return;
        }

        String playerName = ChangePasswordUtil.getChangePasswordHashMap().get(event.getSender().getUserId());
        String password = event.getMessage();
        ChangePasswordUtil.changePassword(playerName, password);
        ChangePasswordUtil.getChangePasswordHashMap().remove(event.getSender().getUserId());

        MHDFBot.sendPrivateMsg(event.getSender().getUserId(), MessageBuilder.builder().text(
                i18n("messages.changePassword.changeDone")
                        .replace("{player}", playerName)
                        .replace("{password}", password)
        ).build());
    }
}
