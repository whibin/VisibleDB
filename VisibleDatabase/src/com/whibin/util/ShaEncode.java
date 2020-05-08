package com.whibin.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author whibin
 * @date 2020/5/7 19:54
 * @Description: 使用SHA算法进行密码加密
 */

public class ShaEncode {
    public static String shaEncode(String password) {
        MessageDigest sha;
        try {
            // 创建实例
            sha = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            // 若出现异常返回空字符串
            e.printStackTrace();
            return "";
        }
        byte[] bytes = sha.digest(password.getBytes());
        StringBuilder builder = new StringBuilder();
        // 遍历字节数组
        for (byte aByte : bytes) {
            int val = ((int) aByte) & 0xff;
            if (val < 16) {
                // 进行补位
                builder.append("0");
            }
            builder.append(Integer.toHexString(val));
        }
        return builder.toString();
    }
}
