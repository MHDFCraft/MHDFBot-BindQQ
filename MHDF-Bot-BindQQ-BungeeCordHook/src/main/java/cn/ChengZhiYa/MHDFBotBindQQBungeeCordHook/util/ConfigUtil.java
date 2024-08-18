package cn.ChengZhiYa.MHDFBotBindQQBungeeCordHook.util;

import cn.ChengZhiYa.MHDFBotBindQQBungeeCordHook.main;
import lombok.Getter;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public final class ConfigUtil {
    @Getter
    public static Configuration config;

    public static void saveDefaultConfig() {
        if (!main.instance.getDataFolder().exists()) {
            main.instance.getDataFolder().mkdir();
        }

        File ConfigFile = new File(main.instance.getDataFolder(), "config.yml");

        if (!ConfigFile.exists()) {
            try (InputStream in = main.instance.getResourceAsStream("config.yml")) {
                Files.copy(in, ConfigFile.toPath());
            } catch (IOException ignored) {
            }
        }
    }

    public static void reloadConfig() {
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(main.instance.getDataFolder(), "config.yml"));
        } catch (IOException ignored) {
        }
    }
}
