package com.whibin.util.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 加载驱动类
 * @author whibin
 */
public class LoadDriver {
    // 加载注册驱动
    static {
        try {
            Class.forName(PropertiesUtil.getValue("driverClassName"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取连接对象
     * @return 获取与数据库的连接对象
     * @throws SQLException SQL异常
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(PropertiesUtil.getValue("url"),
                PropertiesUtil.getValue("username"),PropertiesUtil.getValue("password"));
    }
}
