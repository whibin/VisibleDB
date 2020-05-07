package com.whibin.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author whibin
 * @date 2020/5/5 16:28
 * @Description: 通用的数据库的相关操作
 */

public interface DatabaseCommonService {
    /**
     * 创建表
     * @param request
     * @param userId
     */
    void createTable(HttpServletRequest request, String userId);

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
     * 删除表数据
     * @param request
     * @param userId
     */
    void deleteData(HttpServletRequest request, String userId);

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
