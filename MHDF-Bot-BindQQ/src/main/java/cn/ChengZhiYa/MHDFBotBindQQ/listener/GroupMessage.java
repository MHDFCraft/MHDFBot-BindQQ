package cn.ChengZhiYa.MHDFBotBindQQ.listener;

import cn.ChengZhiYa.MHDFBot.api.MHDFBot;
import cn.ChengZhiYa.MHDFBot.api.builder.MessageBuilder;
import cn.ChengZhiYa.MHDFBot.api.enums.user.RoleType;
import cn.ChengZhiYa.MHDFBot.api.interfaces.EventHandler;
import cn.ChengZhiYa.MHDFBot.api.manager.Listener;
import cn.ChengZhiYa.MHDFBot.entity.user.Member;
import cn.ChengZhiYa.MHDFBot.event.message.GroupMessageEvent;
import cn.ChengZhiYa.MHDFBotBindQQ.entity.PlayerData;
import cn.ChengZhiYa.MHDFBotBindQQ.main;
import cn.ChengZhiYa.MHDFBotBindQQ.util.ChangePasswordUtil;
import cn.ChengZhiYa.MHDFBotBindQQ.util.DatabaseUtil;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFBotBindQQ.util.LangUtil.i18n;

public final class GroupMessage implements Listener {
    @EventHandler
    public void onGroupMessage(GroupMessageEvent event) {
        if (main.instance.getConfig().getStringList("AllowUseGroup").contains(String.valueOf(event.getGroupId()))) {
            if (event.getMessage().startsWith("#")) {
                String[] args = event.getMessage().split(" ");
                MessageBuilder messageBuilder = MessageBuilder.builder();
                if (main.instance.getConfig().getBoolean("MessageSettings.Reply")) {
                    messageBuilder.reply(event.getMessageId());
                }
                if (main.instance.getConfig().getBoolean("MessageSettings.At")) {
                    messageBuilder.at(event.getSender().getUserId()).text("\n");
                }
                switch (args[0]) {
                    case "#绑定" -> {
                        if (args.length > 1) {
                            Member member = MHDFBot.getGroupMemberInfo(event.getGroupId(), event.getSender().getUserId());
                            String playerName = args[1];
                            if (member.getLevel() >= main.instance.getConfig().getInt("BindSettings.MinQQLevel")) {
                                if (playerName.matches(main.instance.getConfig().getString("BindSettings.BindNameRegex"))) {
                                    if (playerName.length() >= main.instance.getConfig().getInt("BindSettings.BindNameMinLength")) {
                                        if (playerName.length() <= main.instance.getConfig().getInt("BindSettings.BindNameMaxLength")) {
                                            if (main.instance.getConfig().getBoolean("BindSettings.Verify")) {
                                                if (args[2] == null || DatabaseUtil.getPlayerVerify(args[1]) == null || !Objects.requireNonNull(DatabaseUtil.getPlayerVerify(args[1])).getVerify().equals(args[2])) {
                                                    messageBuilder.text(i18n("Messages.Bind.VerifyCodeError"));
                                                    MHDFBot.sendMessage(event, messageBuilder.build());
                                                    return;
                                                }
                                            }
                                            if (DatabaseUtil.getQqBindList(event.getSender().getUserId()).size() < main.instance.getConfig().getInt("BindSettings.MaxBind") ||
                                                    main.instance.getConfig().getStringList("BindSettings.BypassMaxBindList").contains(String.valueOf(event.getSender().getUserId()))) {
                                                PlayerData playerData = DatabaseUtil.getPlayerData(playerName);
                                                if (playerData == null) {
                                                    playerData = new PlayerData(playerName, event.getSender().getUserId());
                                                    if (main.instance.getConfig().getBoolean("BindSettings.ChangeName")) {
                                                        if (event.getSender().getRole() == RoleType.MEMBER || !main.instance.getConfig().getBoolean("BindSettings.ChangeNameIgnoreAdmin")) {
                                                            MHDFBot.setGroupCard(event.getGroupId(), event.getSender().getUserId(), playerName);
                                                        }
                                                    }
                                                    DatabaseUtil.bind(playerData);
                                                    messageBuilder.text(i18n("Messages.Bind.BindDone").replaceAll("\\{Player}", playerName).replaceAll("\\{QQ}", String.valueOf(event.getSender().getUserId())));
                                                } else {
                                                    messageBuilder.text(i18n("Messages.Bind.AlwaysBind").replaceAll("\\{Player}", playerName).replaceAll("\\{QQ}", String.valueOf(playerData.getQQ())));
                                                }
                                            } else {
                                                messageBuilder.text(i18n("Messages.Bind.MaxBind").replaceAll("\\{Size}", String.valueOf(main.instance.getConfig().getInt("BindSettings.MaxBind"))));
                                            }
                                        } else {
                                            messageBuilder.text(i18n("Messages.Bind.BindNameMaxLength").replaceAll("\\{Length}", String.valueOf(main.instance.getConfig().getInt("BindSettings.BindNameMaxLength"))));
                                        }
                                    } else {
                                        messageBuilder.text(i18n("Messages.Bind.BindNameMinLength").replaceAll("\\{Length}", String.valueOf(main.instance.getConfig().getInt("BindSettings.BindNameMinLength"))));
                                    }
                                } else {
                                    messageBuilder.text(i18n("Messages.Bind.BindNameRegex"));
                                }
                            } else {
                                messageBuilder.text(i18n("Messages.Bind.MinQQLevel").replaceAll("\\{Level}", String.valueOf(main.instance.getConfig().getInt("BindSettings.MinQQLevel"))));
                            }
                        } else {
                            messageBuilder.text(i18n("Messages.Bind.Usage"));
                        }
                        MHDFBot.sendMessage(event, messageBuilder.build());
                    }
                    case "#解除绑定" -> {
                        if (args.length == 2) {
                            String playerName = args[1];
                            PlayerData playerData = DatabaseUtil.getPlayerData(playerName);
                            if (playerData != null) {
                                if (Objects.equals(playerData.getQQ(), event.getSender().getUserId())) {
                                    DatabaseUtil.unbind(playerData);
                                    messageBuilder.text(i18n("Messages.UnBind.UnBindDone").replaceAll("\\{Player}", playerName).replaceAll("\\{QQ}", String.valueOf(event.getSender().getUserId())));
                                } else {
                                    messageBuilder.text(i18n("Messages.UnBind.DontUnBind").replaceAll("\\{Player}", playerName));
                                }
                            } else {
                                messageBuilder.text(i18n("Messages.UnBind.DontUnBind").replaceAll("\\{Player}", playerName));
                            }
                        } else {
                            messageBuilder.text(i18n("Messages.UnBind.Usage"));
                        }
                        MHDFBot.sendMessage(event, messageBuilder.build());
                    }
                    case "#重置密码" -> {
                        if (args.length == 2) {
                            String playerName = args[1];
                            PlayerData playerData = DatabaseUtil.getPlayerData(playerName);
                            if (playerData != null) {
                                if (Objects.equals(playerData.getQQ(), event.getSender().getUserId())) {
                                    ChangePasswordUtil.getChangePasswordHashMap().put(event.getSender().getUserId(), playerName);
                                    messageBuilder.text(i18n("Messages.ChangePassword.ChangeInfo"));
                                } else {
                                    messageBuilder.text(i18n("Messages.ChangePassword.DontBind").replaceAll("\\{Player}", playerName).replaceAll("\\{QQ}", String.valueOf(playerData.getQQ())));
                                }
                            } else {
                                messageBuilder.text(i18n("Messages.ChangePassword.DontBind").replaceAll("\\{Player}", playerName));
                            }
                        } else {
                            messageBuilder.text(i18n("Messages.ChangePassword.Usage"));
                        }
                        MHDFBot.sendMessage(event, messageBuilder.build());
                    }
                    default -> {
                    }
                }
            }
        }
    }
}
