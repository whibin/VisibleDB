package com.whibin.web.servlet;

import com.whibin.domain.po.User;
import com.whibin.domain.vo.UserDatabase;
import com.whibin.service.AuthorityService;
import com.whibin.service.impl.AuthorityServiceImpl;
import com.whibin.util.GetUserId;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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

    /**
     * 发送用户的请求
     * @param request
     * @param response
     * @throws IOException
     */
    public void sendRequest(HttpServletRequest request, HttpServletResponse response) {
        // 获取接受人的id
        String receiverId = request.getParameter("receiver");
        // 获取发起人的id
        String userId = GetUserId.getUserId(request);
        HttpSession session = request.getSession();
        // 获取接收人的信息
        UserDatabase receiver = (UserDatabase) session.getAttribute("userDatabase" + receiverId);
        // 获取发起人的信息
        UserDatabase initiator = (UserDatabase) session.getAttribute("userDatabase" + userId);
        // 获取真实路径
        String realPath = request.getServletContext().getRealPath("UserData");
        service.saveSentRequest(receiver,initiator,realPath);
    }

    /**
     * 获取请求
     * @param request
     * @param response
     * @return
     */
    public Object getSentRequest(HttpServletRequest request, HttpServletResponse response) {
        // 获取真实路径
        String realPath = request.getServletContext().getRealPath("UserData");
        // 获取接受请求的用户id
        String userId = GetUserId.getUserId(request);
        return service.getSentRequest(realPath,userId);
    }

    /**
     * 拒绝请求
     * @param request
     * @param response
     */
    public void rejectRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ClassNotFoundException {
        // 获取接收者id
        String userId = GetUserId.getUserId(request);
        // 获取拒绝的id
        String rejectId = request.getParameter("iId");
        String realPath = request.getServletContext().getRealPath("UserData");
        // 从请求队列中删除
        service.removeRequest(userId,rejectId,realPath);
    }

    /**
     * 允许请求
     * @param request
     * @param response
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void allowRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ClassNotFoundException {
        // 获取接收者id
        String userId = GetUserId.getUserId(request);
        // 获取允许的id
        String allowId = request.getParameter("iId");
        String realPath = request.getServletContext().getRealPath("UserData");
        HttpSession session = request.getSession();
        // 获取接收者的信息
        UserDatabase receiver = (UserDatabase) session.getAttribute("userDatabase" + userId);
        // 获取允许的用户的信息
        UserDatabase allow = (UserDatabase) session.getAttribute("userDatabase" + allowId);
        // 在接收者信息中设置允许的用户的权限
        service.setAuthority(receiver,allow);
        // 重置attribute
        session.setAttribute("userDatabase" + userId, receiver);
        // 从请求队列中删除
        service.removeRequest(userId,allowId,realPath);
    }

    /**
     * 获取权限
     * @param request
     * @param response
     * @return
     */
    public Object getAuthority(HttpServletRequest request, HttpServletResponse response) {
        UserDatabase user = (UserDatabase) request.getSession().getAttribute("userDatabase" + GetUserId.getUserId(request));
        if (user == null) {
            return null;
        }
        return user.getAuthority();
    }

    /**
     * 修改权限
     * @param request
     * @param response
     */
    public void updateAuthority(HttpServletRequest request, HttpServletResponse response) {
        UserDatabase user = (UserDatabase) request.getSession().getAttribute("userDatabase" + GetUserId.getUserId(request));
        // 获取新的权限和用户名
        String newAuthority = request.getParameter("newA");
        String username = request.getParameter("username");
        // 更改权限
        service.updateAuthority(user,newAuthority,username);
    }

    /**
     * 删除权限
     * @param request
     * @param response
     */
    public void removeAuthority(HttpServletRequest request, HttpServletResponse response) {
        UserDatabase user = (UserDatabase) request.getSession().getAttribute("userDatabase" + GetUserId.getUserId(request));
        user.getAuthority().remove(request.getParameter("username"));
    }

    /**
     * 获取可操作性的数据库
     * @param request
     * @param response
     * @return
     */
    public Object getOperableDatabase(HttpServletRequest request, HttpServletResponse response) {
        List<Object> list = new ArrayList<>();
        UserDatabase userDatabase = (UserDatabase) request.getSession().getAttribute("userDatabase" + GetUserId.getUserId(request));
        list.add(userDatabase.getUser().getUsername());
        list.add(service.getOperableDatabase(request));
        return list;
    }
}
