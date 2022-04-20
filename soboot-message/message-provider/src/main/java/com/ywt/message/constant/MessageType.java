package com.ywt.message.constant;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @Author: huangchaoyang
 * @Description: 消息类型
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Slf4j
public enum MessageType {
    ALL(-1, "全部"),
    //    DEFAULT(0, "Default"),
    SMS(1, "短信"),
    PUSH_NOTICE(3, "推送通知"),
    PUSH_MESSAGE(4, "推送消息"),
    ;

    private static final Object _LOCK = new Object();

    private static Map<Integer, MessageType> _MAP;
    private static List<MessageType> _LIST;
    private static List<MessageType> _ALL_LIST;

    static {
        synchronized (_LOCK) {
            Map<Integer, MessageType> map = new HashMap<>();
            List<MessageType> list = new ArrayList<>();
            List<MessageType> listAll = new ArrayList<>();
            for (MessageType messageType : MessageType.values()) {
                map.put(messageType.getValue(), messageType);
                listAll.add(messageType);
                if (!messageType.equals(ALL)) {
                    list.add(messageType);
                }
            }

            _MAP = Collections.unmodifiableMap(map);
            _LIST = Collections.unmodifiableList(list);
            _ALL_LIST = Collections.unmodifiableList(listAll);
        }
    }

    private int value;
    private String name;

    MessageType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public static MessageType get(int value) {
        try {
            return _MAP.get(value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static List<MessageType> list() {
        return _LIST;
    }

    public static List<MessageType> listAll() {
        return _ALL_LIST;
    }
}
