package com.whibin.web.websocket.thread;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whibin.constant.Path;
import com.whibin.domain.vo.RedisDatabase;
import com.whibin.domain.vo.UserDatabase;
import com.whibin.util.jdbc.PropertiesUtil;

import javax.websocket.Session;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author whibin
 * @date 2020/5/7 22:36
 * @Description: 向前端页面发送redis数据库数据的线程
 */

public class RedisThread implements Runnable {

    private Session session;
    /**
     * 存放信息的集合
     */
    private Map<String, Map<String, Map<String, String>>> data;

    private ObjectInputStream inputStream;

    public RedisThread(Session session) {
        this.session = session;
    }

    /**
     * 用一个线程来持续发送信息
     */
    @Override
    public void run() {
        while (true) {
            data = readData();
            if (data != null) {
                try {
                    session.getAsyncRemote().sendText(new ObjectMapper().writeValueAsString(data));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            try {
                // 每隔10s刷新
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取数据
     * @return
     */
    private Map<String, Map<String, Map<String, String>>> readData() {
        Map<String, Map<String, Map<String, String>>> data = new HashMap<>();
        for (int i = 1; i <= Integer.parseInt(PropertiesUtil.getValue("maxActive")); i++) {
            String text = Path.PATH + "/userDatabase" + i + ".txt";
            try {
                // 从文件中读出
                inputStream = new ObjectInputStream(new FileInputStream(text));
                UserDatabase user = (UserDatabase) inputStream.readObject();
                Map<String, RedisDatabase> redisDatabaseMap = user.getRedisDatabaseMap();
                Map<String, Map<String, String>> tmp = new HashMap<>();
                for (Map.Entry<String, RedisDatabase> entry : redisDatabaseMap.entrySet()) {
                    tmp.put(entry.getKey(),entry.getValue().getData());
                }
                data.put(user.getUser().getUsername(),tmp);
            } catch (IOException | ClassNotFoundException e) {
                // 找不到就跳过
                continue;
            }
        }
        if (data.size() <= 0) {
            return null;
        }
        return data;
    }
}
