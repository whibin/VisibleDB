package com.whibin.util.jdbc;

import com.whibin.util.LoadDriver;
import com.whibin.util.PropertiesUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 数据库连接池
 * @author whibin
 */
public class ConnectionPool implements DIYConnectionPool {
    private List<Connection> freePool;
    private List<Connection> activePool;

    /**
     * 使用double-checked来提高多线程下的性能
     */
    private volatile static ConnectionPool connectionPool;
    public static ConnectionPool getConnectionPool() {
        if (connectionPool == null) {
            synchronized (ConnectionPool.class) {
                if (connectionPool == null) {
                    connectionPool = new ConnectionPool();
                }
            }
        }
        return connectionPool;
    }

    private ConnectionPool() {
        freePool = new CopyOnWriteArrayList<>();
        activePool = new CopyOnWriteArrayList<>();
        // 新建空闲连接池，把连接对象放入其内
        for (int i = 0; i < Integer.parseInt(PropertiesUtil.getValue("initialSize")); i++) {
            Connection connection = newConnection();
            if ( connection != null ) {
                freePool.add(connection);
            }
        }
    }

    /**
     * 获取连接对象
     * @return 从线程池中获取数据库连接对象
     */
    @Override
    public synchronized Connection getConnection() {
        Connection connection = null;
        if (activePool.size() < Integer.parseInt(PropertiesUtil.getValue("maxActive")) ) {
            if (freePool.size() > 0 ) {
                connection = freePool.remove(0);
            }
            else {  // 空池里没有，就新创建一个对象
                connection = newConnection();
            }
            if ( isAvailable(connection) ) {
                activePool.add(connection);
            }
            else {
                connection = getConnection();
            }
        }
        // 若已到达最大连接数，则让用户等待
        else {
            try {
                wait(Integer.parseInt(PropertiesUtil.getValue("maxWait")));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    @Override
    public synchronized void close(Connection connection) {
        if ( isAvailable(connection) ) {
            activePool.remove(connection);
            freePool.add(connection);
            this.notifyAll();
        }
        else {
            throw new RuntimeException("连接回收时出现异常");
        }
    }

    private Connection newConnection() {
        try {
            return LoadDriver.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isAvailable(Connection connection) {
        try {
            if ( connection == null || connection.isClosed() ) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
