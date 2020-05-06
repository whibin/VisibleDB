package com.whibin.service.impl;

import com.whibin.dao.UserDao;
import com.whibin.dao.impl.UserDaoImpl;
import com.whibin.domain.SqlType;
import com.whibin.domain.po.User;
import com.whibin.domain.vo.*;
import com.whibin.service.DatabaseCommonService;
import com.whibin.util.GetUserId;
import com.whibin.util.SqlParser;
import net.sf.jsqlparser.JSQLParserException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author whibin
 * @date 2020/5/5 16:29
 * @Description: DatabaseCommonService的实现类
 */

public class DatabaseCommonServiceImpl implements DatabaseCommonService {
    private UserDao dao = new UserDaoImpl();

    @Override
    public UserDatabase newUserDatabaseAndSetUser(HttpSession session, String id) {
        UserDatabase userDatabase = new UserDatabase();
        User user = (User) session.getAttribute("user" + id);
        if (user == null) {
            user = dao.get(Integer.parseInt(id));
            session.setAttribute("user"+id,user);
        }
        // 设置user属性
        userDatabase.setUser(user);
        return userDatabase;
    }

    @Override
    public void createTable(HttpServletRequest request, String userId) {
        // 获取userSql
        UserDatabase userDatabase = (UserDatabase) request.getSession().getAttribute("userDatabase"+ userId);
        Database database = userDatabase.getDatabaseMap().get(request.getParameter("databaseName"));
        Map<String, Table> tableMap = database.getTableMap();
        if (tableMap == null) {
            tableMap = new HashMap<>();
            database.setTableMap(tableMap);
        }
        newTable(tableMap,request);
        request.getSession().setAttribute("userDatabase"+userId,userDatabase);
    }

