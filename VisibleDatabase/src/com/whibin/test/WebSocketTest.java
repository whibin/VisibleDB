package com.whibin.test;

import org.apache.log4j.Logger;

import java.io.IOException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * @Class: Test
 * @Description: 简单websocket demo
 * @author 九风萍舟
 */
@ServerEndpoint(value="/websocketTest/{userId}")
public class WebSocketTest {
    private Logger logger = Logger.getLogger(WebSocketTest.class);

    private static String userId;

    //连接时执行
    @OnOpen
    public void onOpen(@PathParam("userId") String userId,Session session) throws IOException{
        this.userId = userId;
        System.out.println("新连接：{"+userId+"}");
    }

    //关闭时执行
    @OnClose
    public void onClose(){
        System.out.println("新连接：{"+userId+"}关闭");
    }

    //收到消息时执行
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.println("收到用户{"+this.userId+"}的消息{"+message+"}");
        session.getAsyncRemote().sendText("收到 "+this.userId+" 的消息 "); //回复用户
    }

    //连接错误时执行
    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("用户id为：{"+this.userId+"}的连接发送错误");
        error.printStackTrace();
    }

}