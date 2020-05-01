package com.whibin.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 读取配置文件工具类
 * @author whibin
 */
public class PropertiesUtil {
    private static Properties config = new Properties();

    /*
     * 使用静态初始化块加载src目录下的配置文件
     * 作用：达到只加载一次，并且优先加载的效果
     */
    static {
        try {
            config.load(new BufferedInputStream(PropertiesUtil.class.getClassLoader().
                                            getResourceAsStream("datasource.properties")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取配置文件的内容
     * @param name 需要从配置文件读取的参数
     * @return 参数对应的值
     */
    public static String getValue(String name) {
        return config.getProperty(name);
    }
}
