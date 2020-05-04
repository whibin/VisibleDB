package com.whibin.domain;

import java.io.Serializable;

/**
 * @author whibin
 * @date 2020/5/3 18:34
 * @Description: sql语句的类型
 */

public enum SqlType implements Serializable {
    /**
     * 分别对应增删查改
     */
    SELECT,
    UPDATE,
    DELETE,
    INSERT
}
