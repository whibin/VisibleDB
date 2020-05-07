package com.whibin.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author whibin
 * @date 2020/5/7 21:35
 * @Description: 敏感词汇过滤
 */

@WebFilter("/*")
public class SensitiveWordFilter implements Filter {
    /**
     * 存放敏感词汇的集合
     */
    private List<String> words = new ArrayList<>();

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        // 创建代理对象，增强getParameter方法
        ServletRequest proxyRequest = (ServletRequest) Proxy.newProxyInstance(req.getClass().getClassLoader(),
                req.getClass().getInterfaces(), (o, method, objects) -> {
                    // 增强getParameter方法，首先需要判断是否为这个方法
                    if ("getParameter".equals(method.getName())) {
                        // 增强返回值
                        // 获取返回值
                        String parameter = (String) method.invoke(req,objects);
                        if (parameter != null) {
                            for (String word : words) {
                                if (parameter.contains(word)) {
                                    parameter = parameter.replaceAll(word, "***");
                                }
                            }
                        }
                        return parameter;
                    }
                    if ("getParameterMap".equals(method.getName())) {
                        // 获取返回值
                        Map<String, String[]> parameter = (Map<String, String[]>) method.invoke(req,objects);
                        if (parameter != null) {
                            for (String[] value : parameter.values()) {
                                for (String s : value) {
                                    for (String word : words) {
                                        if (s.contains(word)) {
                                            s = s.replaceAll(word, "***");
                                        }
                                    }
                                }
                            }
                        }
                        return parameter;
                    }
                    return method.invoke(req, objects);
                });
        // 放行，同时将增强后的对象传入doFilter
        chain.doFilter(proxyRequest, resp);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        try {
            // 加载文件
            // 获取文件的真实路径
            String path = config.getServletContext().getRealPath("/WEB-INF/classes/SensitiveWords.txt");
            // 读取文件
            BufferedReader reader = new BufferedReader(new FileReader(path));
            // 将文件的每一行数据添加到集合中
            String word = null;
            while ((word = reader.readLine()) != null) {
                words.add(word);
            }
            System.out.println(words);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
