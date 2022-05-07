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
 */
@Slf4j
public enum DeleteStatus {
    ALL(-1, "全部"),
    //    DEFAULT(0, "默认"),
    DELETE(1, "删除"),
    NOT_DELETE(0, "未删除");

    private static final Object _LOCK = new Object();

    private static Map<Integer, DeleteStatus> _MAP;
    private static List<DeleteStatus> _LIST;
    private static List<DeleteStatus> _ALL_LIST;

    static {
        synchronized (_LOCK) {
            Map<Integer, DeleteStatus> map = new HashMap<>();
            List<DeleteStatus> list = new ArrayList<>();
            List<DeleteStatus> listAll = new ArrayList<>();
            for (DeleteStatus value : DeleteStatus.values()) {
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

    DeleteStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public static DeleteStatus get(int value) {
        try {
            return _MAP.get(value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static List<DeleteStatus> list() {
        return _LIST;
    }

    public static List<DeleteStatus> listAll() {
        return _ALL_LIST;
    }
}
