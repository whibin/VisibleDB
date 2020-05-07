package com.whibin.domain.vo;

import com.whibin.domain.po.User;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author whibin
 * @date 2020/5/1 15:58
 * @Description: 用于绑定user信息和其数据库信息
 */
@Data
public class UserDatabase implements Serializable {
    /**
     * 用户的基本信息
     */
    private User user;
    /**
     * sql数据库的map集合
     */
    private Map<String, Database> databaseMap;
    /**
     * redis数据库的map集合
     */
    private Map<String, RedisDatabase> redisDatabaseMap;
    /**
     * 其下的用户权限集合
     * 一个用户名对应一个权限
     */
    private Map<String, String> authority;
}
