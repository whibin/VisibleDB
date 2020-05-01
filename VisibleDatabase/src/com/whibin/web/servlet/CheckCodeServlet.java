package com.whibin.web.servlet;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * @author whibin
 * @date 2020/4/13 14:25
 * @Description: 生成验证码
 */

@WebServlet("/code")
public class CheckCodeServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, IOException {
        int width = 100;
        int height = 50;
        // 1. 创建对象，在内存中画图（验证码图片对象）
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 2. 美化图片
        // 2.1 填充背景色
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.DARK_GRAY);
        graphics.fillRect(0,0,width,height);
        // 2.2 画边框
        graphics.setColor(Color.green);
        graphics.drawRect(0,0,width-1,height-1);

        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        // 生产随机角标
        Random random = new Random();

        graphics.setColor(Color.ORANGE);
        // 创建对象，存储验证码
        StringBuilder builder = new StringBuilder();

        for (int i = 1; i <= 4; i++) {
            int index = random.nextInt(str.length());
            // 获取字符
            char charAt = str.charAt(index);
            // 2.3 写验证码
            graphics.drawString(charAt + "",width / 5 * i,height / 2);
            builder.append(charAt);
        }

        graphics.setColor(Color.PINK);

        // 3. 将图片输出到页面展示
        ImageIO.write(image,"jpg",resp.getOutputStream());
        // 将验证码存储到session中
        req.getSession().setAttribute("code",builder.toString());
        System.out.println(builder.toString());
    }
}
