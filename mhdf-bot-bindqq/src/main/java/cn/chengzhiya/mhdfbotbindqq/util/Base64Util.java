package cn.chengzhiya.mhdfbotbindqq.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class Base64Util {
    /**
     * base64解码
     *
     * @return 原数据
     */
    public static byte[] decode(byte[] bytes) {
        return Base64.getDecoder().decode(bytes);
    }

    /**
     * base64解码
     *
     * @return 原数据
     */
    public static byte[] decode(String string) {
        return decode(string.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * base64编码
     *
     * @return base64字符串
     */
    public static String encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * base64编码
     *
     * @return base64字符串
     */
    public static String encode(String string) {
        return encode(string.getBytes(StandardCharsets.UTF_8));
    }
}
