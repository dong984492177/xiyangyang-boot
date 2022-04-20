package com.ywt.common.enums;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public enum ConditionType {
    ALL(-1, "全部"),
    DEFAULT(0, "默认"),
    DATE_INTERVAL(1, "日期区间"),
    TIME_INTERVAL(4, "时间区间"),
    ;

    private static final Object _LOCK = new Object();

    private static Map<Integer, ConditionType> _MAP;
    private static List<ConditionType> _LIST;
    private static List<ConditionType> _ALL_LIST;

    static {
        synchronized (_LOCK) {
            Map<Integer, ConditionType> map = new HashMap<>();
            List<ConditionType> list = new ArrayList<>();
            List<ConditionType> listAll = new ArrayList<>();
            for (ConditionType value : ConditionType.values()) {
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

    ConditionType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public static ConditionType get(int value) {
        try {
            return _MAP.get(value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static List<ConditionType> list() {
        return _LIST;
    }

    public static List<ConditionType> listAll() {
        return _ALL_LIST;
    }
}
