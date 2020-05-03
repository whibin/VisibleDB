package com.whibin.util.jdbc;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author whibin
 * @date 2020/4/16 12:17
 * @Description: 封装JDBC工具类
 */

public class JDBCUtil<T> {
    /**
     * 用于映射的Map
     * <数据库字段, 属性字段>
     */
    private static Map<String, String> MAPPER = new HashMap<>();

    /**
     * dao对应的表的实体类
     */
    private Class<T> clz;

    /**
     * 将映射的Map构建起来
     * @param clz
     * @param modify
     */
    public JDBCUtil(Class<T> clz, Map<String, String> modify) {
        this.clz = clz;
        for (Field field : clz.getDeclaredFields()) {
            MAPPER.put(field.getName(), field.getName());
        }
        if (modify != null && modify.size() > 0) {
            for (Map.Entry<String, String> entry : modify.entrySet()) {
                MAPPER.remove(entry.getKey());
                MAPPER.put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * 获取连接池
     */
    private static ConnectionPool pool = ConnectionPool.getConnectionPool();

    /**
     * 增删改操作
     * @param sql
     * @param args
     * @return 返回操作影响的行数
     */
    public int update(String sql, Object... args) {
        Connection connection = pool.getConnection();
        PreparedStatement statement = null;
        int i = 0;
        try {
            statement = connection.prepareStatement(sql);
            // 判断是否传参，若参数为0，则说明没有参数，不需设置
            setArguments(statement, args);
            i = statement.executeUpdate();
        } catch (SQLException e) {
            // 出现异常应该将异常情况打印，并终止
            e.printStackTrace();
            throw new RuntimeException();
        } finally {
            close(null, statement, connection);
        }
        return i;
    }

    /**
     * 查询返回单个对象
     * @param sql
     * @param args
     * @return 返回要查询的对象实体；返回null，说明查询不到
     */
    public T queryForSingle(String sql, Object... args) {
        Connection connection = pool.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        T singleObject = null;
        try {
            statement = connection.prepareStatement(sql);
            setArguments(statement, args);
            set = statement.executeQuery();
            // 若查询到结果集
            // 先获取ResultSetMetaData对象，用于获取字段名称
            ResultSetMetaData data = set.getMetaData();
            if (set.next()) {
                singleObject = newObject(data, set);
            }
        } catch (SQLException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
            throw new RuntimeException();
        } finally {
            close(set, statement, connection);
        }
        return singleObject;
    }

    /**
     * 批量查询
     * @param sql
     * @param args
     * @return 返回装载对象实体的集合；若返回为null或长度<=0，说明查询不到
     */
    public List<T> queryForList(String sql, Object... args) {
        List<T> list = new ArrayList<>();
        Connection connection = pool.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        T singleObject = null;
        try {
            statement = connection.prepareStatement(sql);
            setArguments(statement, args);
            set = statement.executeQuery();
            // 若查询到结果集
            // 先获取ResultSetMetaData对象，用于获取字段名称
            ResultSetMetaData data = set.getMetaData();
            while (set.next()) {
                singleObject = newObject(data, set);
                list.add(singleObject);
            }
        } catch (SQLException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
            throw new RuntimeException();
        } finally {
            close(set, statement, connection);
        }
        return list;
    }

    /**
     * 用于聚合函数的查询
     * @param sql
     * @param args
     * @return
     */
    public T queryForObject(String sql, Object... args) {
        Connection connection = pool.getConnection();
        PreparedStatement statement = null;
        ResultSet set = null;
        T object = null;
        try {
            statement = connection.prepareStatement(sql);
            setArguments(statement,args);
            set = statement.executeQuery();
            if (set.next()) {
                object = (T) set.getObject(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        } finally {
            close(set,statement,connection);
        }
        return object;
    }

    /**
     * 释放statement，connection资源
     * @param statement
     * @param connection
     */
    private void close(ResultSet set, Statement statement, Connection connection) {
        if (set != null) {
            try {
                set.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        pool.close(connection);
    }

    /**
     * 设置sql语句的参数
     * @param statement
     * @param args
     * @throws SQLException
     */
    private void setArguments(PreparedStatement statement, Object... args) throws SQLException {
        if (args != null && args.length > 0) {
            int index = 1;
            for (Object object : args) {
                statement.setObject(index++, object);
            }
        }
    }

    /**
     * 获取要单个查询的对象的数据
     * @param data
     * @param set
     * @return
     * @throws SQLException
     * @throws NoSuchMethodException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    private T newObject(ResultSetMetaData data, ResultSet set) throws SQLException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // 利用反射获得构造器，new出要的对象
        T singleObject = clz.getConstructor().newInstance();
        for (int i = 0; i < data.getColumnCount(); i++) {
            // 根据字段对对象中的属性赋值
            // 获取查询到的数据库字段名
            String key = data.getColumnName(i + 1);
            // 根据数据库字段名获取值
            Object value = set.getObject(key);
            // 根据数据库字段名映射的属性字段调用set方法进行赋值
            if (value != null) {
                clz.getMethod(methodName(MAPPER.get(key)),value.getClass()).invoke(singleObject,value);
            }
        }
        return singleObject;
    }

    /**
     * 根据属性字段拼接出set方法名称
     * @param name
     * @return
     */
    private String methodName(String name) {
        StringBuilder builder = new StringBuilder();
        builder.append("set");
        char[] temp = name.toCharArray();
        temp[0] -= 32;
        builder.append(String.valueOf(temp));
        return builder.toString();
    }
}
