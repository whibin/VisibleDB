package com.whibin.service;

import com.whibin.domain.vo.UserDatabase;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author whibin
 * @date 2020/5/4 21:31
 * @Description: 权限相关业务逻辑的接口
 */

public interface AuthorityService {
    /**
     * 获取有数据库的用户
     * @param request
     * @return
     */
    List<UserDatabase> getUsers(HttpServletRequest request);
}
