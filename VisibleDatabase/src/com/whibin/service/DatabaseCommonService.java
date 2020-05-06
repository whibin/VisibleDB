package com.whibin.service;

import com.whibin.dao.UserDao;
import com.whibin.domain.vo.Database;
import com.whibin.domain.vo.Table;
import com.whibin.domain.vo.UserDatabase;
import net.sf.jsqlparser.JSQLParserException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @author whibin
 * @date 2020/5/5 16:28
 * @Description: 通用的数据库的相关操作
 */

public interface DatabaseCommonService {
    /**
     * 新建UserDatabase对象并且设置User属性
     * @param session
     * @param id
     * @return
     */
    UserDatabase newUserDatabaseAndSetUser(HttpSession session, String id);

    /**
     * 创建表
     * @param request
     * @param userId
     */
    void createTable(HttpServletRequest request, String userId);

    /**
     * 创建一个新的表
     * @param tableMap
     * @param request
     */
    void newTable(Map<String, Table> tableMap, HttpServletRequest request);

    /**
     * 判断数据是否为空
     * @param string
     * @return
     */
    Boolean isNull(String string);

    /**
     * 删除表
     * @param request
     * @param userId
     */
    void deleteTable(HttpServletRequest request, String userId);

    /**
     * 修改表
     * @param request
     * @param userId
     */
    void updateTable(HttpServletRequest request, String userId);

    /**
     * 添加数据
     * @param request
     * @param userId
     */
    void addData(HttpServletRequest request, String userId);

    /**
     * 添加指定的数据到指定的表中
     * @param table
     * @param data
     */
    void addData(Table table, String[] data);

    /**
     * 创建表的字段
     * @param table
     */
    void newTableDataWhenNull(Table table);

    /**
     * 删除表数据
     * @param request
     * @param userId
     */
    void deleteData(HttpServletRequest request, String userId);

    /**
     * 删除指定表中指定行的数据
     * @param table
     * @param id
     */
    void deleteData(Table table, String id);

    /**
     * 修改表的数据
     * @param request
     * @param userId
     */
    void updateData(HttpServletRequest request, String userId);

    /**
     * 解析sql
     * @param request
     * @param userId
     * @return
     */
    Object parseSql(HttpServletRequest request, String userId);

    /**
     * 根据sql语句查询数据
     * @param database
     * @param sql
     * @return
     */
    Map<String, List<String>> selectDataWithSql(Database database, String sql);

    /**
     * 获取数据的长度
     * @param data
     * @return
     */
    Integer getDataSize(Map<String, List<String>> data);

    /**
     * 获取数据所在行数
     * @param condition
     * @param table
     * @return
     */
    List<String> getDataId(String condition, Table table);

    /**
     * 使用sql语句删除数据
     * @param database
     * @param sql
     * @return
     */
    Boolean deleteDataWithSql(Database database, String sql);

    /**
     * 使用sql语句修改数据
     * @param database
     * @param sql
     * @return
     */
    Boolean updateDataWithSql(Database database, String sql);

    /**
     * 使用sql语句来更新指定行的数据
     * @param sql
     * @param data
     * @param id
     * @throws JSQLParserException
     */
    void updateDataWithSql(String sql, Map<String, List<String>> data, String id) throws JSQLParserException;

    /**
     * 使用sql语句插入数据
     * @param database
     * @param sql
     * @return
     */
    Boolean insertDataWithSql(Database database, String sql);

    /**
     * 解析redis语法
     * @param request
     * @param userId
     * @return
     */
    Object parseCommand(HttpServletRequest request, String userId);

    /**
     * 创建数据库
     * @param request
     * @param id
     */
    void createDatabase(HttpServletRequest request, String id);

    /**
     * 删除数据库
     * @param request
     * @param userId
     */
    void deleteDatabase(HttpServletRequest request, String userId);

    /**
     * 修改数据库
     * @param request
     * @param userId
     */
    void updateDatabase(HttpServletRequest request, String userId);

    /**
     * 创建redis数据库
     * @param request
     * @param id
     */
    void createRedisDatabase(HttpServletRequest request, String id);

    /**
     * 修改redis数据库
     * @param request
     * @param userId
     */
    void updateRedisDatabase(HttpServletRequest request, String userId);

    /**
     * 删除redis数据库
     * @param request
     * @param userId
     */
    void deleteRedisDatabase(HttpServletRequest request, String userId);
}
