package com.whibin.web.servlet;

import com.whibin.domain.vo.UserDatabase;
import com.whibin.service.AuthorityService;
import com.whibin.service.impl.AuthorityServiceImpl;
import com.whibin.util.GetUserId;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author whibin
 * @date 2020/5/6 13:09
 * @Description: 管理员的servlet
 */
@WebServlet("/adminServlet/*")
public class AdminServlet extends BaseServlet {
    private AuthorityService authorityService = new AuthorityServiceImpl();

    /**
     * 修改权限
     * @param request
     * @param response
     */
    public void updateAuthority(HttpServletRequest request, HttpServletResponse response) {
        UserDatabase user = (UserDatabase) request.getSession().getAttribute("userDatabase" + GetUserId.getOtherUserId(request));
        // 获取新的权限和用户名
        String newAuthority = request.getParameter("newA");
        String username = request.getParameter("username");
        // 更改权限
        authorityService.updateAuthority(user,newAuthority,username);
    }

    /**
     * 删除权限
     * @param request
     * @param response
     */
    public void removeAuthority(HttpServletRequest request, HttpServletResponse response) {
        UserDatabase user = (UserDatabase) request.getSession().getAttribute("userDatabase" + GetUserId.getOtherUserId(request));
        user.getAuthority().remove(request.getParameter("username"));
    }
}
