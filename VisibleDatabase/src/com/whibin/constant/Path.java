package com.whibin.constant;

import com.whibin.util.jdbc.PropertiesUtil;

/**
 * @author whibin
 * @date 2020/5/8 16:12
 * @Description: 用户数据的存储路径
 */

public class Path {
    static {
        // 读入路径
        PATH = PropertiesUtil.getValue("path");
    }
    public static String PATH;
}
