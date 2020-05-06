package com.whibin.web.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whibin.domain.vo.RedisDatabase;
import com.whibin.domain.vo.UserDatabase;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author whibin
 * @date 2020/5/7 0:14
 * @Description: 实时更新redis数据库的websocket
 */

@ServerEndpoint("/websocketRedis")
public class RedisListenWebSocket {
    String path = "F:/MyJavaProject/QG_Assessment/VisibleDatabase/out/artifacts/VisibleDatabase_war_exploded/UserData";

    @OnOpen
    public void onOpen(Session session) throws ClassNotFoundException {

    }

    @OnClose
    public void onClose() throws IOException {

    }

    /**
     * 更新数据
     * @param session
     * @throws IOException
     */
    @OnMessage
    public void onMessage(String message, Session session) throws JsonProcessingException {
        Map<String, Map<String, Map<String, String>>> data = new HashMap<>();
        for (int i = 1; i <= 10; i++) {
            String text = path + "/userDatabase" + i + ".txt";
            try {
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(text));
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
        System.out.println(data);
        session.getAsyncRemote().sendText(new ObjectMapper().writeValueAsString(data));
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
