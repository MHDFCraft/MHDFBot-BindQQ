package cn.ChengZhiYa.MHDFBotBindQQBukkitHook.util;

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

    public static void changePassword(String userName, String password) {
        if (Bukkit.getPluginManager().getPlugin("AuthMe") != null) {
            AuthMeApi authmeApi = AuthMeApi.getInstance();
            authmeApi.changePassword(userName, password);
        }
        if (Bukkit.getPluginManager().getPlugin("MHDF-Login") != null) {
            try {
                Class<?> mhdfLoginUtil = Class.forName("cn.chengzhiya.mhdflogin.Util");
                Method changePasswordMethod = mhdfLoginUtil.getDeclaredMethod("changePassword", String.class, String.class);
                changePasswordMethod.invoke(null, userName, sha256(password));
            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static String sha256(String message) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(message.getBytes(StandardCharsets.UTF_8));
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
