package com.whibin.service.impl;

import com.whibin.dao.UserDao;
import com.whibin.dao.impl.UserDaoImpl;
import com.whibin.domain.po.User;
import com.whibin.domain.vo.ResultInfo;
import com.whibin.service.UserService;
import com.whibin.util.BeanUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author whibin
 * @date 2020/4/29 16:43
 * @Description: UserService的实现类
 */

public class UserServiceImpl implements UserService {
    private UserDao dao = new UserDaoImpl();

    @Override
    public Boolean login(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        HttpSession session = request.getSession();
        // 判断验证码是否正确
        String code = (String) session.getAttribute("code");
        // 删除验证码的信息
        session.removeAttribute("code");
        // 若不正确
        if (!parameterMap.get("code")[0].equalsIgnoreCase(code)) {
            System.out.println("myinput:" + parameterMap.get("code")[0]);
            System.out.println("Error Verification code!");
            session.setAttribute("message","Error Verification code!");
            return false;
        }
        // 在数据库中判断用户名密码是否正确
        User user = dao.get(parameterMap.get("username")[0]);
        if (user == null || !user.getPassword().equals(parameterMap.get("password")[0])) {
            System.out.println("Username or Password incorrect!");
            session.setAttribute("message","Username or Password incorrect!");
            return false;
        }
        // 若均正确
        session.setAttribute("user",user);
        return true;
    }

    @Override
    public Object checkUsername(String username) {
        if (dao.get(username) == null) {
            return new ResultInfo(false,null,null);
        }
        return new ResultInfo(true,"Username already exists!",null);
    }

    @Override
    public Boolean register(HttpServletRequest request) throws IllegalAccessException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        // 判断验证码是否正确
        HttpSession session = request.getSession();
        String code = (String) session.getAttribute("code");
        // 删除验证码的信息
        session.removeAttribute("code");
        if (!parameterMap.get("code")[0].equalsIgnoreCase(code)) {
            session.setAttribute("message","Error Verification code!");
            return false;
        }
        // 判断用户名是否存在
        if (dao.get(parameterMap.get("username")[0]) != null) {
            return false;
        }
        if (!isLegal(parameterMap.get("username")[0])
                || !isLegal(parameterMap.get("password")[0])) {
            return false;
        }
        // 存储用户信息
        User user = new User();
        BeanUtil.populateParameter(user,parameterMap);
        dao.save(user);
        session.setAttribute("message","Register Success! Please login");
        return true;
    }

    @Override
    public void updateUser(HttpServletRequest request) throws IllegalAccessException, FileUploadException, IOException, NoSuchFieldException {
        // 创建ServletFileUpload对象
        ServletFileUpload fileUpload = new ServletFileUpload(new DiskFileItemFactory());
        // 设置字符集
        fileUpload.setHeaderEncoding("UTF-8");
        // 解析request请求
        List<FileItem> fileItems = fileUpload.parseRequest(request);
        // 创建map，用于存放键值对
        Map<String,Object> map = new HashMap<>();
        for (FileItem fileItem : fileItems) {
            // 如果是普通字段
            if (fileItem.isFormField()) {
                map.put(fileItem.getFieldName(),fileItem.getString());
                continue;
            }
            // 如果是文件
            String name = fileItem.getName();
            // 获取id，根据id来存储图片
            String id = (String) map.get("id");
            if (name == null || "".equals(name)) {
                map.put("icon","UserIcon/" + id + ".jpg");
                continue;
            }
            InputStream inputStream = fileItem.getInputStream();
            // 获得文件的后缀名
            String suffix = name.substring(name.indexOf("."));
            // 获取真实路径
            String realPath = request.getServletContext().getRealPath("UserIcon");
            System.out.println(realPath);
            String path = realPath + "/" + id + suffix;
            FileOutputStream outputStream = new FileOutputStream(path);
            byte[] flush = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(flush)) != -1) {
                outputStream.write(flush,0,len);
            }
            outputStream.close();
            map.put("icon","UserIcon/" + id + suffix);
        }
        // 若符合格式，进行修改
        User user = new User();
        BeanUtil.populate(user,map);
        dao.update(user);
        request.getSession().setAttribute("user",user);
    }

    /**
     * 判断用户名密码是否符合格式
     * @param text
     * @return
     */
    private Boolean isLegal(String text) {
        return text.matches("^\\w{6,15}$");
    }
}
