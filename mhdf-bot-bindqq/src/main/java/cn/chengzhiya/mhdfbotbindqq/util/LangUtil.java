package cn.chengzhiya.mhdfbotbindqq.util;

import cn.chengzhiya.mhdfbot.api.entity.config.YamlConfiguration;
import cn.chengzhiya.mhdfbotbindqq.Main;

import java.io.File;

public final class LangUtil {
    private static YamlConfiguration lang;

    /**
     * 保存初始语言文件
     */
    public static void saveDefaultLang() {
        Main.instance.saveResource("lang.yml", "lang.yml", false);
    }

    /**
     * 加载语言文件
     */
    public static void reloadLang() {
        lang = YamlConfiguration.loadConfiguration(new File(Main.instance.getDataFolder(), "lang.yml"));
    }

    /**
     * 获取指定key在语言文件中对应的文本
     */
    public static String i18n(String key) {
        if (lang == null) {
            reloadLang();
        }
        return lang.getString(key);
    }
}
