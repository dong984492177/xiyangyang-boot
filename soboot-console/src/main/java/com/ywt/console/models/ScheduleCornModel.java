package com.ywt.console.models;

import lombok.Data;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Data
public class ScheduleCornModel {

    /**
     * 所选类型:
     * 1  -> 每天
     * 2  -> 每周
     * 3  -> 每月
     */
    private Integer jobType;

    /**
     * 一年的哪几个月
     */
    private Integer[] monthOfYears;

    /**
     * 一周的哪几天
     */
    private Integer[] dayOfWeeks;

    /**
     * 一个月的哪几天
     */
    private Integer[] dayOfMonths;

    /**
     * 一天的哪个时间点
     */
    private Integer[] hourOfDays;

    /**
     * 间隔多少分钟执行一次
     */
    private Integer intervalLength;
    /**
     * 是否间隔
     */
    private Boolean isInterval;
    /**
     * 秒
     */
    private Integer second;

    /**
     * 分
     */
    private Integer minute;

    /**
     * 时
     */
    private Integer hour;

    /**
     * 年
     */
    private Integer year;

    /**
     * 月
     */
    private Integer month;

    /**
     * 日
     */
    private Integer day;

    /**
     * 全年
     */
    private Boolean yearly;
    /**
     * 全星期
     */
    private Boolean weekly;
    /**
     * 全时段
     */
    private Boolean hourly;
    /**
     * 全月
     */
    private Boolean monthly;
}
