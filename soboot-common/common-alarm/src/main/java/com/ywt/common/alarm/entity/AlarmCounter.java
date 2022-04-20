package com.ywt.common.alarm.entity;


/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public class AlarmCounter {

	/**
	 * alaram key
	 */
	private String alarmKey;

	/**
	 * error occur first timestamps
	 */
	private Long entranceTime;

	/**
	 * error occur times
	 */
	private Integer count;

	public String getAlarmKey() {
		return alarmKey;
	}

	public void setAlarmKey(String alarmKey) {
		this.alarmKey = alarmKey;
	}

	public Long getEntranceTime() {
		return entranceTime;
	}

	public void setEntranceTime(Long entranceTime) {
		this.entranceTime = entranceTime;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
}
