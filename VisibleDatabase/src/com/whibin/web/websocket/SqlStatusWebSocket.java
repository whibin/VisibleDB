package com.whibin.web.websocket;

import com.whibin.constant.Path;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 实时监听sql数据库更新状态的websocket
 * @author whibin
 */
@ServerEndpoint("/websocket")
public class SqlStatusWebSocket {
    /**
     * 文件存放路径
     */
    String path = Path.PATH + "/SqlMessage.txt";

    /**
     * 监听更新信息
     * @param session
     * @throws IOException
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {

        Map<String, List<String>> messages = null;
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(path));
            // 读取
            messages = (Map<String, List<String>>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // 若出异常，则说明找不到。不做任何处理
        }
        System.out.println("监听到数据库更新");
        // 存储数据，解析
        if (messages == null) {
            messages = new HashMap<>();
        }
        // 解析固定的格式
        String[] split = message.split("-");
        String databaseName = split[0];
        String msg = split[1];
        List<String> stringList = messages.get(databaseName);
        if (stringList == null) {
            stringList = new ArrayList<>();
        }
        // 存入
        stringList.add(msg);
        messages.put(databaseName,stringList);
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(path));
        outputStream.writeObject(messages);
    }

    /**
     * 遇到异常打印
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error){
        error.printStackTrace();
    }
}