package com.whibin.dao;

import com.whibin.domain.po.User;

/**
 * @author whibin
 * @date 2020/4/29 16:57
 * @Description: user表的CRUD
 */

public interface UserDao {
    /**
     * 查询用户
     * @param username
     * @return
     */
    User get(String username);

    /**
     * 存储用户
     * @param user
     */
    void save(User user);

    /**
     * 修改用户
     * @param user
     */
    void update(User user);

    /**
     * 查询用户
     * @param id
     * @return
     */
    User get(Integer id);
}