    @Override
    public void newTable(Map<String, Table> tableMap, HttpServletRequest request) {
        Table table = new Table();
        String name = request.getParameter("tableName");
        if (isNull(name)) {
            return;
        }
        name = name.toLowerCase();
        tableMap.put(name,table);
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

    @Override
    public Boolean isNull(String string) {
        return string == null || "".equals(string);
    }

    @Override
    public void deleteTable(HttpServletRequest request, String userId) {
        // 获取数据库和表名
        String tableName = request.getParameter("tableName");
        String databaseName = request.getParameter("databaseName");
        UserDatabase userDatabase = (UserDatabase) request.getSession().getAttribute("userDatabase"+userId);
        // 删除表
        userDatabase.getDatabaseMap().get(databaseName).getTableMap().remove(tableName);
        request.getSession().setAttribute("userDatabase"+userId,userDatabase);
    }

    @Override
    public void updateTable(HttpServletRequest request, String userId) {
        UserDatabase userDatabase = (UserDatabase) request.getSession().getAttribute("userDatabase"+userId);
        Database database = userDatabase.getDatabaseMap().get(request.getParameter("databaseName"));
        Map<String, Table> tableMap = database.getTableMap();
        String oldTableName = request.getParameter("oldTableName");
        tableMap.remove(oldTableName);
        newTable(tableMap,request);
        request.getSession().setAttribute("userDatabase"+userId,userDatabase);
    }

    @Override
    public void addData(HttpServletRequest request, String userId) {
        UserDatabase userDatabase = (UserDatabase) request.getSession().getAttribute("userDatabase"+userId);
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
        Table table = userDatabase.getDatabaseMap().get(databaseName).getTableMap().get(tableName);
        String[] data = request.getParameterMap().get("data");
        addData(table,data);
        request.getSession().setAttribute("userDatabase"+userId,userDatabase);
    }

    @Override
    public void addData(Table table, String[] data) {
        Map<String, List<String>> tableData = table.getData();
        // 判断tableData是否存在，若不存在则新建
        newTableDataWhenNull(table);
        tableData = table.getData();
        // 若存在，则直接操作
        int i = 0;
        for (Map.Entry<String, String> entry : table.getFieldType().entrySet()) {
            tableData.get(entry.getKey()).add(data[i++]);
        }
    }

    @Override
    public void newTableDataWhenNull(Table table) {
        Map<String, List<String>> tableData = table.getData();
        if (tableData == null) {
            tableData = new HashMap<>();
            // 为数据设置字段名
            for (Map.Entry<String, String> entry : table.getFieldType().entrySet()) {
                tableData.put(entry.getKey(),new ArrayList<>());
            }
            table.setData(tableData);
        }
    }

    @Override
    public void deleteData(HttpServletRequest request, String userId) {
        UserDatabase userDatabase = (UserDatabase) request.getSession().getAttribute("userDatabase"+userId);
        Table table = userDatabase.getDatabaseMap().get(request.getParameter("databaseName"))
                .getTableMap().get(request.getParameter("tableName"));
        String id = request.getParameter("id");
        // 传入表和数据所在的id（即行数）
        deleteData(table,id);
        request.getSession().setAttribute("userDatabase"+userId,userDatabase);
    }

    @Override
    public void deleteData(Table table, String id) {
        Map<String, String> fieldType = table.getFieldType();
        Map<String, List<String>> data = table.getData();
        for (Map.Entry<String, String> entry : fieldType.entrySet()) {
            List<String> list = data.get(entry.getKey());
            list.remove(Integer.parseInt(id));
        }
    }

    @Override
    public void updateData(HttpServletRequest request, String userId) {
        // 先删除
        UserDatabase userDatabase = (UserDatabase) request.getSession().getAttribute("userDatabase"+userId);
        Table table = userDatabase.getDatabaseMap().get(request.getParameter("databaseName"))
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
        request.getSession().setAttribute("userDatabase"+userId,userDatabase);
    }

    @Override
    public Object parseSql(HttpServletRequest request, String userId) {
        UserDatabase userDatabase = (UserDatabase) request.getSession().getAttribute("userDatabase"+userId);
        String databaseName = null;
        for (Cookie cookie : request.getCookies()) {
            if ("databaseName".equals(cookie.getName())) {
                databaseName = cookie.getValue();
            }
        }
        Database database = userDatabase.getDatabaseMap().get(databaseName);
        String sql = request.getParameter("sql");
        // 获取sql类型
        SqlType sqlType = SqlParser.getSqlType(sql);
        // 若为null，说明语法错误
        if (sqlType == null) {
            return new ResultInfo(false,"Please check your sql syntax!",null);
        }
        // 判断sql类型
        switch (sqlType) {
            case SELECT:
                Map<String, List<String>> resultSet = selectDataWithSql(database, sql);
                if (resultSet == null) {
                    return new ResultInfo(false,"Select failed! Please check your sql syntax!",null);
                }
                return new ResultInfo(true,null,resultSet);
            case DELETE:
                if (deleteDataWithSql(database,sql)) {
                    // 删除成功
                    request.getSession().setAttribute("userDatabase"+userId,userDatabase);
                    return new ResultInfo(true,null,null);
                }
                return new ResultInfo(false,"Delete failed! Please check your sql syntax!",null);
            case UPDATE:
                if (updateDataWithSql(database,sql)) {
                    request.getSession().setAttribute("userDatabase"+userId,userDatabase);
                    return new ResultInfo(true,null,null);
                }
                return new ResultInfo(false,"Update failed! Please check your sql syntax!",null);
            case INSERT:
                if (insertDataWithSql(database,sql)) {
                    request.getSession().setAttribute("userDatabase"+userId,userDatabase);
                    return new ResultInfo(true,null,null);
                }
                return new ResultInfo(false,"Insert failed! Please check your sql syntax!",null);
            // 若四种类型都不是，则说明sql语法有误
            default:
                // 返回提示错误的信息
                return new ResultInfo(false,"Please check your sql syntax!",null);
        }
    }

    @Override
    public Map<String, List<String>> selectDataWithSql(Database database, String sql) {
        try {
            // 获取表
            Table table = database.getTableMap().get(SqlParser.getTableName(sql));
            // 获取要查询的字段
            List<String> fields = SqlParser.getFields(sql);
            newTableDataWhenNull(table);
            // 如果为空，说明是查询所有字段
            if (fields == null || fields.size() <= 0) {
                fields = new ArrayList<>(table.getData().keySet());
            }
            // 获取要查询的行的id
            String where = SqlParser.getWhere(sql);
            List<String> dataId = getDataId(where, table);
            if (dataId == null || dataId.size() <= 0) {
                dataId = new ArrayList<>();
                for (int i = 0; i < getDataSize(table.getData()); i++) {
                    dataId.add(String.valueOf(i));
                }
            }
            Map<String, List<String>> data = table.getData();
            // 取出结果集
            Map<String, List<String>> resultSet = new HashMap<>();
            for (String field : fields) {
                List<String> result = new ArrayList<>();
                for (String id : dataId) {
                    result.add(data.get(field).get(Integer.parseInt(id)));
                }
                resultSet.put(field,result);
            }
            return resultSet;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer getDataSize(Map<String, List<String>> data) {
        for (Map.Entry<String, List<String>> entry : data.entrySet()) {
            return entry.getValue().size();
        }
        return null;
    }

    @Override
    public List<String> getDataId(String condition, Table table) {
        // 如果没有条件，说明是操作所有数据
        if (condition == null) {
            return null;
        }
        // 把空格去掉
        condition = condition.replaceAll(" ","");
        // 根据 = 来取出键和值
        String[] split = condition.split("=");
        String key = split[0];
        String value = split[1];
        // 获取数据
        Map<String, List<String>> data = table.getData();
        // 通过键获取其对应的值的集合
        List<String> list = data.get(key);
        List<String> ids = new ArrayList<>();
        // 遍历集合，找到该值对应的行数
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(value)) {
                ids.add(String.valueOf(i));
            }
        }
        return ids;
    }

    @Override
    public Boolean deleteDataWithSql(Database database, String sql) {
        String tableName = null;
        try {
            tableName = SqlParser.getTableName(sql);
            Table table = database.getTableMap().get(tableName);
            List<String> list = getDataId(SqlParser.getWhere(sql), table);
            // 如果没有条件，则说明是全部删除
            if (list == null || list.size() <= 0) {
                database.getTableMap().remove(tableName);
                return true;
            }
            // 若有返回条件，则按条件删除
            for (String id : list) {
                deleteData(table,id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Boolean updateDataWithSql(Database database, String sql) {
        try {
            Table table = database.getTableMap().get(SqlParser.getTableName(sql));
            Map<String, List<String>> data = table.getData();
            // 获取id，来修改对应的值
            List<String> id = getDataId(SqlParser.getWhere(sql), table);
            // 若没有条件，说明是修改全部
            if (id == null || id.size() <= 0) {
                id = new ArrayList<>();
                for (int i = 0; i < getDataSize(data); i++) {
                    id.add(String.valueOf(i));
                }
            }
            for (String s : id) {
                updateDataWithSql(sql,data,s);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void updateDataWithSql(String sql, Map<String, List<String>> data, String id) throws JSQLParserException {
        // 获取sql语句中的字段名
        List<String> fields = SqlParser.getFields(sql);
        // 如果获取不到字段名，说明是对所有字段数据进行插入
        if (fields == null) {
            fields = new ArrayList<>(data.keySet());
        }
        // 获取sql语句对应的字段值
        List<String> fieldValue = SqlParser.getFieldValue(sql);
        for (int i = 0; i < fields.size(); i++) {
            // 获取对应字段的值集合
            List<String> values = data.get(fields.get(i));
            // 删除原表中要修改的值
            values.remove(Integer.parseInt(id));
            // 加入新的值
            values.add(Integer.parseInt(id),fieldValue.get(i));
        }
    }

    @Override
    public Boolean insertDataWithSql(Database database, String sql) {
        Table table = null;
        try {
            table = database.getTableMap().get(SqlParser.getTableName(sql));
            // 若获取不到表，说明sql语句有误
            if (table == null) {
                return false;
            }
            String id = null;
            // 先判断表中是否有数据
            newTableDataWhenNull(table);
            // 先新建一行数据，并初始化为空字符串
            for (String field : table.getFieldType().keySet()) {
                table.getData().get(field).add("");
                id = String.valueOf(table.getData().get(field).size()-1);
            }
            // 进行修改
            updateDataWithSql(sql,table.getData(),id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Object parseCommand(HttpServletRequest request, String userId) {
        try {
            String databseName = null;
            // 获取数据库名
            for (Cookie cookie : request.getCookies()) {
                if ("databaseName".equals(cookie.getName())) {
                    databseName = cookie.getValue();
                }
            }
            UserDatabase userDatabase = (UserDatabase) request.getSession().getAttribute("userDatabase"+ userId);
            Map<String, RedisDatabase> redisDatabaseMap = userDatabase.getRedisDatabaseMap();
            // 获取命令
            String command = request.getParameter("redisCommand");
            if (command == null || command.length() <= 0) {
                return new ResultInfo(false,"Please check the syntax!",null);
            }
            Map<String, String> data = redisDatabaseMap.get(databseName).getData();
            // 对语句进行预处理
            command = command.trim().toLowerCase();
            // 判断语句的类型
            if (command.startsWith("set")) {
                // 存放操作
                // 获取键
                String key = command.substring(command.indexOf(" ") + 1, command.lastIndexOf(" "));
                // 获取值
                String value = command.substring(command.lastIndexOf(" ") + 1);
                // 若没有数据，则新建一个数据map
                if (data == null) {
                    data = new HashMap<>();
                    redisDatabaseMap.get(databseName).setData(data);
                }
                // 存入数据中
                data.put(key,value);
                request.getSession().setAttribute("userDatabase"+userId,userDatabase);
                return new ResultInfo(true,null,null);
            }
            // 若不存在数据，则删除和取出操作不能进行
            if (data == null) {
                return new ResultInfo(false,null,null);
            }
            if (command.startsWith("del")) {
                // 删除操作
                String[] keys = command.substring(command.indexOf(" ") + 1).split(" ");
                // 遍历删除
                for (String key : keys) {
                    data.remove(key);
                }
                request.getSession().setAttribute("userDatabase"+userId,userDatabase);
                return new ResultInfo(true,null,null);
            }
            if (command.startsWith("get")) {
                // 取出操作
                String key = command.substring(command.indexOf(" ") + 1);
                String value = data.get(key);
                if (value != null && value.length() > 0) {
                    Map<String, String> map = new HashMap<>();
                    map.put(key,value);
                    request.getSession().setAttribute("userDatabase"+userId,userDatabase);
                    return new ResultInfo(true,null,map);
                }
                request.getSession().setAttribute("userDatabase"+userId,userDatabase);
                return new ResultInfo(false,null,null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultInfo(false,"Please check the syntax",null);
        }
        return new ResultInfo(false,"Please check the syntax",null);
    }

    @Override
    public void createDatabase(HttpServletRequest request, String id) {
        HttpSession session = request.getSession();
        // 获取数据库的名字
        String name = request.getParameter("name");
        if (isNull(name)) {
            return;
        }
        name = name.toLowerCase();
        // 创建database对象
        Database database = new Database();
        // 在session中获取userDatabase，若为空则新建一个
        Object newUserSql = session.getAttribute("userDatabase"+id);
        if (newUserSql != null) {
            UserDatabase userSql = (UserDatabase) newUserSql;
            if (userSql.getDatabaseMap() == null) {
                Map<String, Database> databaseMap = new HashMap<>();
                userSql.setDatabaseMap(databaseMap);
            }
            userSql.getDatabaseMap().put(name,database);
            session.setAttribute("userDatabase"+id,userSql);
            return;
        }
        UserDatabase userDatabase = newUserDatabaseAndSetUser(session, id);
        // 创建map
        Map<String, Database> databaseMap = new HashMap<>();
        // 设置database的name
        databaseMap.put(name,database);
        userDatabase.setDatabaseMap(databaseMap);
        session.setAttribute("userDatabase"+id, userDatabase);
    }

    @Override
    public void deleteDatabase(HttpServletRequest request, String userId) {
        // 获取数据库名称
        String databaseName = request.getParameter("name");
        // 删除该数据库
        UserDatabase userDatabase = (UserDatabase) request.getSession().getAttribute("userDatabase"+userId);
        userDatabase.getDatabaseMap().remove(databaseName);
        request.getSession().setAttribute("userDatabase"+userId,userDatabase);
    }

    @Override
    public void updateDatabase(HttpServletRequest request, String userId) {
        // 获取新旧数据库的名称
        String oldName = request.getParameter("oldName");
        String newName = request.getParameter("newName");
        if (isNull(newName)) {
            return;
        }
        newName = newName.toLowerCase();
        UserDatabase userDatabase = (UserDatabase) request.getSession().getAttribute("userDatabase"+userId);
        // 获取databaseMap
        Map<String, Database> databaseMap = userDatabase.getDatabaseMap();
        // 获取旧数据库
        Database database = databaseMap.get(oldName);
        // 将旧数据库名称从databaseMap删除
        databaseMap.remove(oldName);
        // 将新的数据库名称添加进去
        databaseMap.put(newName,database);
        request.getSession().setAttribute("userDatabase"+userId,userDatabase);
    }

    @Override
    public void createRedisDatabase(HttpServletRequest request, String id) {
        HttpSession session = request.getSession();
        // 获取数据库的名字
        String name = request.getParameter("name");
        if (name == null || name.length() <= 0) {
            return;
        }
        name = name.toLowerCase();
        // 创建redisDatabase对象
        RedisDatabase redisDatabase = new RedisDatabase();
        // 在session中获取userDatabase，若为空则新建一个
        Object newUserDatabase = session.getAttribute("userDatabase"+id);
        if (newUserDatabase != null) {
            UserDatabase userSql = (UserDatabase) newUserDatabase;
            if (userSql.getRedisDatabaseMap() == null) {
                Map<String, RedisDatabase> databaseMap = new HashMap<>();
                userSql.setRedisDatabaseMap(databaseMap);
            }
            userSql.getRedisDatabaseMap().put(name,redisDatabase);
            session.setAttribute("userDatabase"+id,userSql);
            return;
        }
        UserDatabase userDatabase = newUserDatabaseAndSetUser(session, id);
        // 创建map
        Map<String, RedisDatabase> map = new HashMap<>();
        // 设置database的name
        map.put(name,redisDatabase);
        userDatabase.setRedisDatabaseMap(map);
        session.setAttribute("userDatabase"+id, userDatabase);
    }

    @Override
    public void updateRedisDatabase(HttpServletRequest request, String userId) {
        // 获取新旧数据库的名称
        String oldName = request.getParameter("oldName");
        String newName = request.getParameter("newName");
        if (newName == null || newName.length() <= 0) {
            return;
        }
        newName = newName.toLowerCase();
        UserDatabase userDatabase = (UserDatabase) request.getSession().getAttribute("userDatabase"+userId);
        // 获取databaseMap
        Map<String, RedisDatabase> databaseMap = userDatabase.getRedisDatabaseMap();
        // 获取旧数据库
        RedisDatabase database = databaseMap.get(oldName);
        // 将旧数据库名称从databaseMap删除
        databaseMap.remove(oldName);
        // 将新的数据库名称添加进去
        databaseMap.put(newName,database);
        request.getSession().setAttribute("userDatabase"+userId,userDatabase);
    }

    @Override
    public void deleteRedisDatabase(HttpServletRequest request, String userId) {
        // 获取数据库名称
        String databaseName = request.getParameter("name");
        // 删除该数据库
        UserDatabase userDatabase = (UserDatabase) request.getSession().getAttribute("userDatabase"+userId);
        userDatabase.getRedisDatabaseMap().remove(databaseName);
        request.getSession().setAttribute("userDatabase"+userId,userDatabase);
    }
}
