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
 * @Description: 分页排序类型
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Slf4j
public enum PageOrderType {
    ALL(-1, "全部"),

    ASC(0, "升序"),
    DESC(1, "降序"),
    ;

    private static final Object _LOCK = new Object();

    private static Map<Integer, PageOrderType> _MAP;
    private static List<PageOrderType> _LIST;
    private static List<PageOrderType> _ALL_LIST;

    static {
        synchronized (_LOCK) {
            Map<Integer, PageOrderType> map = new HashMap<>();
            List<PageOrderType> list = new ArrayList<>();
            List<PageOrderType> listAll = new ArrayList<>();
            for (PageOrderType type : PageOrderType.values()) {
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

    PageOrderType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public static PageOrderType get(int value) {
        try {
            return _MAP.get(value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static List<PageOrderType> list() {
        return _LIST;
    }

    public static List<PageOrderType> listAll() {
        return _ALL_LIST;
    }
}
