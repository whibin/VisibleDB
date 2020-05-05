package com.whibin.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author whibin
 * @date 2020/5/4 17:43
 * @Description: 获取cookie中用户的id
 */

public class GetUserId {
    public static String getUserId(HttpServletRequest request) {
        for (Cookie cookie : request.getCookies()) {
            if ("userId".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public static String getOtherUserId(HttpServletRequest request) {
        for (Cookie cookie : request.getCookies()) {
            if ("OtherUserId".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
