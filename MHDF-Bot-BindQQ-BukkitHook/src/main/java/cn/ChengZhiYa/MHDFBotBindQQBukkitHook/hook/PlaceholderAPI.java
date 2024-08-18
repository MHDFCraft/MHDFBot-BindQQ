package cn.ChengZhiYa.MHDFBotBindQQBukkitHook.hook;

import cn.ChengZhiYa.MHDFBotBindQQBukkitHook.main;
import cn.ChengZhiYa.MHDFBotBindQQBukkitHook.util.DatabaseUtil;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFBotBindQQBukkitHook.util.message.MessageUtil.colorMessage;

public final class PlaceholderAPI extends PlaceholderExpansion {

    public PlaceholderAPI() {
    }

    public static void registerPlaceholders() {
        new PlaceholderAPI().register();
    }

    public static void unregisterPlaceholders() {
        new PlaceholderAPI().unregister();
    }

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
        return main.getDescriptionFile().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equalsIgnoreCase("isbind")) {
            return DatabaseUtil.ifPlayerDataExist(player.getName()) ? colorMessage(main.instance.getConfig().getString("Messages.Bind")) :
                    colorMessage(main.instance.getConfig().getString("Messages.NotBind"));
        }
        if (params.equalsIgnoreCase("getqq")) {
            return DatabaseUtil.ifPlayerDataExist(player.getName()) ? String.valueOf(Objects.requireNonNull(DatabaseUtil.getPlayerData(player.getName())).getQQ()) :
                    colorMessage(main.instance.getConfig().getString("Messages.NotBind"));
        }
        if (params.equalsIgnoreCase("getchattimes")) {
            return DatabaseUtil.ifPlayerDataExist(player.getName()) ? String.valueOf(Objects.requireNonNull(DatabaseUtil.getPlayerData(player.getName())).getChatTimes()) :
                    colorMessage(main.instance.getConfig().getString("Messages.NotBind"));
        }
        if (params.equalsIgnoreCase("getdaychattimes")) {
            return DatabaseUtil.ifPlayerDataExist(player.getName()) ? String.valueOf(Objects.requireNonNull(DatabaseUtil.getPlayerData(player.getName())).getDayChatTimes()) :
                    colorMessage(main.instance.getConfig().getString("Messages.NotBind"));
        }
        if (params.equalsIgnoreCase("getVerifyCode")) {
            return DatabaseUtil.getPlayerVerify(player.getName()) != null ? DatabaseUtil.getPlayerVerify(player.getName()).getVerify() : "";
        }
        return null;
    }
}
