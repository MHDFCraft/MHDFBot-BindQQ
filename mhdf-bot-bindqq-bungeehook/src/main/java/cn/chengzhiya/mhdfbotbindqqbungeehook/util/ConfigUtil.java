package cn.chengzhiya.mhdfbotbindqqbungeehook.util;

import cn.chengzhiya.mhdfbotbindqqbungeehook.Main;
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

    /**
     * 保存初始配置文件
     */
    public static void saveDefaultConfig() {
        if (!Main.instance.getDataFolder().exists()) {
            Main.instance.getDataFolder().mkdir();
        }

        File configFile = new File(Main.instance.getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            try (InputStream in = Main.instance.getResourceAsStream("config.yml")) {
                Files.copy(in, configFile.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 重新加载配置文件
     */
    public static void reloadConfig() {
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(Main.instance.getDataFolder(), "config.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
