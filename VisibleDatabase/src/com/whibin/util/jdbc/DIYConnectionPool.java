package com.whibin.util.jdbc;

import java.sql.Connection;

/**
 * 连接池接口
 * @author whibin
 */
public interface DIYConnectionPool {
    /**
     * 获取连接对象
     * @return 从连接池中获取数据库连接对象
     */
    Connection getConnection();

    /**
     * 释放连接
     * @param connection 释放数据库连接对象的资源
     */
    void close(Connection connection);
}
