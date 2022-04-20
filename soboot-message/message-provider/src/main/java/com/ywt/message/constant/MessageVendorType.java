package com.ywt.message.constant;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: huangchaoyang
 * @Description: 消息供应商类型
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Slf4j
public enum MessageVendorType {
    ALL(-1, "全部"),

    DEFAULT(0, "默认"),
    VIRTUAL(1, "虚拟"), // 用于开发环境等不实际发送消息

    XG(2, "信鸽"),
    CHUANGLAN(3, "创蓝"),

    ;

    private static final Object _LOCK = new Object();

    private static Map<Integer, MessageVendorType> _MAP;
    private static List<MessageVendorType> _LIST;
    private static List<MessageVendorType> _ALL_LIST;

    static {
        synchronized (_LOCK) {
            Map<Integer, MessageVendorType> map = new HashMap<>();
            List<MessageVendorType> list = new ArrayList<>();
            List<MessageVendorType> listAll = new ArrayList<>();
            for (MessageVendorType type : MessageVendorType.values()) {
                map.put(type.getValue(), type);
                listAll.add(type);
                if (!type.equals(ALL)) {
                    list.add(type);
                }
            }

            _MAP = ImmutableMap.copyOf(map);
            _LIST = ImmutableList.copyOf(list);
            _ALL_LIST = ImmutableList.copyOf(listAll);
        }
    }

    private int value;
    private String name;

    MessageVendorType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public static MessageVendorType get(int value) {
        try {
            return _MAP.get(value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static List<MessageVendorType> list() {
        return _LIST;
    }

    public static List<MessageVendorType> listAll() {
        return _ALL_LIST;
    }
}
