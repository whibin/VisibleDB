package com.whibin.service.impl;

import com.whibin.dao.UserDao;
import com.whibin.dao.impl.UserDaoImpl;
import com.whibin.domain.po.User;
import com.whibin.domain.vo.Database;
import com.whibin.domain.vo.RedisDatabase;
import com.whibin.domain.vo.ResultInfo;
import com.whibin.domain.vo.UserDatabase;
import com.whibin.service.DatabaseCommonService;
import com.whibin.service.RedisService;
import com.whibin.util.GetUserId;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author whibin
 * @date 2020/5/4 14:31
 * @Description: RedisService的实现类
 */

public class RedisServiceImpl implements RedisService {
    private UserDao dao = new UserDaoImpl();
    private DatabaseCommonService commonService = new DatabaseCommonServiceImpl();

    @Override
    public void createDatabase(HttpServletRequest request) {
        HttpSession session = request.getSession();
        // 获取数据库的名字
        String name = request.getParameter("name");
        if (name == null || name.length() <= 0) {
            return;
        }
        name = name.toLowerCase();
        // 创建redisDatabase对象
        RedisDatabase redisDatabase = new RedisDatabase();
        String id = GetUserId.getUserId(request);
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
        UserDatabase userDatabase = commonService.newUserDatabaseAndSetUser(session, dao, id);
        // 创建map
        Map<String, RedisDatabase> map = new HashMap<>();
        // 设置database的name
        map.put(name,redisDatabase);
        userDatabase.setRedisDatabaseMap(map);
        session.setAttribute("userDatabase"+id, userDatabase);
    }

    @Override
    public void deleteDatabase(HttpServletRequest request) {
        // 获取数据库名称
        String databaseName = request.getParameter("name");
        // 删除该数据库
        UserDatabase userDatabase = (UserDatabase) request.getSession().getAttribute("userDatabase"+GetUserId.getUserId(request));
        userDatabase.getRedisDatabaseMap().remove(databaseName);
        request.getSession().setAttribute("userDatabase"+GetUserId.getUserId(request),userDatabase);
    }

    @Override
    public void updateDatabase(HttpServletRequest request) {
        // 获取新旧数据库的名称
        String oldName = request.getParameter("oldName");
        String newName = request.getParameter("newName");
        if (newName == null || newName.length() <= 0) {
            return;
        }
        newName = newName.toLowerCase();
        UserDatabase userDatabase = (UserDatabase) request.getSession().getAttribute("userDatabase"+GetUserId.getUserId(request));
        // 获取databaseMap
        Map<String, RedisDatabase> databaseMap = userDatabase.getRedisDatabaseMap();
        // 获取旧数据库
        RedisDatabase database = databaseMap.get(oldName);
        // 将旧数据库名称从databaseMap删除
        databaseMap.remove(oldName);
        // 将新的数据库名称添加进去
        databaseMap.put(newName,database);
        request.getSession().setAttribute("userDatabase"+GetUserId.getUserId(request),userDatabase);
    }

    @Override
    public Object parseCommand(HttpServletRequest request) {
        return commonService.parseCommand(request,GetUserId.getUserId(request));
    }
}
