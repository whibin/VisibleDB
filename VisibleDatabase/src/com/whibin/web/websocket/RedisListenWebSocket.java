package com.whibin.web.websocket;

import com.whibin.web.websocket.thread.RedisThread;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * @author whibin
 * @date 2020/5/7 0:14
 * @Description: 实时更新redis数据库的websocket
 */

@ServerEndpoint("/websocketRedis")
public class RedisListenWebSocket {
    /**
     * 开启信息发送
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        new Thread(new RedisThread(session)).start();
    }

    @OnClose
    public void onClose() {}
}
