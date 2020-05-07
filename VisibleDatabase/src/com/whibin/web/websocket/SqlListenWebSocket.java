package com.whibin.web.websocket;

import com.whibin.web.websocket.thread.SqlThread;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * @author whibin
 * @date 2020/5/7 23:58
 * @Description: 向前端页面发送sql数据库状态信息
 */

@ServerEndpoint("/websocketSql")
public class SqlListenWebSocket {
    /**
     * 开启信息发送
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        new Thread(new SqlThread(session)).start();
    }

    @OnClose
    public void onClose() {}
}
