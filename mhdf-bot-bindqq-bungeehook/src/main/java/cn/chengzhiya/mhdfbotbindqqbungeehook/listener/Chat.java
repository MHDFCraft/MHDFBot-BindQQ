package cn.chengzhiya.mhdfbotbindqqbungeehook.listener;

import cn.chengzhiya.mhdfbotbindqqbungeehook.entity.PlayerVerify;
import cn.chengzhiya.mhdfbotbindqqbungeehook.util.ConfigUtil;
import cn.chengzhiya.mhdfbotbindqqbungeehook.util.DatabaseUtil;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import static cn.chengzhiya.mhdfbotbindqqbungeehook.util.message.MessageUtil.colorMessage;

public final class Chat implements Listener {
    @EventHandler
    public void onChat(ChatEvent event) {
        if (event.isCancelled()) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) event.getSender();

        if (DatabaseUtil.ifPlayerDataExist(player.getName())) {
            return;
        }

        PlayerVerify playerVerify = DatabaseUtil.getPlayerVerify(player.getName());
        if (event.isCommand()) {
            event.setCancelled(!(ConfigUtil.getConfig().getStringList("allowUseCommandList").contains(event.getMessage().split(" ")[0])));
            if (event.isCancelled()) {
                player.sendMessage(new TextComponent(
                        colorMessage(ConfigUtil.getConfig().getString("messages.antiUseCommand"))
                                .replace("{player}", player.getName())
                                .replace("{verify}", playerVerify != null ? playerVerify.verify() : "")
                ));
            }
            return;
        }

        event.setCancelled(!(ConfigUtil.getConfig().getBoolean("allowChat")));
        if (event.isCancelled()) {
            player.sendMessage(new TextComponent(
                    colorMessage(ConfigUtil.getConfig().getString("messages.antiChat"))
                            .replace("{player}", player.getName())
                            .replace("{verify}", playerVerify != null ? playerVerify.verify() : "")
            ));
        }
    }
}

