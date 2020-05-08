package com.whibin.web.listener;
/**
 * @author whibin
 * @date 2020/5/4 23:39
 * @Description: 用于加载用户数据的资源
 */

import com.whibin.domain.vo.UserDatabase;
import com.whibin.util.jdbc.PropertiesUtil;
import lombok.SneakyThrows;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@WebListener()
public class UserDatabaseListener implements ServletContextListener,
        HttpSessionListener, HttpSessionAttributeListener {
    /**
     * 用户文件数据的真实路径
     */
    private String realPath;
    /**
     * 存储用户数据的集合
     */
    private List<UserDatabase> userDatabases = new ArrayList<>();

    // Public constructor is required by servlet spec
    public UserDatabaseListener() {
    }

    // -------------------------------------------------------
    // ServletContextListener implementation
    // -------------------------------------------------------
    @Override
    public void contextInitialized(ServletContextEvent sce) {
      /* This method is called when the servlet context is
         initialized(when the Web application is deployed).
         You can initialize servlet context related data here.
      */
        // 获取真实路径
        System.out.println("Servlet Context was initialized");
        realPath = sce.getServletContext().getRealPath("UserData");
        // 把文件中的数据读出来
        for (int i = 1; i <= Integer.parseInt(PropertiesUtil.getValue("maxActive")); i++) {
            try {
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(realPath + "/userDatabase"+i+".txt"));
                UserDatabase o = (UserDatabase) inputStream.readObject();
                userDatabases.add(o);
            } catch (IOException | ClassNotFoundException e) {
                // 若读取不到，则说明没有该文件，跳过即可
                continue;
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
      /* This method is invoked when the Servlet Context
         (the Web application) is undeployed or
         Application Server shuts down.
      */
        System.out.println("Servlet Context was shut down");
    }

    // -------------------------------------------------------
    // HttpSessionListener implementation
    // -------------------------------------------------------
    @SneakyThrows
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        /* Session is created. */
        System.out.println("Session was created");
        HttpSession session = se.getSession();
        // 将文件数据添加到session中
        List<UserDatabase> temp = new ArrayList<>(userDatabases);
        for (UserDatabase userDatabase : temp) {
            session.setAttribute("userDatabase"+userDatabase.getUser().getId(),userDatabase);
        }
        System.out.println(temp);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        /* Session is destroyed. */
        System.out.println("Session was destroyed");
    }

    // -------------------------------------------------------
    // HttpSessionAttributeListener implementation
    // -------------------------------------------------------

    @Override
    public void attributeAdded(HttpSessionBindingEvent sbe) {
      /* This method is called when an attribute
         is added to a session.
      */
        HttpSession session = sbe.getSession();
        for (int i = 1; i <= Integer.parseInt(PropertiesUtil.getValue("maxActive")); i++) {
            // 若找不到该数据，则跳过
            Object attribute = session.getAttribute("userDatabase" + i);
            if (attribute == null) {
                continue;
            }
            // 若找得到则把数据写入文件
            ObjectOutputStream outputStream = null;
            try {
                outputStream = new ObjectOutputStream(new FileOutputStream(realPath+"/userDatabase"+i+".txt"));
                outputStream.writeObject(attribute);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("userData attribute was written");
            // 添加进集合
            userDatabases.add((UserDatabase) attribute);
        }
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent sbe) {
      /* This method is called when an attribute
         is removed from a session.
      */
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent sbe) {
      /* This method is invoked when an attribute
         is replaced in a session.
      */
        System.out.println("Attributed was replaced");
        HttpSession session = sbe.getSession();
        for (int i = 1; i <= Integer.parseInt(PropertiesUtil.getValue("maxActive")); i++) {
            // 若找不到该数据，则跳过
            Object attribute = session.getAttribute("userDatabase" + i);
            if (attribute == null) {
                continue;
            }
            // 若找得到
            UserDatabase userDatabase = (UserDatabase) attribute;
            // 把该数据写到文件
            ObjectOutputStream outputStream = null;
            try {
                outputStream = new ObjectOutputStream(new FileOutputStream(realPath+"/userDatabase"+i+".txt"));
                outputStream.writeObject(userDatabase);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("userData attribute was written");
            // 添加进集合
            userDatabases.add(userDatabase);
        }
    }
}
