package com.whibin.service.impl;

import com.whibin.domain.po.User;
import com.whibin.domain.vo.UserDatabase;
import com.whibin.service.AuthorityService;
import com.whibin.util.GetUserId;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author whibin
 * @date 2020/5/4 21:31
 * @Description:
 */

public class AuthorityServiceImpl implements AuthorityService {
    @Override
    public List<UserDatabase> getUsers(HttpServletRequest request) {
        // 创建集合，用来放用户的数据
        List<UserDatabase> list = new ArrayList<>();
        // 获取用户
        for (int i = 1; i <= 10; i++) {
            // 排除自己
            if (GetUserId.getUserId(request).equals(String.valueOf(i))) {
                continue;
            }
            // session内没有，跳过
            Object attribute = request.getSession().getAttribute("userDatabase" + i);
            if (attribute == null) {
                continue;
            }
            // 没有数据库，跳过
            UserDatabase userDatabase = (UserDatabase) attribute;
            if (userDatabase.getDatabaseMap() == null
                    && userDatabase.getRedisDatabaseMap() == null) {
                continue;
            }
            // 有数据库，添加
            list.add(userDatabase);
        }
        System.out.println(list);
        return list;
    }
}
