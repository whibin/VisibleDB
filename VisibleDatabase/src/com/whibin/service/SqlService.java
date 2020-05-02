package com.whibin.service;

import com.whibin.domain.vo.UserSql;

import javax.servlet.http.HttpServletRequest;

/**
 * @author whibin
 * @date 2020/5/1 16:02
 * @Description: sql数据库的业务接口
 */

public interface SqlService {
    /**
     * 创建数据库
     * @param request
     */
    void createDatabase(HttpServletRequest request);

    /**
     * 创建表
     * @param request
     */
    void createTable(HttpServletRequest request);

    /**
     * 删除数据库
     * @param request
     */
    void deleteDatabase(HttpServletRequest request);

    /**
     * 删除表
     * @param request
     */
    void deleteTable(HttpServletRequest request);

    /**
     * 修改数据库
     * @param request
     */
    void updateDatabase(HttpServletRequest request);
}
