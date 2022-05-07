package com.ywt.common.alarm.constant;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public enum AlarmLevelEnum {

	/**
	 * info level
	 */
	INFO(3, "info"),

	/**
	 * warning level
	 */
	WARNING(2, "warning"),

	/**
	 * error level
	 */
	ERROR(1, "error");


	private int code;

	private String name;

	AlarmLevelEnum(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return code;
	}


	public String getName() {
		return name;
	}
}
