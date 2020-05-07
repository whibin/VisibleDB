package com.whibin.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author whibin
 * @date 2020/5/1 15:56
 * @Description: 存放数据库相关的数据
 */
@Data
public class Database implements Serializable {
    /**
     * 表名对应一个具体的表实体
     */
    private Map<String, Table> tableMap;
}
