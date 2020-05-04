package com.whibin.domain;

/**
 * @author whibin
 * @date 2020/5/4 20:30
 * @Description: 权限类别
 */

public enum AuthorityType {
    /**
     * 只读
     * 只写
     * 读写均可
     */
    READONLY,
    WRITEONLY,
    ALL
}
