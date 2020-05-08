package com.whibin.web.websocket.thread;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whibin.constant.Path;

import javax.websocket.Session;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Map;

/**
 * @author whibin
 * @date 2020/5/7 23:59
 * @Description: 向前端发送数据库状态信息的线程
 */

public class SqlThread implements Runnable {
    /**
     * 文件存放路径
     */
    private String path = Path.PATH + "/SqlMessage.txt";

    private Session session;
    /**
     * 存放信息的集合
     */
    private Map<String, List<String>> messages;

    private ObjectInputStream inputStream;

    public SqlThread(Session session) {
        this.session = session;
    }

    @Override
    public void run() {
        while (true) {
            messages = readData();
            if (messages != null) {
                try {
                    session.getAsyncRemote().sendText(new ObjectMapper().writeValueAsString(messages));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            try {
                // 隔10s刷新
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
    private Map<String, List<String>> readData() {
        try {
            inputStream = new ObjectInputStream(new FileInputStream(path));
            // 读取
            return (Map<String, List<String>>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // 若出异常，则说明找不到
        }
        return null;
    }
}
