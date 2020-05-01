//package com.whibin.web.servlet;
//
//import com.whibin.vo.ResultInfo;
//
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//import java.util.Map;
//
///**
// * 将每个模块整合成一个servlet，其中的每个方法代表一个功能
// * 在请求servlet时，将方法名加在servlet后
// */
//@WebServlet("/customerServlet/*")
//public class CustomerServlet extends BaseServlet{
//
//
//    /**
//     * @Description: 游客的登陆功能
//     * @Param: [request, response]
//     * @return: void
//     * @Author: SheldonPeng
//     * @Date: 2019-05-03
//     */
//    /**
//     * 将要返回的信息封装在ResultInfo中，将其返回至反射调用的方法
//     * 再由其传输到前端
//     */
//    public Object login(HttpServletRequest request , HttpServletResponse response){
//
//        // 从前端获取数据
//        Map<String , String[]> customerMap = request.getParameterMap();
////        Customer customer = new Customer();
//        // service层做以下操作，我只是演示一下
//        ResultInfo resultInfo = new ResultInfo();
//        resultInfo.setStatus(false);
//        resultInfo.setMessage("xxx");
////        request.getSession().setAttribute("customer",customer);
//        return resultInfo;
//    }
//
//
//    /**
//     * @Description: 游客改变头像
//     * @Param: [request, response]
//     * @return: void
//     * @Author: SheldonPeng
//     * @Date: 2019-05-03
//     */
//    public void changeIcon(HttpServletRequest request ,HttpServletResponse response){
//
////        Random random = new Random();
//        // 无
//    }
//
//    /**
//     * @Description: 游客退出程序,清除session
//     * @Param: [request, response]
//     * @return: void
//     * @Author: SheldonPeng
//     * @Date: 2019-05-03
//     */
//    public void exit( HttpServletRequest request , HttpServletResponse response) throws IOException {
//
//        HttpSession session = request.getSession();
//        session.invalidate();
//    }
//}
