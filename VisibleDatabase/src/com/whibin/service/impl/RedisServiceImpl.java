package com.whibin.service.impl;

import com.whibin.dao.UserDao;
import com.whibin.dao.impl.UserDaoImpl;
import com.whibin.domain.po.User;
import com.whibin.domain.vo.Database;
import com.whibin.domain.vo.RedisDatabase;
import com.whibin.domain.vo.ResultInfo;
import com.whibin.domain.vo.UserDatabase;
import com.whibin.service.DatabaseCommonService;
import com.whibin.service.RedisService;
import com.whibin.util.GetUserId;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author whibin
 * @date 2020/5/4 14:31
 * @Description: RedisService的实现类
 */

public class RedisServiceImpl implements RedisService {
    private DatabaseCommonService commonService = new DatabaseCommonServiceImpl();

    @Override
    public void createDatabase(HttpServletRequest request) {
        commonService.createRedisDatabase(request,GetUserId.getUserId(request));
    }

    @Override
    public void deleteDatabase(HttpServletRequest request) {
        commonService.deleteRedisDatabase(request,GetUserId.getUserId(request));
    }

    @Override
    public void updateDatabase(HttpServletRequest request) {
        commonService.updateRedisDatabase(request,GetUserId.getUserId(request));
    }

    @Override
    public Object parseCommand(HttpServletRequest request) {
        return commonService.parseCommand(request,GetUserId.getUserId(request));
    }
}
