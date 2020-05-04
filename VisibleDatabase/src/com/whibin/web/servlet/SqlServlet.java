package com.whibin.web.servlet;

import com.whibin.domain.vo.UserDatabase;
import com.whibin.service.SqlService;
import com.whibin.service.impl.SqlServiceImpl;
import com.whibin.util.GetUserId;
import net.sf.jsqlparser.JSQLParserException;

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
        UserDatabase userDatabase = (UserDatabase) request.getSession().getAttribute("userDatabase"+ GetUserId.getUserId(request));
        if (userDatabase == null) {
            return null;
        }
        return userDatabase.getDatabaseMap();
    }

    /**
     * 创建表
     * @param request
     * @param response
     */
    public void createTable(HttpServletRequest request, HttpServletResponse response) {
        service.createTable(request);
    }

    /**
     * 删除数据库
     * @param request
     * @param response
     */
    public void deleteDatabase(HttpServletRequest request, HttpServletResponse response) {
        service.deleteDatabase(request);
    }

    /**
     * 删除表
     * @param request
     * @param response
     */
    public void deleteTable(HttpServletRequest request, HttpServletResponse response) {
        service.deleteTable(request);
    }

    /**
     * 修改数据库
     * @param request
     * @param response
     */
    public void updateDatabase(HttpServletRequest request, HttpServletResponse response) {
        service.updateDatabase(request);
    }

    /**
     * 修改表
     * @param request
     * @param response
     */
    public void updateTable(HttpServletRequest request, HttpServletResponse response) {
        service.updateTable(request);
    }

    /**
     * 添加表的数据
     * @param request
     * @param response
     */
    public void addData(HttpServletRequest request, HttpServletResponse response) {
        service.addData(request);
    }

    /**
     * 删除表的数据
     * @param request
     * @param response
     */
    public void deleteData(HttpServletRequest request, HttpServletResponse response) {
        service.deleteData(request);
    }

    /**
     * 修改表的数据
     * @param request
     * @param response
     */
    public void updateData(HttpServletRequest request, HttpServletResponse response) {
        service.updateData(request);
    }

    /**
     * 解析sql语句
     * @param request
     * @param response
     * @return
     */
    public Object parseSql(HttpServletRequest request, HttpServletResponse response) throws JSQLParserException {
        return service.parseSql(request);
    }
}
