package com.ywt.common.enums;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: huangchaoyang
 * @Description: WebSocket消息类型
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Slf4j
public enum WebSocketMessageType {
    ALL(-1, "全部"),
    GATEWAY_LOG(1, "主机日志"),
    DEVICE_STATUS(2, "设备状态"),
    HOTEL_MESSAGE(3,"客需消息"),
    ROOM_STATUS_MESSAGE(4,"房间状态"),
    AC_STATUS_MESSAGE(5,"空调状态"),
    ;

    private static final Object _LOCK = new Object();

    private static Map<Integer, WebSocketMessageType> _MAP;
    private static List<WebSocketMessageType> _LIST;
    private static List<WebSocketMessageType> _ALL_LIST;

    static {
        synchronized (_LOCK) {
            Map<Integer, WebSocketMessageType> map = new HashMap<>();
            List<WebSocketMessageType> list = new ArrayList<>();
            List<WebSocketMessageType> listAll = new ArrayList<>();
            for (WebSocketMessageType value : WebSocketMessageType.values()) {
                map.put(value.getValue(), value);
                listAll.add(value);
                if (!value.equals(ALL)) {
                    list.add(value);
                }
            }

            _MAP = ImmutableMap.copyOf(map);
            _LIST = ImmutableList.copyOf(list);
            _ALL_LIST = ImmutableList.copyOf(listAll);
        }
    }

    private Integer value;
    private String name;


    WebSocketMessageType(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }

    public static WebSocketMessageType get(Integer value) {
        try {
            return _MAP.get(value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static List<WebSocketMessageType> list() {
        return _LIST;
    }

    public static List<WebSocketMessageType> listAll() {
        return _ALL_LIST;
    }
}
