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
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Slf4j
public enum OperationType {

    ALL("ALL", "0"),
    CREATE("新增", "CREATE"),
    UPDATE("修改", "UPDATE"),
    DELETE("删除","DELETE"),
    QUERY("查询","QUERY"),
    UNKNOWN("未知","UNKNOWN"),
    ;

    private static final Object _LOCK = new Object();

    private static Map<String, OperationType> _MAP;
    private static List<OperationType> _LIST;
    private static List<OperationType> _ALL_LIST;

    static {
        synchronized (_LOCK) {
            Map<String, OperationType> map = new HashMap<>();
            List<OperationType> list = new ArrayList<>();
            List<OperationType> listAll = new ArrayList<>();
            for (OperationType value : OperationType.values()) {
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

    OperationType(String name, String aliasName) {
        this.name = name;
        this.aliasName = aliasName;
    }

    public String getName() {
        return name;
    }

    public String getAliasName() {
        return aliasName;
    }

    public static OperationType get(String value) {
        try {
            return _MAP.get(value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static List<OperationType> list() {
        return _LIST;
    }

    public static List<OperationType> listAll() {
        return _ALL_LIST;
    }
}
