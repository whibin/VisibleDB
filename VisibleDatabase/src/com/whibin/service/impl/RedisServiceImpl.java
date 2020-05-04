package com.whibin.service.impl;

import com.whibin.dao.UserDao;
import com.whibin.dao.impl.UserDaoImpl;
import com.whibin.domain.po.User;
import com.whibin.domain.vo.Database;
import com.whibin.domain.vo.RedisDatabase;
import com.whibin.domain.vo.ResultInfo;
import com.whibin.domain.vo.UserDatabase;
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
        UserDatabase userDatabase = new UserDatabase();
        User user = (User) session.getAttribute("user" + id);
        if (user == null) {
            user = dao.get(Integer.parseInt(id));
            session.setAttribute("user"+id,user);
        }
        // 设置user属性
        userDatabase.setUser(user);
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
        try {
            String databseName = null;
            // 获取数据库名
            for (Cookie cookie : request.getCookies()) {
                if ("databaseName".equals(cookie.getName())) {
                    databseName = cookie.getValue();
                }
            }
            UserDatabase userDatabase = (UserDatabase) request.getSession().getAttribute("userDatabase"+GetUserId.getUserId(request));
            Map<String, RedisDatabase> redisDatabaseMap = userDatabase.getRedisDatabaseMap();
            // 获取命令
            String command = request.getParameter("redisCommand");
            if (command == null || command.length() <= 0) {
                return new ResultInfo(false,"Please check the syntax!",null);
            }
            Map<String, String> data = redisDatabaseMap.get(databseName).getData();
            // 对语句进行预处理
            command = command.trim().toLowerCase();
            // 判断语句的类型
            if (command.startsWith("set")) {
                // 存放操作
                // 获取键
                String key = command.substring(command.indexOf(" ") + 1, command.lastIndexOf(" "));
                // 获取值
                String value = command.substring(command.lastIndexOf(" ") + 1);
                // 若没有数据，则新建一个数据map
                if (data == null) {
                    data = new HashMap<>();
                    redisDatabaseMap.get(databseName).setData(data);
                }
                // 存入数据中
                data.put(key,value);
                request.getSession().setAttribute("userDatabase"+GetUserId.getUserId(request),userDatabase);
                return new ResultInfo(true,null,null);
            }
            // 若不存在数据，则删除和取出操作不能进行
            if (data == null) {
                return new ResultInfo(false,null,null);
            }
            if (command.startsWith("del")) {
                // 删除操作
                String[] keys = command.substring(command.indexOf(" ") + 1).split(" ");
                // 遍历删除
                for (String key : keys) {
                    data.remove(key);
                }
                request.getSession().setAttribute("userDatabase"+GetUserId.getUserId(request),userDatabase);
                return new ResultInfo(true,null,null);
            }
            if (command.startsWith("get")) {
                // 取出操作
                String key = command.substring(command.indexOf(" ") + 1);
                String value = data.get(key);
                if (value != null && value.length() > 0) {
                    Map<String, String> map = new HashMap<>();
                    map.put(key,value);
                    request.getSession().setAttribute("userDatabase"+GetUserId.getUserId(request),userDatabase);
                    return new ResultInfo(true,null,map);
                }
                request.getSession().setAttribute("userDatabase"+GetUserId.getUserId(request),userDatabase);
                return new ResultInfo(false,null,null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultInfo(false,"Please check the syntax",null);
        }
        return new ResultInfo(false,"Please check the syntax",null);
    }
}
