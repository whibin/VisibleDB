package com.whibin.domain.vo;

import lombok.Data;

import java.util.Map;

/**
 * @author whibin
 * @date 2020/5/1 16:35
 * @Description: 存储sql数据库的表的信息
 */
@Data
public class Table {
    private Map<String, Class> fieldType;
    private Map<String, String[]> data;
}
