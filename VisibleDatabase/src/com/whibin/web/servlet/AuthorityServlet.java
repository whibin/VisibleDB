package com.whibin.web.servlet;

import com.whibin.service.AuthorityService;
import com.whibin.service.impl.AuthorityServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author whibin
 * @date 2020/5/4 21:29
 * @Description: 和权限相关的servlet
 */
@WebServlet("/authorityServlet/*")
public class AuthorityServlet extends BaseServlet {
    private AuthorityService service = new AuthorityServiceImpl();

    /**
     * 获取用户
     * @param request
     * @param response
     * @return
     */
    public Object getUsers(HttpServletRequest request, HttpServletResponse response) {
        return service.getUsers(request);
    }
}
