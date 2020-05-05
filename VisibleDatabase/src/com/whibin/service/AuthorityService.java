package com.whibin.service;

import com.whibin.domain.vo.UserDatabase;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    /**
     * 存储发送请求
     * @param receiver
     * @param initiator
     * @param realPath
     * @throws IOException
     */
    void saveSentRequest(UserDatabase receiver, UserDatabase initiator, String realPath);

    /**
     * 获取请求
     * @param realPath
     * @param userId
     * @return
     */
    Object getSentRequest(String realPath, String userId);

    /**
     * 从请求队列中删除
     * @param userId
     * @param rejectId
     * @param realPath
     * @throws IOException
     * @throws ClassNotFoundException
     */
    void removeRequest(String userId, String rejectId, String realPath) throws IOException, ClassNotFoundException;

    /**
     * 设置权限
     * @param receiver
     * @param allow
     */
    void setAuthority(UserDatabase receiver, UserDatabase allow);

    /**
     * 更改权限
     * @param user
     * @param newAuthority
     * @param username
     */
    void updateAuthority(UserDatabase user, String newAuthority, String username);

    /**
     * 获取可操作性的数据库
     * @param request
     * @return
     */
    Object getOperableDatabase(HttpServletRequest request);
}
