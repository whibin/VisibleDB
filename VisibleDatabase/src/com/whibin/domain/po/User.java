package com.whibin.domain.po;

import lombok.Data;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author whibin
 * @date 2020/4/29 15:23
 * @Description: 用户的实体类
 */
@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String icon;
}
