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
 * @Description: 加密类型
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Slf4j
public enum EncryptType {
    ALL(-1, "全部"),
    NONE(0, "不加密"),
    AES_CBC_PKCS5PADDING(1, "AES/CBC/PKCS5PADDING"),
    ;

    private static final Object _LOCK = new Object();

    private static Map<Integer, EncryptType> _MAP;
    private static List<EncryptType> _LIST;
    private static List<EncryptType> _ALL_LIST;

    static {
        synchronized (_LOCK) {
            Map<Integer, EncryptType> map = new HashMap<>();
            List<EncryptType> list = new ArrayList<>();
            List<EncryptType> listAll = new ArrayList<>();
            for (EncryptType signType : EncryptType.values()) {
                map.put(signType.getValue(), signType);
                listAll.add(signType);
                if (!signType.equals(ALL)) {
                    list.add(signType);
                }
            }

            _MAP = ImmutableMap.copyOf(map);
            _LIST = ImmutableList.copyOf(list);
            _ALL_LIST = ImmutableList.copyOf(listAll);
        }
    }

    private int value;
    private String name;

    EncryptType(int value, String name){
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getValue(){
        return value;
    }

    public static EncryptType get(int value){
        try{
            return _MAP.get(value);
        }catch(Exception e){
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static List<EncryptType> list() {
        return _LIST;
    }

    public static List<EncryptType> listAll() {
        return _ALL_LIST;
    }
}
