package com.ywt.common.alarm.pipeline;

import com.ywt.common.alarm.entity.Alarm;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public interface AlarmPipeline {

	/**
	 * push error messge to specified pipeline
	 *
	 * @param alarmDO: error param
	 */
	void send(Alarm alarmDO);

}
