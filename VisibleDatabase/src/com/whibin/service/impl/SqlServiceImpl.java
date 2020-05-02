package com.whibin.service.impl;

import com.whibin.domain.po.User;
import com.whibin.domain.vo.Database;
import com.whibin.domain.vo.Table;
import com.whibin.domain.vo.UserSql;
import com.whibin.service.SqlService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author whibin
 * @date 2020/5/1 16:04
 * @Description: SqlService的实现类
 */

public class SqlServiceImpl implements SqlService {
    @Override
    public void createDatabase(HttpServletRequest request) {
        HttpSession session = request.getSession();
        // 获取数据库的名字
        String name = request.getParameter("name");
        // 创建database对象
        Database database = new Database();
        Object newUserSql = session.getAttribute("userSql");
        if (newUserSql != null) {
            ((UserSql)newUserSql).getDatabaseMap().put(name,database);
            return;
        }
        UserSql userSql = new UserSql();
        // 设置user属性
        userSql.setUser((User) session.getAttribute("user"));
        // 创建map
        Map<String, Database> databaseMap = new HashMap<>();
        // 设置database的name
        databaseMap.put(name,database);
        userSql.setDatabaseMap(databaseMap);
        session.setAttribute("userSql",userSql);
    }

    @Override
    public void createTable(HttpServletRequest request) {
        // 获取userSql
        UserSql userSql = (UserSql) request.getSession().getAttribute("userSql");
        Database database = userSql.getDatabaseMap().get(request.getParameter("databaseName"));
        Map<String, Table> tableMap = database.getTableMap();
        if (tableMap == null) {
            tableMap = new HashMap<>();
            database.setTableMap(tableMap);
        }
        Table table = new Table();
        tableMap.put(request.getParameter("tableName"),table);
        Map<String, Class> fieldType = new HashMap<>();
        table.setFieldType(fieldType);
        // 获取字段名称和类型
        Map<String, String[]> parameterMap = request.getParameterMap();
        String[] fields = parameterMap.get("field");
        String[] dataTypes = parameterMap.get("dataType");
        for (int i = 0; i < fields.length; i++) {
            if (fields[i] == null || "".equals(fields[i])) {
                continue;
            }
            if ("bigint".equals(dataTypes[i])) {
                fieldType.put(fields[i],Long.class);
            }
            if ("int".equals(dataTypes[i]) || "mediumint".equals(dataTypes[i])) {
                fieldType.put(fields[i],Integer.class);
            }
            if ("char".equals(dataTypes[i]) || "varchar".equals(dataTypes[i])) {
                fieldType.put(fields[i],String.class);
            }
        }
    }

    @Override
    public void deleteDatabase(HttpServletRequest request) {
        // 获取数据库名称
        String databaseName = request.getParameter("name");
        // 删除该数据库
        UserSql userSql = (UserSql) request.getSession().getAttribute("userSql");
        userSql.getDatabaseMap().remove(databaseName);
    }

    @Override
    public void deleteTable(HttpServletRequest request) {
        // 获取数据库和表名
        String tableName = request.getParameter("tableName");
        String databaseName = request.getParameter("databaseName");
        UserSql userSql = (UserSql) request.getSession().getAttribute("userSql");
        // 删除表
        userSql.getDatabaseMap().get(databaseName).getTableMap().remove(tableName);
    }

    @Override
    public void updateDatabase(HttpServletRequest request) {
        // 获取新旧数据库的名称
        String oldName = request.getParameter("oldName");
        String newName = request.getParameter("newName");
        UserSql userSql = (UserSql) request.getSession().getAttribute("userSql");
        // 获取databaseMap
        Map<String, Database> databaseMap = userSql.getDatabaseMap();
        // 获取旧数据库
        Database database = databaseMap.get(oldName);
        // 将旧数据库名称从databaseMap删除
        databaseMap.remove(oldName);
        // 将新的数据库名称添加进去
        databaseMap.put(newName,database);
    }
}
