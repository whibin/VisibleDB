package com.whibin.web.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: Websocket
 * @author whibin
 */
@ServerEndpoint("/websocket")
public class SqlStatusWebSocket {
    String path = "F:/MyJavaProject/QG_Assessment/VisibleDatabase/out/artifacts/VisibleDatabase_war_exploded/UserData/SqlMessage.txt";

    @OnOpen
    public void onOpen(Session session) throws ClassNotFoundException {

    }

    @OnClose
    public void onClose() throws IOException {

    }

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
        // 判断是否要显示数据
        if ("GET".equals(message)) {
            System.out.println("收到显示数据的请求");
            if (messages != null) {
                // 以json格式发出
                ObjectMapper mapper = new ObjectMapper();
                String s = mapper.writeValueAsString(messages);
                session.getAsyncRemote().sendText(s);
            }
            return;
        }
        System.out.println("监听到数据库更新");
        // 若是存储数据，解析
        if (messages == null) {
            messages = new HashMap<>();
        }
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