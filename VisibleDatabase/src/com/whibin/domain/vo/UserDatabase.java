package com.whibin.domain.vo;

import com.whibin.domain.AuthorityType;
import com.whibin.domain.po.User;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author whibin
 * @date 2020/5/1 15:58
 * @Description: 用于绑定user信息和其数据库信息
 */
@Data
public class UserDatabase implements Serializable {
    private User user;
    private Map<String, Database> databaseMap;
    private Map<String, RedisDatabase> redisDatabaseMap;
    private Map<User, AuthorityType> authority;
}
