package com.whibin.test;

import org.junit.Test;

import static com.whibin.util.ShaEncode.shaEncode;

/**
 * @author whibin
 * @date 2020/5/7 17:10
 * @Description: 测试sha加密算法
 */

public class ShaTest {
    /**
     * 加密解密
     */
    @Test
    public void encryptBASE64() {
        String s = shaEncode("123456");
        System.out.println(s);
    }
}
