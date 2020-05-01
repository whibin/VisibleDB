package com.whibin.web.servlet;

import com.whibin.domain.vo.UserSql;
import com.whibin.service.SqlService;
import com.whibin.service.impl.SqlServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author whibin
 * @date 2020/5/1 15:57
 * @Description: sql数据库相关的servlet
 */
@WebServlet("/sqlServlet/*")
public class SqlServlet extends BaseServlet {
    private SqlService service = new SqlServiceImpl();

    /**
     * 创建数据库
     * @param request
     * @param response
     */
    public void createDatabase(HttpServletRequest request, HttpServletResponse response) {
        service.createDatabase(request);
    }

    /**
     * 获取数据库
     * @param request
     * @param response
     * @return
     */
    public Object getDatabase(HttpServletRequest request, HttpServletResponse response) {
        UserSql userSql = (UserSql) request.getSession().getAttribute("userSql");
        if (userSql == null) {
            return null;
        }
        return userSql.getDatabaseMap();
    }

    public void createTable(HttpServletRequest request, HttpServletResponse response) {
        service.createTable(request);
    }
}
