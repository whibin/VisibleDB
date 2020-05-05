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
 * @date 2020/5/5 19:06
 * @Description: 访问其他用户数据库的servlet
 */
@WebServlet("/otherUserDatabaseServlet/*")
public class OtherUserDatabaseServlet extends BaseServlet {
    private DatabaseCommonService service = new DatabaseCommonServiceImpl();

    /**
     * 创建表
     * @param request
     * @param response
     */
    public void createTable(HttpServletRequest request, HttpServletResponse response) {
        service.createTable(request, GetUserId.getOtherUserId(request));
    }

    /**
     * 删除数据
     * @param request
     * @param response
     */
    public void deleteData(HttpServletRequest request, HttpServletResponse response) {
        service.deleteData(request, GetUserId.getOtherUserId(request));
    }

    /**
     * 修改数据
     * @param request
     * @param response
     */
    public void updateData(HttpServletRequest request, HttpServletResponse response) {
        service.updateData(request, GetUserId.getOtherUserId(request));
    }

    /**
     * 获取数据库
     * @param request
     * @param response
     * @return
     */
    public Object getDatabase(HttpServletRequest request, HttpServletResponse response) {
        UserDatabase userDatabase = (UserDatabase) request.getSession()
                .getAttribute("userDatabase" + GetUserId.getOtherUserId(request));
        return userDatabase.getDatabaseMap();
    }

    /**
     * 删除表
     * @param request
     * @param response
     */
    public void deleteTable(HttpServletRequest request, HttpServletResponse response) {
        service.deleteTable(request, GetUserId.getOtherUserId(request));
    }

    /**
     * 修改表
     * @param request
     * @param response
     */
    public void updateTable(HttpServletRequest request, HttpServletResponse response) {
        service.updateTable(request, GetUserId.getOtherUserId(request));
    }

    /**
     * 添加表数据
     * @param request
     * @param response
     */
    public void addData(HttpServletRequest request, HttpServletResponse response) {
        service.addData(request, GetUserId.getOtherUserId(request));
    }

    /**
     * 解析sql
     * @param request
     * @param response
     * @return
     */
    public Object parseSql(HttpServletRequest request, HttpServletResponse response) {
        return service.parseSql(request,GetUserId.getOtherUserId(request));
    }

    /**
     * 获取redis数据库
     * @param request
     * @param response
     * @return
     */
    public Object getRedisDatabase(HttpServletRequest request, HttpServletResponse response) {
        UserDatabase userDatabase = (UserDatabase) request.getSession().getAttribute("userDatabase" + GetUserId.getOtherUserId(request));
        return userDatabase.getRedisDatabaseMap();
    }

    /**
     * 解析redis语法
     * @param request
     * @param response
     * @return
     */
    public Object parseCommand(HttpServletRequest request, HttpServletResponse response) {
        return service.parseCommand(request,GetUserId.getOtherUserId(request));
    }
}
