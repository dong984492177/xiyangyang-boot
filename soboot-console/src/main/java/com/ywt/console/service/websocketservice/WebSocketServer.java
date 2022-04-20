package com.ywt.console.service.websocketservice;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.ywt.common.base.constant.XiotConstant;
import com.ywt.common.config.redis.RedissonService;
import com.ywt.common.enums.DeleteStatus;
import com.ywt.common.enums.WebSocketMessageType;
import com.ywt.console.config.WebsocketConfig;
import com.ywt.console.entity.SysUser;
import com.ywt.console.models.reqmodel.systemreqmodels.QueryUserDetailReqModel;
import com.ywt.console.models.resmodel.systemresmodels.QueryUserDetailResModel;
import com.ywt.console.service.ISysUserService;
import com.ywt.console.service.SystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@ServerEndpoint(value = "/webSocket/{token}/{msgType}/{requestId}", configurator = WebsocketConfig.class)
@Component
@Slf4j
public class WebSocketServer {

    /**
     * 所有连接的客户端
     */
    public static Map<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    /**
     * 连接服务的客户端个数
     */
    public static Map<WebSocketMessageType, AtomicInteger> countMap = new ConcurrentHashMap<>();

    private final static Object __INCREMENT_Lock__ = new Object();

    private final static Object __DECREMENT_Lock__ = new Object();

    static {
        WebSocketMessageType.list().forEach(webSocketMessageType -> {
            if (countMap.get(webSocketMessageType) == null) {
                countMap.put(webSocketMessageType, new AtomicInteger(0));
            }
        });
    }

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    private String token;
    private String requestId;
    private Integer msgType;
    private WebSocketMessageType messageType;

    private String gatewayMac;

    private List<Integer> hotelIds;

    private static RedissonService redissonService;

    private static ISysUserService sysUserService;

    private static SystemService systemService;

    public List<Integer> getHotelIds() {
        return hotelIds;
    }


    @Autowired
    public  void setSystemService(SystemService systemService) {
        WebSocketServer.systemService = systemService;
    }

    @Autowired
    public void setRedissonService(RedissonService redissonService) {
        WebSocketServer.redissonService = redissonService;
    }

    @Autowired
    public void setSysUserService(ISysUserService sysUserService) {
        WebSocketServer.sysUserService = sysUserService;
    }

    public String getGatewayMac() {
        return gatewayMac;
    }

    public WebSocketMessageType getMessageType() {
        return messageType;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token, @PathParam("msgType") Integer msgType, @PathParam("requestId") String requestId) {
        try {
            List<SysUser> users = new LambdaQueryChainWrapper<>(sysUserService.getBaseMapper()).eq(SysUser::getPhonenumber, token).eq(SysUser::getIsDelete, DeleteStatus.NOT_DELETE).list();
            if (CollectionUtils.isEmpty(users)) {
                log.error("token异常");
                session.close();
            } else {
                this.session = session;
                this.token = token;
                this.requestId = requestId;
                this.msgType = msgType;

                SysUser sysUser = users.get(0);
                QueryUserDetailReqModel queryUserDetailReqModel = QueryUserDetailReqModel.builder().userId(sysUser.getId()).build();
                QueryUserDetailResModel queryUserDetailResModel  = systemService.getUserDetail(queryUserDetailReqModel);
                this.hotelIds = queryUserDetailResModel.getHotelIds();

                connect();
            }
        } catch (Exception e) {
            log.error("onOpen error:" + e.toString());
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        disConnect();
        log.info("**********************" + token + requestId);
    }


    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        WebSocketMessage webSocketMessage = new WebSocketMessage();
        switch (messageType) {
            //客需消息
            case HOTEL_MESSAGE:
                if (StringUtils.isEmpty(message)) {
                    webSocketMessage.setMsg("非法参数");
                    sendMessage(webSocketMessage);
                    return;
                }
                webSocketMessage.setMsg("HOTEL_MESSAGE");
                sendMessage(session, webSocketMessage.getMsg());
                break;
            case ROOM_STATUS_MESSAGE:
                if (StringUtils.isEmpty(message)) {
                    webSocketMessage.setMsg("非法参数");
                    sendMessage(webSocketMessage);
                    return;
                }
                webSocketMessage.setMsg("ROOM_STATUS_MESSAGE");
                sendMessage(session, webSocketMessage.getMsg());
                break;
            default:
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        disConnect();
        log.error("webSocket 出现错误了:" + error.getMessage());
    }

    /**
     * 实现服务器主动发送消息
     */
    public void sendMessage(WebSocketMessage message){
        webSocketMap.forEach((k, v) -> {
            if (session.getId().equals(v.session.getId()) && v.session.isOpen()) {
                //if (gatewayMac.equalsIgnoreCase(message.getMacAddress())) {
                    sendMessage(v.session, message.getMsg());
                //}
            }
        });
    }

    private void sendMessage(Session session, String message) {
        String msg = message.replaceAll("\n", "<br/>");
//        try {
//            session.getBasicRemote().sendText(msg);
            session.getAsyncRemote().sendText(msg);
//        } catch (IOException e) {
//            log.error("发送webSocket消息错误。", e);
//        }
    }

    /**
     * 将新加入的会话保存
     */
    private void connect() {
        messageType = WebSocketMessageType.get(msgType);
        if (messageType == null) {
            sendMessage(this.session, "消息类型错误");
            disConnect();
        }
        incrementCount(requestId);
    }

    private void disConnect() {
       decrementCount(requestId);
        try {
            session.close();
        } catch (IOException e) {
            log.error("关闭webSocket错误。", e);
        }

        if (messageType == WebSocketMessageType.DEVICE_STATUS) {
            if (!StringUtils.isEmpty(gatewayMac)) {
                redissonService.deleteString(XiotConstant.WEB_SOCKET_CLIENT_DEVICE_LINKED + ":" + gatewayMac);
            }
        }
    }

    private void incrementCount(String requestId) {
        if (requestId == null || webSocketMap.containsKey(requestId)) {
            return;
        }

        synchronized(__INCREMENT_Lock__) {
            if (!webSocketMap.containsKey(requestId)) {
                webSocketMap.put(requestId, this);
                countMap.get(messageType).incrementAndGet();
                log.info("[{}]连接后，[{}]当前连接数：{}",  requestId, messageType.getName(), countMap.get(messageType).get());
            }
        }
    }

    private void decrementCount(String requestId) {
        if (requestId == null || !webSocketMap.containsKey(requestId)) {
            return;
        }

        synchronized(__DECREMENT_Lock__) {
            if (webSocketMap.containsKey(requestId)) {
                webSocketMap.remove(requestId);
                countMap.get(messageType).decrementAndGet();
                log.info("[{}]断开连接后，[{}]当前连接数：{}",  requestId, messageType.getName(), countMap.get(messageType).get());

                if (countMap.get(WebSocketMessageType.GATEWAY_LOG).get() == 0) {
                    redissonService.setString(XiotConstant.WEB_SOCKET_CLIENT_LINKED + ":" + gatewayMac, "false");
                }
            }
        }
    }

}
