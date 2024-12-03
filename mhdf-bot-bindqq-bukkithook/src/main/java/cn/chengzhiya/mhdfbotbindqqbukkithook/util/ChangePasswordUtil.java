package cn.chengzhiya.mhdfbotbindqqbukkithook.util;

import fr.xephi.authme.api.v3.AuthMeApi;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;

public final class ChangePasswordUtil {
    @Getter
    static HashMap<Long, String> changePasswordHashMap = new HashMap<>();

    /**
     * 修改指定游戏ID的账户密码
     *
     * @param username 目标游戏ID
     * @param password 要修改的密码
     */
    public static void changePassword(String username, String password) {
        // AuthMe API实现
        if (Bukkit.getPluginManager().getPlugin("AuthMe") != null) {
            AuthMeApi authmeApi = AuthMeApi.getInstance();
            authmeApi.changePassword(username, password);
        }

        // 梦之登录 反射实现
        if (Bukkit.getPluginManager().getPlugin("MHDF-Login") != null) {
            try {
                Class<?> mhdfLoginUtil = Class.forName("cn.chengzhiya.mhdflogin.Util");
                Method changePasswordMethod = mhdfLoginUtil.getDeclaredMethod("changePassword", String.class, String.class);
                changePasswordMethod.invoke(null, username, sha256(password));
            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * sha256处理
     *
     * @param string 输入字符串
     * @return sha256字符串
     */
    private static String sha256(String string) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(string.getBytes(StandardCharsets.UTF_8));
            StringBuilder stringBuffer = new StringBuilder();
            for (byte aByte : messageDigest.digest()) {
                String temp = Integer.toHexString(aByte & 0xFF);
                if (temp.length() == 1) {
                    stringBuffer.append("0");
                }
                stringBuffer.append(temp);
            }
            return stringBuffer.toString().toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
