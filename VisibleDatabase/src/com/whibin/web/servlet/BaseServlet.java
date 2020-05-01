package com.whibin.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Description: 用于对servlet共用的方法进行抽取
 * @Param:
 * @return:
 * @Author: SheldonPeng
 * @Date: 2019-04-29
 * @Modified: 将该类变为抽象类，使其不能被实例化 -- By whibin
 */
public abstract class BaseServlet extends HttpServlet {

    /**
     * @Description: 用于对用户请求的方法进行分发与执行
     * @Param: [request, response]
     * @return: void
     * @Author: SheldonPeng
     * @Date: 2019-04-29
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 设置编码格式为UTF-8
        request.setCharacterEncoding("utf-8");
        // 获取请求的路径
        String uri = request.getRequestURI();
        // 获取请求的方法名称
        String requestName = uri.substring(uri.lastIndexOf('/')+ 1);
        System.out.println(uri);
        System.out.println(requestName);
        try {
            //  获取方法的对象
            Method method = this.getClass().getMethod(requestName , HttpServletRequest.class , HttpServletResponse.class);
            // 执行这个方法
            Object invokeResponse = method.invoke(this, request, response);
            ObjectMapper objectMapper = new ObjectMapper();
            // 设置编码格式
            response.setContentType("application/json;charset=utf-8");
            // 将数据传回客户端
            objectMapper.writeValue( response.getOutputStream(), invokeResponse);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}