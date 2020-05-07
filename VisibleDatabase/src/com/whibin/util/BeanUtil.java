package com.whibin.util;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author whibin
 * @date 2020/4/21 12:44
 * @Description: 实现对javabean对象的属性赋值
 */

public class BeanUtil {
    /**
     * 根据传入的map对对象进行赋值
     * @param object
     * @param map
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static void populate(Object object, Map<String, Object> map) throws NoSuchFieldException, IllegalAccessException {
        // 获得其类
        Class<?> clz = object.getClass();
        // 遍历集合，设置属性值
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Field field;
            try {
                field = clz.getDeclaredField(entry.getKey());
            } catch (NoSuchFieldException e) {
                // 若找不到该字段，说明没有，此时跳过即可
                continue;
            }
            field.setAccessible(true);
            // 特殊判断
            if ("id".equals(field.getName())) {
                field.set(object,Long.valueOf((String) entry.getValue()));
                continue;
            }
            field.set(object, entry.getValue());
        }
    }

    /**
     * 将传入的parameterMap集合的值赋值到所指定的对象中
     * @param object
     * @param map
     * @throws IllegalAccessException
     */
    public static void populateParameter(Object object, Map<String, String[]> map) throws IllegalAccessException {
        Class<?> clz = object.getClass();
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            Field field;
            try {
                field = clz.getDeclaredField(entry.getKey());
            } catch (NoSuchFieldException e) {
                continue;
            }
            field.setAccessible(true);
            field.set(object, entry.getValue()[0]);
        }
    }
}
