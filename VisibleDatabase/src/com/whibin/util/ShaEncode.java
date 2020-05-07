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
            sha = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
        byte[] bytes = sha.digest(password.getBytes());
        StringBuilder builder = new StringBuilder();
        for (byte aByte : bytes) {
            int val = ((int) aByte) & 0xff;
            if (val < 16) {
                builder.append("0");
            }
            builder.append(Integer.toHexString(val));
        }
        return builder.toString();
    }
}
