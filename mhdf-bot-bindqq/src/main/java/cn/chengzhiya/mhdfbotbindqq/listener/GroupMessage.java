package cn.chengzhiya.mhdfbotbindqq.listener;

import cn.chengzhiya.mhdfbot.api.MHDFBot;
import cn.chengzhiya.mhdfbot.api.builder.MessageBuilder;
import cn.chengzhiya.mhdfbot.api.entity.user.Member;
import cn.chengzhiya.mhdfbot.api.enums.user.RoleType;
import cn.chengzhiya.mhdfbot.api.event.message.GroupMessageEvent;
import cn.chengzhiya.mhdfbot.api.listener.EventHandler;
import cn.chengzhiya.mhdfbot.api.listener.Listener;
import cn.chengzhiya.mhdfbotbindqq.Main;
import cn.chengzhiya.mhdfbotbindqq.entity.PlayerData;
import cn.chengzhiya.mhdfbotbindqq.entity.PlayerVerify;
import cn.chengzhiya.mhdfbotbindqq.util.ChangePasswordUtil;
import cn.chengzhiya.mhdfbotbindqq.util.DatabaseUtil;

import java.util.Objects;

import static cn.chengzhiya.mhdfbotbindqq.util.LangUtil.i18n;

public final class GroupMessage implements Listener {
    @EventHandler
    public void onGroupMessage(GroupMessageEvent event) {
        if (!Main.instance.getConfig().getStringList("allowUseGroup").contains(String.valueOf(event.getGroupId()))) {
            return;
        }
        if (!event.getMessage().startsWith("#")) {
            return;
        }

        String[] args = event.getMessage().split(" ");
        MessageBuilder messageBuilder = MessageBuilder.builder();
        if (Main.instance.getConfig().getBoolean("messageSettings.reply")) {
            messageBuilder.reply(event.getMessageId());
        }
        if (Main.instance.getConfig().getBoolean("messageSettings.at")) {
            messageBuilder.at(event.getSender().getUserId()).text("\n");
        }
        switch (args[0]) {
            case "#绑定" -> {
                // 命令帮助
                if (args.length == 1) {
                    messageBuilder.text(i18n("messages.bind.usage"));
                    break;
                }

                Member member = MHDFBot.getGroupMemberInfo(event.getGroupId(), event.getSender().getUserId(), false);
                String playerName = args[1];

                // QQ等级不足
                if (member.getLevel() < Main.instance.getConfig().getInt("bindSettings.minQQLevel")) {
                    messageBuilder.text(i18n("messages.bind.minQQLevel")
                            .replace("{level}", String.valueOf(Main.instance.getConfig().getInt("bindSettings.minQQLevel"))));
                    break;
                }

                // 游戏ID包含无效字符
                if (!playerName.matches(Main.instance.getConfig().getString("bindSettings.bindNameRegex"))) {
                    messageBuilder.text(i18n("messages.bind.bindNameRegex"));
                    break;
                }

                // 游戏ID过短
                if (playerName.length() < Main.instance.getConfig().getInt("bindSettings.bindNameMinLength")) {
                    messageBuilder.text(i18n("messages.bind.BindNameMinLength")
                            .replace("{length}", String.valueOf(Main.instance.getConfig().getInt("bindSettings.bindNameMinLength"))));
                    break;
                }

                // 游戏ID过长
                if (playerName.length() > Main.instance.getConfig().getInt("bindSettings.bindNameMaxLength")) {
                    messageBuilder.text(i18n("messages.bind.bindNameMaxLength")
                            .replace("{length}", String.valueOf(Main.instance.getConfig().getInt("bindSettings.bindNameMaxLength"))));
                    break;
                }

                // 验证码错误
                if (Main.instance.getConfig().getBoolean("bindSettings.verify")) {
                    PlayerVerify playerVerify = DatabaseUtil.getPlayerVerify(playerName);
                    if (args.length < 3 || playerVerify == null || !playerVerify.verify().equals(args[2])) {
                        messageBuilder.text(i18n("messages.bind.verifyCodeError"));
                        break;
                    }
                }

                // 超出绑定数量上线
                if (!Main.instance.getConfig().getStringList("bindSettings.bypassMaxBindList").contains(String.valueOf(event.getSender().getUserId())) &&
                        DatabaseUtil.getQqBindList(event.getSender().getUserId()).size() > Main.instance.getConfig().getInt("bindSettings.maxBind")) {
                    messageBuilder.text(i18n("messages.bind.maxBind")
                            .replace("{count}", String.valueOf(Main.instance.getConfig().getInt("bindSettings.maxBind"))));
                    break;
                }

                PlayerData playerData = DatabaseUtil.getPlayerData(playerName);

                // 目标游戏ID已经被绑定了
                if (playerData != null) {
                    messageBuilder.text(i18n("messages.bind.alwaysBind")
                            .replace("{player}", playerName)
                            .replace("{qq}", String.valueOf(playerData.getQQ())));
                    break;
                }

                playerData = new PlayerData(playerName, event.getSender().getUserId());

                // 游戏群昵称
                if (Main.instance.getConfig().getBoolean("bindSettings.changeName")) {
                    if (event.getSender().getRole() == RoleType.MEMBER || Main.instance.getConfig().getBoolean("bindSettings.changeNameIgnoreAdmin")) {
                        MHDFBot.setGroupCard(event.getGroupId(), event.getSender().getUserId(), playerName);
                    }
                }

                DatabaseUtil.bind(playerData);
                messageBuilder.text(i18n("messages.bind.done")
                        .replace("{player}", playerName)
                        .replace("{qq}", String.valueOf(event.getSender().getUserId())));
            }
            case "#解除绑定" -> {
                // 命令帮助
                if (args.length == 1) {
                    messageBuilder.text(i18n("messages.unBind.usage"));
                    break;
                }

                String playerName = args[1];
                PlayerData playerData = DatabaseUtil.getPlayerData(playerName);

                // 目标游戏ID并没有绑定
                if (playerData == null) {
                    messageBuilder.text(i18n("messages.unBind.noBind")
                            .replace("{player}", playerName));
                    break;
                }

                // 目标游戏ID是被其他人绑定的
                if (!Objects.equals(playerData.getQQ(), event.getSender().getUserId())) {
                    messageBuilder.text(i18n("messages.unBind.noBind")
                            .replace("{player}", playerName));
                    break;
                }

                DatabaseUtil.unbind(playerData);
                messageBuilder.text(i18n("messages.unBind.done")
                        .replace("{player}", playerName)
                        .replace("{qq}", String.valueOf(event.getSender().getUserId())));
            }
            case "#重置密码" -> {
                // 命令帮助
                if (args.length == 1) {
                    messageBuilder.text(i18n("messages.changePassword.usage"));
                    break;
                }

                String playerName = args[1];
                PlayerData playerData = DatabaseUtil.getPlayerData(playerName);

                // 目标游戏ID并没有绑定
                if (playerData == null) {
                    messageBuilder.text(i18n("messages.changePassword.noBind")
                            .replace("{player}", playerName));
                    break;
                }

                // 目标游戏ID是被其他人绑定的
                if (!Objects.equals(playerData.getQQ(), event.getSender().getUserId())) {
                    messageBuilder.text(i18n("messages.changePassword.noBind")
                            .replace("{player}", playerName));
                    break;
                }

                ChangePasswordUtil.getChangePasswordHashMap().put(event.getSender().getUserId(), playerName);
                messageBuilder.text(i18n("messages.changePassword.startChange"));
            }
            default -> {
                return;
            }
        }
        MHDFBot.sendGroupMsg(event.getGroupId(), messageBuilder.build());
    }
}
