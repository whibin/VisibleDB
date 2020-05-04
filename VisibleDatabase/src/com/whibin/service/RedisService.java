package com.whibin.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author whibin
 * @date 2020/5/4 14:31
 * @Description: redis数据相关的业务逻辑
 */

public interface RedisService {
    /**
     * 创建数据库
     * @param request
     */
    void createDatabase(HttpServletRequest request);

    /**
     * 删除数据库
     * @param request
     */
    void deleteDatabase(HttpServletRequest request);

    /**
     * 修改数据库
     * @param request
     */
    void updateDatabase(HttpServletRequest request);

    /**
     * 解析redis命令
     * @param request
     * @return
     */
    Object parseCommand(HttpServletRequest request);
}
