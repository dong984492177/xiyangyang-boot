package com.ywt.common.alarm.entity;

import com.ywt.common.alarm.constant.AlarmLineEnum;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public class AlarmLine {

	/**
	 * 时间间隔，单位（分钟）
	 */
	private Integer interval;

	/**
	 * 告警阈值
	 */
	private Integer threshold;

	/**
	 * 告警类型枚举
	 * real_time(实时告警) | counter（阈值告警）
	 */
	private AlarmLineEnum alarmLineEnum;

	public Integer getInterval() {
		return interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	public Integer getThreshold() {
		return threshold;
	}

	public void setThreshold(Integer threshold) {
		this.threshold = threshold;
	}

	public AlarmLineEnum getAlarmLineEnum() {
		return alarmLineEnum;
	}

	public void setAlarmLineEnum(AlarmLineEnum alarmLineEnum) {
		this.alarmLineEnum = alarmLineEnum;
	}
}
