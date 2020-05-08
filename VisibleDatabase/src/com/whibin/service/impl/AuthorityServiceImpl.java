package com.whibin.service.impl;

import com.whibin.domain.po.User;
import com.whibin.domain.vo.UserDatabase;
import com.whibin.service.AuthorityService;
import com.whibin.util.GetUserId;
import com.whibin.util.jdbc.PropertiesUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author whibin
 * @date 2020/5/4 21:31
 * @Description: AuthorityService的实现类
 */

public class AuthorityServiceImpl implements AuthorityService {
    @Override
    public List<UserDatabase> getUsers(HttpServletRequest request) {
        // 创建集合，用来放用户的数据
        List<UserDatabase> list = new ArrayList<>();
        // 获取用户
        for (int i = 1; i <= Integer.parseInt(PropertiesUtil.getValue("maxActive")); i++) {
            // 排除自己
            if (GetUserId.getUserId(request).equals(String.valueOf(i))) {
                continue;
            }
            // session内没有，跳过
            Object attribute = request.getSession().getAttribute("userDatabase" + i);
            if (attribute == null) {
                continue;
            }
            UserDatabase userDatabase = (UserDatabase) attribute;
            // 符合条件，添加
            list.add(userDatabase);
        }
        System.out.println(list);
        return list;
    }

    @Override
    public void saveSentRequest(UserDatabase receiver, UserDatabase initiator, String realPath) {
        // 拼接路径字符串
        String path = realPath + "/" + "sendRequest-r" + receiver.getUser().getId() + ".txt";
        List<UserDatabase> initiators;
        try {
            // 从文件中读取出集合
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(path));
            initiators = (List<UserDatabase>) inputStream.readObject();
            initiators.add(initiator);
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(path));
            outputStream.writeObject(initiators);
        } catch (IOException e) {
            // 若捕获到异常，说明该文件不存在
            // 此时把文件写入
            try {
                ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(path));
                // 创建新的集合
                initiators = new ArrayList<>();
                // 将发起者添加到集合中
                initiators.add(initiator);
                // 写入文件
                outputStream.writeObject(initiators);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getSentRequest(String realPath, String userId) {
        String path = realPath + "/" + "sendRequest-r" + userId + ".txt";
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(path));
            // 读取文件数据并返回
            return (List<UserDatabase>) inputStream.readObject();
        } catch (IOException e) {
            // 捕获到异常，说明没有接收到请求
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void removeRequest(String userId, String rejectId, String realPath) throws IOException, ClassNotFoundException {
        // 拼接路径
        String path = realPath + "/" + "sendRequest-r" + userId + ".txt";
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(path));
        List<UserDatabase> userDatabases = (List<UserDatabase>) inputStream.readObject();
        // 从请求队列的文件中删除该请求
        for (int i = 0; i < userDatabases.size(); i++) {
            if (userDatabases.get(i).getUser().getId().equals(Long.valueOf(rejectId))) {
                userDatabases.remove(i);
                break;
            }
        }
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(path));
        outputStream.writeObject(userDatabases);
    }

    @Override
    public void setAuthority(UserDatabase receiver, UserDatabase allow) {
        Map<String, String> authority = receiver.getAuthority();
        // 若为空，则新建一个
        if (authority == null) {
            authority = new HashMap<>();
            receiver.setAuthority(authority);
        }
        // 设置权限，默认为只读
        authority.put(allow.getUser().getUsername(),"READ ONLY");
    }

    @Override
    public void updateAuthority(UserDatabase user, String newAuthority, String username) {
        Map<String, String> authority = user.getAuthority();
        // 重置新的权限
        authority.put(username,newAuthority);
    }

    @Override
    public Object getOperableDatabase(HttpServletRequest request) {
        // 获取当前的用户
        HttpSession session = request.getSession();
        String id = GetUserId.getUserId(request);
        UserDatabase userNow = (UserDatabase) session.getAttribute("userDatabase" + id);
        List<UserDatabase> list = new ArrayList<>();
        // 遍历所有用户的权限列表
        for (int i = 1; i <= Integer.parseInt(PropertiesUtil.getValue("maxActive")); i++) {
            // 若是自己就跳过
            if (id.equals(String.valueOf(i))) {
                continue;
            }
            // 若找不到该用户就跳过
            Object attribute = session.getAttribute("userDatabase" + i);
            if (attribute == null) {
                continue;
            }
            // 若找得到
            UserDatabase userDatabase = (UserDatabase) attribute;
            // 若没有权限列表则跳过
            Map<String, String> authority = userDatabase.getAuthority();
            if (authority == null) {
                continue;
            }
            // 如果权限列表中没有则跳过
            String au = authority.get(userNow.getUser().getUsername());
            if (au == null) {
                continue;
            }
            // 若有，则将该用户添加到集合中
            list.add(userDatabase);
        }
        return list;
    }
}
