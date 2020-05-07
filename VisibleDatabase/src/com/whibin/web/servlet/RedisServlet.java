package com.whibin.web.servlet;

import com.whibin.domain.vo.UserDatabase;
import com.whibin.service.DatabaseCommonService;
import com.whibin.service.impl.DatabaseCommonServiceImpl;
import com.whibin.util.GetUserId;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author whibin
 * @date 2020/5/4 14:22
 * @Description: redis数据库相关的servlet
 */
@WebServlet("/redisServlet/*")
public class RedisServlet extends BaseServlet {
    private DatabaseCommonService commonService = new DatabaseCommonServiceImpl();

    /**
     * 创建数据库
     * @param request
     * @param response
     */
    public void createDatabase(HttpServletRequest request, HttpServletResponse response) {
        commonService.createRedisDatabase(request,GetUserId.getUserId(request));
    }

    /**
     * 获取数据库
     * @param request
     * @param response
     * @return
     */
    public Object getDatabase(HttpServletRequest request, HttpServletResponse response) {
        UserDatabase userDatabase = (UserDatabase) request.getSession().getAttribute("userDatabase"+ GetUserId.getUserId(request));
        if (userDatabase == null) {
            return null;
        }
        return userDatabase.getRedisDatabaseMap();
    }

    /**
     * 删除数据库
     * @param request
     * @param response
     */
    public void deleteDatabase(HttpServletRequest request, HttpServletResponse response) {
        commonService.deleteRedisDatabase(request,GetUserId.getUserId(request));
    }

    /**
     * 修改数据库
     * @param request
     * @param response
     */
    public void updateDatabase(HttpServletRequest request, HttpServletResponse response) {
        commonService.updateRedisDatabase(request,GetUserId.getUserId(request));
    }

    /**
     * 解析命令
     * @param request
     * @param response
     * @return
     */
    public Object parseCommand(HttpServletRequest request, HttpServletResponse response) {
        return commonService.parseCommand(request,GetUserId.getUserId(request));
    }
}
