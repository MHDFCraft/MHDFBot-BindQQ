package cn.chengzhiya.mhdfbotbindqqbukkithook.hook;

import cn.chengzhiya.mhdfbotbindqqbukkithook.Main;
import cn.chengzhiya.mhdfbotbindqqbukkithook.util.DatabaseUtil;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.chengzhiya.mhdfbotbindqqbukkithook.util.message.MessageUtil.colorMessage;

public final class PlaceholderAPI extends PlaceholderExpansion {

    @Override
    public @NotNull String getAuthor() {
        return "ChengZhiYa";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "BindQQ";
    }

    @Override
    public @NotNull String getVersion() {
        return Main.instance.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("isbind")) {
            return DatabaseUtil.ifPlayerDataExist(player.getName()) ? colorMessage(Main.instance.getConfig().getString("messages.bind")) :
                    colorMessage(Main.instance.getConfig().getString("messages.noBind"));
        }
        if (params.equalsIgnoreCase("getqq")) {
            return DatabaseUtil.ifPlayerDataExist(player.getName()) ? String.valueOf(Objects.requireNonNull(DatabaseUtil.getPlayerData(player.getName())).getQQ()) :
                    colorMessage(Main.instance.getConfig().getString("messages.noBind"));
        }
        if (params.equalsIgnoreCase("getchattimes")) {
            return DatabaseUtil.ifPlayerDataExist(player.getName()) ? String.valueOf(Objects.requireNonNull(DatabaseUtil.getPlayerData(player.getName())).getChatTimes()) :
                    colorMessage(Main.instance.getConfig().getString("messages.noBind"));
        }
        if (params.equalsIgnoreCase("getdaychattimes")) {
            return DatabaseUtil.ifPlayerDataExist(player.getName()) ? String.valueOf(Objects.requireNonNull(DatabaseUtil.getPlayerData(player.getName())).getDayChatTimes()) :
                    colorMessage(Main.instance.getConfig().getString("messages.noBind"));
        }
        if (params.equalsIgnoreCase("getVerifyCode")) {
            return DatabaseUtil.getPlayerVerify(player.getName()) != null ? DatabaseUtil.getPlayerVerify(player.getName()).verify() : "";
        }
        return null;
    }
}
