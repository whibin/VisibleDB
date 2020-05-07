package com.whibin.web.filter;

import com.whibin.util.GetUserId;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author whibin
 * @date 2020/5/7 9:29
 * @Description: 拦截未登录的用户、拦截访问管理员资源的普通用户
 */

@WebFilter("/*")
public class SafeFilter implements Filter {
    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        // 强转
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        // 获取访问资源路径
        String uri = request.getRequestURI().substring(29);
        System.out.println(uri);
        // 进行未登录拦截
        // 放行servlet
        if (!uri.contains(".")) {
            chain.doFilter(request,resp);
            return;
        }
        // 放行通用资源
        if (uri.startsWith("/css/") || uri.startsWith("/fonts/") || uri.startsWith("/images/")
        || uri.startsWith("/js/")) {
            chain.doFilter(request,resp);
            return;
        }
        // 若访问的是登录和注册界面，放行
        if ("/".equals(uri) || "/index.jsp".equals(uri) || "/register.jsp".equals(uri)) {
            chain.doFilter(request,resp);
            return;
        }
        // 若未登录
        String id = GetUserId.getUserId(request);
        if ("undefined".equals(id)) {
            // 未登录，跳转到登录界面
            request.getSession().setAttribute("message","Please Login FIRST!");
            request.getRequestDispatcher("/index.jsp").forward(request,response);
            return;
        }
        // 若已登录
        // 如果访问的是管理员资源
        if (uri.startsWith("/admin") || "SqlStatus.html".equals(uri)
                || "RedisListen.html".equals(uri)) {
            // 若其不是管理员，跳转
            if (!"1".equals(id)) {
                request.getRequestDispatcher("/No-authority.html").forward(request,response);
                return;
            }
            chain.doFilter(request,resp);
            return;
        }
        chain.doFilter(request,resp);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {

    }

}
