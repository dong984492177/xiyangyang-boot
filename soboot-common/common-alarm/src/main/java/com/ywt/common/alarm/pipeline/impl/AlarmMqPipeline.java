package com.ywt.common.alarm.pipeline.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ywt.common.alarm.entity.Alarm;
import com.ywt.common.alarm.pipeline.AlarmPipeline;
import com.ywt.common.alarm.service.MQService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Slf4j
@Service
public class AlarmMqPipeline implements AlarmPipeline {

	@Autowired
    private MQService mqService;

    @Override
    public void send(Alarm alarmDO) {
        if (null == alarmDO) {
        	log.error("AlarmMQ.send alarmDO is null!");
            return;
        }
        try {
            mqService.sendAlarm(alarmDO);
        } catch (Exception e) {
        	log.error("send error!", e);
        }
    }

}
