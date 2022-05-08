package com.ywt.console.constant;

/**
 * @Author: huangchaoyang
 * @Description: 提醒时间枚举
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public enum NoticeTypeEnum {

	DAY(1, "按指定日期"),
	WEEK(2, "按周"),
	MONTH(3, "按月"),
	YEAR(4, "按年");

	public Integer value;
	public String name;

	NoticeTypeEnum(Integer value, String name) {
		this.value = value;
		this.name = name;
	}
}
