package com.whibin.service.impl;

import com.whibin.domain.po.User;
import com.whibin.domain.vo.Database;
import com.whibin.domain.vo.Table;
import com.whibin.domain.vo.UserSql;
import com.whibin.service.SqlService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

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
        newTable(tableMap,request);
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

    @Override
    public void updateTable(HttpServletRequest request) {
        UserSql userSql = (UserSql) request.getSession().getAttribute("userSql");
        Database database = userSql.getDatabaseMap().get(request.getParameter("databaseName"));
        Map<String, Table> tableMap = database.getTableMap();
        String oldTableName = request.getParameter("oldTableName");
        tableMap.remove(oldTableName);
        newTable(tableMap,request);
    }

    @Override
    public void addData(HttpServletRequest request) {
        UserSql userSql = (UserSql) request.getSession().getAttribute("userSql");
        String databaseName = null;
        String tableName = null;
        for (Cookie cookie : request.getCookies()) {
            if ("databaseName".equals(cookie.getName())) {
                databaseName = cookie.getValue();
                System.out.println(databaseName);
            }
            if ("tableName".equals(cookie.getName())) {
                tableName = cookie.getValue();
                System.out.println(tableName);
            }
        }
        Table table = userSql.getDatabaseMap().get(databaseName).getTableMap().get(tableName);
        String[] data = request.getParameterMap().get("data");
        addData(table,data);
    }

    @Override
    public void deleteData(HttpServletRequest request) {
        UserSql userSql = (UserSql) request.getSession().getAttribute("userSql");
        Table table = userSql.getDatabaseMap().get(request.getParameter("databaseName"))
                .getTableMap().get(request.getParameter("tableName"));
        String id = request.getParameter("id");
        deleteData(table,id);
    }

    @Override
    public void updateData(HttpServletRequest request) {
        // 先删除
        UserSql userSql = (UserSql) request.getSession().getAttribute("userSql");
        Table table = userSql.getDatabaseMap().get(request.getParameter("databaseName"))
                .getTableMap().get(request.getParameter("tableName"));
        String id = request.getParameter("id");
        deleteData(table,id);
        // 后添加
        List<String> newData = new ArrayList<>();
        for (String datum : request.getParameterMap().get("data")) {
            if (datum == null || "".equals(datum) || "undefined".equals(datum)) {
                continue;
            }
            newData.add(datum);
        }
        // 如果集合中的数量为0，说明此时不需要再添加表数据
        if (newData.size() <= 0) {
            return;
        }
        addData(table, (String[]) newData.toArray(new String[newData.size()]));
        System.out.println(newData);
    }

    /**
     * 创建一个新的表
     * @param tableMap
     * @param request
     */
    private void newTable(Map<String, Table> tableMap, HttpServletRequest request) {
        Table table = new Table();
        tableMap.put(request.getParameter("tableName"),table);
        Map<String, String> fieldType = new HashMap<>();
        table.setFieldType(fieldType);
        // 获取字段名称和类型
        Map<String, String[]> parameterMap = request.getParameterMap();
        String[] fields = parameterMap.get("field");
        String[] dataTypes = parameterMap.get("dataType");
        for (int i = 0; i < fields.length; i++) {
            if (fields[i] == null || "".equals(fields[i])) {
                continue;
            }
            fieldType.put(fields[i],dataTypes[i]);
        }
    }

    /**
     * 删除表中指定的数据
     * @param table
     * @param id
     */
    private void deleteData(Table table, String id) {
        Map<String, String> fieldType = table.getFieldType();
        Map<String, List<String>> data = table.getData();
        for (Map.Entry<String, String> entry : fieldType.entrySet()) {
            List<String> list = data.get(entry.getKey());
            list.remove(Integer.parseInt(id));
        }
    }

    /**
     * 向表中添加指定数据
     * @param table
     * @param data
     */
    private void addData(Table table, String[] data) {
        Map<String, List<String>> tableData = table.getData();
        // 判断tableData是否存在，若不存在则新建
        if (tableData == null) {
            tableData = new HashMap<>();
            // 为数据设置字段名
            for (Map.Entry<String, String> entry : table.getFieldType().entrySet()) {
                tableData.put(entry.getKey(),new ArrayList<>());
            }
            table.setData(tableData);
        }
        // 若存在，则直接操作
        int i = 0;
        for (Map.Entry<String, String> entry : table.getFieldType().entrySet()) {
            tableData.get(entry.getKey()).add(data[i++]);
        }
    }
}
