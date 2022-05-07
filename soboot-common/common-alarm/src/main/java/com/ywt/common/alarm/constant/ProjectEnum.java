package com.ywt.common.alarm.constant;

/**
 * @Author: huangchaoyang
 * @Description:  工程枚举
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public enum ProjectEnum {

    YWT_CONSOLE("ywt_console");


    private String name;

    ProjectEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
