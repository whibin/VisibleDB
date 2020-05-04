package com.whibin.domain.vo;

import lombok.Data;

import java.util.Map;

/**
 * @author whibin
 * @date 2020/5/4 14:04
 * @Description: redis数据库的实体类
 */
@Data
public class RedisDatabase {
    private Map<String, String> data;
}
