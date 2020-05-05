package com.whibin.service.impl;

import com.whibin.dao.UserDao;
import com.whibin.dao.impl.UserDaoImpl;
import com.whibin.domain.SqlType;
import com.whibin.domain.po.User;
import com.whibin.domain.vo.Database;
import com.whibin.domain.vo.ResultInfo;
import com.whibin.domain.vo.Table;
import com.whibin.domain.vo.UserDatabase;
import com.whibin.service.DatabaseCommonService;
import com.whibin.service.SqlService;
import com.whibin.util.GetUserId;
import com.whibin.util.SqlParser;
import net.sf.jsqlparser.JSQLParserException;

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
    private UserDao dao = new UserDaoImpl();
    private DatabaseCommonService commonService = new DatabaseCommonServiceImpl();

    @Override
    public void createDatabase(HttpServletRequest request) {
        HttpSession session = request.getSession();
        // 获取数据库的名字
        String name = request.getParameter("name");
        if (commonService.isNull(name)) {
            return;
        }
        name = name.toLowerCase();
        // 创建database对象
        Database database = new Database();
        String id = GetUserId.getUserId(request);
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
        UserDatabase userDatabase = commonService.newUserDatabaseAndSetUser(session, dao, id);
        // 创建map
        Map<String, Database> databaseMap = new HashMap<>();
        // 设置database的name
        databaseMap.put(name,database);
        userDatabase.setDatabaseMap(databaseMap);
        session.setAttribute("userDatabase"+id, userDatabase);
    }

    @Override
    public void createTable(HttpServletRequest request) {
        commonService.createTable(request,GetUserId.getUserId(request));
    }

    @Override
    public void deleteDatabase(HttpServletRequest request) {
        // 获取数据库名称
        String databaseName = request.getParameter("name");
        // 删除该数据库
        UserDatabase userDatabase = (UserDatabase) request.getSession().getAttribute("userDatabase"+GetUserId.getUserId(request));
        userDatabase.getDatabaseMap().remove(databaseName);
        request.getSession().setAttribute("userDatabase"+GetUserId.getUserId(request),userDatabase);
    }

    @Override
    public void deleteTable(HttpServletRequest request) {
        commonService.deleteTable(request,GetUserId.getUserId(request));
    }

    @Override
    public void updateDatabase(HttpServletRequest request) {
        // 获取新旧数据库的名称
        String oldName = request.getParameter("oldName");
        String newName = request.getParameter("newName");
        if (commonService.isNull(newName)) {
            return;
        }
        newName = newName.toLowerCase();
        UserDatabase userDatabase = (UserDatabase) request.getSession().getAttribute("userDatabase"+GetUserId.getUserId(request));
        // 获取databaseMap
        Map<String, Database> databaseMap = userDatabase.getDatabaseMap();
        // 获取旧数据库
        Database database = databaseMap.get(oldName);
        // 将旧数据库名称从databaseMap删除
        databaseMap.remove(oldName);
        // 将新的数据库名称添加进去
        databaseMap.put(newName,database);
        request.getSession().setAttribute("userDatabase"+GetUserId.getUserId(request),userDatabase);
    }

    @Override
    public void updateTable(HttpServletRequest request) {
        commonService.updateTable(request,GetUserId.getUserId(request));
    }

    @Override
    public void addData(HttpServletRequest request) {
        commonService.addData(request,GetUserId.getUserId(request));
    }

    @Override
    public void deleteData(HttpServletRequest request) {
        commonService.deleteData(request,GetUserId.getUserId(request));
    }

    @Override
    public void updateData(HttpServletRequest request) {
        commonService.updateData(request,GetUserId.getUserId(request));
    }

    @Override
    public Object parseSql(HttpServletRequest request) {
        return commonService.parseSql(request,GetUserId.getUserId(request));
    }
}