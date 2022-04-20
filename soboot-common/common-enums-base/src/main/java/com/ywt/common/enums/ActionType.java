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
 * @Description: 业务日志类型
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Slf4j
public enum ActionType {

    ALL("ALL", "0"),
    SYSTEM("系统","SYSTEM"),
    UNKNOWN("未知","UNKNOWN"),
    ;

    private static final Object _LOCK = new Object();

    private static Map<String, ActionType> _MAP;
    private static List<ActionType> _LIST;
    private static List<ActionType> _ALL_LIST;

    static {
        synchronized (_LOCK) {
            Map<String, ActionType> map = new HashMap<>();
            List<ActionType> list = new ArrayList<>();
            List<ActionType> listAll = new ArrayList<>();
            for (ActionType value : ActionType.values()) {
                map.put(value.getAliasName(), value);
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

    private String name;
    private String aliasName;

    ActionType(String name, String aliasName) {
        this.name = name;
        this.aliasName = aliasName;
    }

    public String getName() {
        return name;
    }

    public String getAliasName() {
        return aliasName;
    }

    public static ActionType get(String value) {
        try {
            return _MAP.get(value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static List<ActionType> list() {
        return _LIST;
    }

    public static List<ActionType> listAll() {
        return _ALL_LIST;
    }
}
