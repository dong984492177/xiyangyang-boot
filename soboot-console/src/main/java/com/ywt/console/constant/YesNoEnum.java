package com.ywt.console.constant;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public enum YesNoEnum {
	NO(0, "否"),
	YES(1, "是");

	public Integer value;
	public String name;

	YesNoEnum(Integer value, String name) {
		this.value = value;
		this.name = name;
	}
}
