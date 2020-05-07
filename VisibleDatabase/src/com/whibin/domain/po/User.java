package com.whibin.domain.po;

import lombok.Data;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;

/**
 * @author whibin
 * @date 2020/4/29 15:23
 * @Description: 用户的实体类
 */
@Data
public class User implements Serializable {
    private Long id;
    private String username;
    private String password;
    /**
     * 图片路径
     */
    private String icon;
}
