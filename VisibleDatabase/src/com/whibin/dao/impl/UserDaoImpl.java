package com.whibin.dao.impl;

import com.whibin.dao.UserDao;
import com.whibin.domain.po.User;
import com.whibin.util.jdbc.JDBCUtil;

/**
 * @author whibin
 * @date 2020/4/29 16:58
 * @Description: userDao的实现类
 */

public class UserDaoImpl implements UserDao {
    private JDBCUtil<User> template = new JDBCUtil<>(User.class, null);

    @Override
    public User get(String username) {
        String sql = "select id,username,password,icon from user where username=?";
        return template.queryForSingle(sql, username);
    }

    @Override
    public void save(User user) {
        String sql = "insert into user(username,password) values(?,?)";
        template.update(sql,user.getUsername(),user.getPassword());
    }

    @Override
    public void update(User user) {
        String sql = "update user set username=?,password=?,icon=? where id=?";
        template.update(sql,user.getUsername(),user.getPassword(),user.getIcon(),user.getId());
    }

    @Override
    public User get(Integer id) {
        String sql = "select id,username,password,icon from user where id=?";
        return template.queryForSingle(sql, id);
    }
}
