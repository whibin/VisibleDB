package com.whibin.web.servlet;

import com.whibin.domain.po.User;
import com.whibin.domain.vo.ResultInfo;
import com.whibin.service.UserService;
import com.whibin.service.impl.UserServiceImpl;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author whibin
 * @date 2020/4/29 14:53
 * @Description:
 */
@WebServlet("/userServlet/*")
public class UserServlet extends BaseServlet {
    private UserService service = new UserServiceImpl();

    /**
     * 登录
     * @param request
     * @param response
     * @return
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 获取参数
        if (service.login(request)) {
            if ("whibin".equals(((User) request.getSession().getAttribute("user")).getUsername())) {
                response.sendRedirect(request.getContextPath() + "/admin.html");
                return;
            }
            response.sendRedirect(request.getContextPath() + "/sql.html");
            return;
        }
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }

    /**
     * 检测用户名是否存在
     * @param request
     * @param response
     * @return
     */
    public Object checkUsername(HttpServletRequest request, HttpServletResponse response) {
        // 获取用户名
        User user = (User) request.getSession().getAttribute("user");
        String username = request.getParameter("username");
        if (user != null && user.getUsername().equals(username)) {
            return new ResultInfo(false, null, null);
        }
        return service.checkUsername(username);
    }

    /**
     * 注册
     * @param request
     * @param response
     */
    public void register(HttpServletRequest request, HttpServletResponse response) throws IOException, IllegalAccessException {
        // 获取参数
        if (service.register(request)) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
        response.sendRedirect(request.getContextPath() + "/register.jsp");
    }

    /**
     * 获取用户的信息
     * @param request
     * @param response
     * @return
     */
    public Object information(HttpServletRequest request, HttpServletResponse response) {
        return new ResultInfo(true, null,request.getSession().getAttribute("user"));
    }

    public void updateInformation(HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, FileUploadException, IOException, NoSuchFieldException {
        service.updateUser(request);
        response.sendRedirect(request.getContextPath() + "/information.html");
    }
}
