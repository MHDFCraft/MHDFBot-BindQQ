package cn.ChengZhiYa.MHDFBotBindQQBungeeCordHook.listener;

import cn.ChengZhiYa.MHDFBotBindQQBungeeCordHook.entity.PlayerVerify;
import cn.ChengZhiYa.MHDFBotBindQQBungeeCordHook.util.ConfigUtil;
import cn.ChengZhiYa.MHDFBotBindQQBungeeCordHook.util.DatabaseUtil;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import static cn.ChengZhiYa.MHDFBotBindQQBungeeCordHook.util.message.MessageUtil.colorMessage;

public final class Chat implements Listener {
    @EventHandler
    public void onChat(ChatEvent event) {
        if (!event.isCancelled()) {
            ProxiedPlayer player = (ProxiedPlayer) event.getSender();
            if (!DatabaseUtil.ifPlayerDataExist(player.getName())) {
                PlayerVerify playerVerify = DatabaseUtil.getPlayerVerify(player.getName());
                if (event.isCommand()) {
                    event.setCancelled(!(ConfigUtil.getConfig().getStringList("AllowUseCommandList").contains(event.getMessage().split(" ")[0])));
                    if (event.isCancelled()) {
                        player.sendMessage(new TextComponent(
                                colorMessage(ConfigUtil.getConfig().getString("Messages.AntiUseCommand"))
                                        .replaceAll("\\{Player}", player.getName())
                                        .replaceAll("\\{Verify}", playerVerify != null ? playerVerify.getVerify() : "")
                        ));
                    }
                } else {
                    event.setCancelled(!(ConfigUtil.getConfig().getBoolean("AllowChat")));
                    if (event.isCancelled()) {
                        player.sendMessage(new TextComponent(
                                colorMessage(ConfigUtil.getConfig().getString("Messages.AntiChat"))
                                        .replaceAll("\\{Player}", player.getName())
                                        .replaceAll("\\{Verify}", playerVerify != null ? playerVerify.getVerify() : "")
                        ));
                    }
                }
            }
        }
    }
}
