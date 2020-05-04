package com.whibin.service;

import com.whibin.domain.po.User;
import com.whibin.domain.vo.ResultInfo;
import org.apache.commons.fileupload.FileUploadException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author whibin
 * @date 2020/4/29 16:42
 * @Description: 用户相关的业务逻辑
 */

public interface UserService {
    /**
     * 登录的相关业务
     * @param request
     * @return
     */
    Boolean login(HttpServletRequest request, HttpServletResponse response);

    /**
     * 检查用户名是否存在
     * @param username
     * @return
     */
    Object checkUsername(String username);

    /**
     * 注册相关的业务
     * @return
     */
    Boolean register(HttpServletRequest request) throws IllegalAccessException;

    /**
     * 修改用户信息的业务
     * @return
     */
    void updateUser(HttpServletRequest request) throws IllegalAccessException, FileUploadException, IOException, NoSuchFieldException;

    /**
     * 获取用户信息
     * @param request
     * @return
     */
    User getInformation(HttpServletRequest request);
}
