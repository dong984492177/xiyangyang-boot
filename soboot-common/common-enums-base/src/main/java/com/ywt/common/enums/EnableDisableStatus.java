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
 * @Description: 启用禁用
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Slf4j
public enum EnableDisableStatus {
    ALL(-1, "全部"),
    //    DEFAULT(0, "默认"),
    DISABLE(0, "禁用"),
    ENABLE(1, "启用");

    private static final Object _LOCK = new Object();

    private static Map<Integer, EnableDisableStatus> _MAP;
    private static List<EnableDisableStatus> _LIST;
    private static List<EnableDisableStatus> _ALL_LIST;

    static {
        synchronized (_LOCK) {
            Map<Integer, EnableDisableStatus> map = new HashMap<>();
            List<EnableDisableStatus> list = new ArrayList<>();
            List<EnableDisableStatus> listAll = new ArrayList<>();
            for (EnableDisableStatus value : EnableDisableStatus.values()) {
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

    private int value;
    private String name;

    EnableDisableStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public static EnableDisableStatus get(int value) {
        try {
            return _MAP.get(value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static List<EnableDisableStatus> list() {
        return _LIST;
    }

    public static List<EnableDisableStatus> listAll() {
        return _ALL_LIST;
    }
}
