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
 * @Description: 业务日志用户类型
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Slf4j
public enum TargetType {

    ALL("ALL", "0"),
    USER("用户", "1"),
    INTERFACE("接口", "2"),
    ;

    private static final Object _LOCK = new Object();

    private static Map<String, TargetType> _MAP;
    private static List<TargetType> _LIST;
    private static List<TargetType> _ALL_LIST;

    static {
        synchronized (_LOCK) {
            Map<String, TargetType> map = new HashMap<>();
            List<TargetType> list = new ArrayList<>();
            List<TargetType> listAll = new ArrayList<>();
            for (TargetType value : TargetType.values()) {
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

    TargetType(String name, String aliasName) {
        this.name = name;
        this.aliasName = aliasName;
    }

    public String getName() {
        return name;
    }

    public String getAliasName() {
        return aliasName;
    }

    public static TargetType get(String value) {
        try {
            return _MAP.get(value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static List<TargetType> list() {
        return _LIST;
    }

    public static List<TargetType> listAll() {
        return _ALL_LIST;
    }
}
