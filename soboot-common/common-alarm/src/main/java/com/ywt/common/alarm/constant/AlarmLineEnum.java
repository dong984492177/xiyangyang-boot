package com.ywt.common.alarm.constant;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public enum AlarmLineEnum {

	/**
	 * realTime
	 */
	REAL_TIME(1, "realTime"),

	/**
	 * counter
	 */
	COUNTER(2, "counter");


	private int code;

	private String name;

	AlarmLineEnum(int code, String name) {
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
