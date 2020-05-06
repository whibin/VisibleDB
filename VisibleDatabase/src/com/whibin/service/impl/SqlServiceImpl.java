package com.whibin.service.impl;

import com.whibin.dao.UserDao;
import com.whibin.dao.impl.UserDaoImpl;
import com.whibin.domain.SqlType;
import com.whibin.domain.po.User;
import com.whibin.domain.vo.Database;
import com.whibin.domain.vo.ResultInfo;
import com.whibin.domain.vo.Table;
import com.whibin.domain.vo.UserDatabase;
import com.whibin.service.DatabaseCommonService;
import com.whibin.service.SqlService;
import com.whibin.util.GetUserId;
import com.whibin.util.SqlParser;
import net.sf.jsqlparser.JSQLParserException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author whibin
 * @date 2020/5/1 16:04
 * @Description: SqlService的实现类
 */

public class SqlServiceImpl implements SqlService {
    private DatabaseCommonService commonService = new DatabaseCommonServiceImpl();

    @Override
    public void createDatabase(HttpServletRequest request) {
        commonService.createDatabase(request,GetUserId.getUserId(request));
    }

    @Override
    public void createTable(HttpServletRequest request) {
        commonService.createTable(request,GetUserId.getUserId(request));
    }

    @Override
    public void deleteDatabase(HttpServletRequest request) {
        commonService.deleteDatabase(request,GetUserId.getUserId(request));
    }

    @Override
    public void deleteTable(HttpServletRequest request) {
        commonService.deleteTable(request,GetUserId.getUserId(request));
    }

    @Override
    public void updateDatabase(HttpServletRequest request) {
        commonService.updateDatabase(request,GetUserId.getUserId(request));
    }

    @Override
    public void updateTable(HttpServletRequest request) {
        commonService.updateTable(request,GetUserId.getUserId(request));
    }

    @Override
    public void addData(HttpServletRequest request) {
        commonService.addData(request,GetUserId.getUserId(request));
    }

    @Override
    public void deleteData(HttpServletRequest request) {
        commonService.deleteData(request,GetUserId.getUserId(request));
    }

    @Override
    public void updateData(HttpServletRequest request) {
        commonService.updateData(request,GetUserId.getUserId(request));
    }

    @Override
    public Object parseSql(HttpServletRequest request) {
        return commonService.parseSql(request,GetUserId.getUserId(request));
    }
}