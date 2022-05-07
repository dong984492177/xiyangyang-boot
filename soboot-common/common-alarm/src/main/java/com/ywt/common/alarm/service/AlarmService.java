package com.ywt.common.alarm.service;

import com.ywt.common.alarm.constant.AlarmLevelEnum;
import com.ywt.common.alarm.constant.AlarmLineEnum;
import com.ywt.common.alarm.constant.ProjectEnum;
import com.ywt.common.alarm.entity.*;
import com.ywt.common.alarm.pipeline.impl.AlarmMqPipeline;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Slf4j
@Service
public class AlarmService {

    /**
     * system error global counter
     * use getCounterMap(),can iterator all errors on running project.
     */
    private ConcurrentMap<String, AlarmCounter> counterMap = new ConcurrentHashMap<>();

    private static final Long MILLISECONDS_IN_ONEMINUTE = 60 * 1000L;

    @Autowired
    private AlarmMqPipeline alarmMqPipeline;

    /**
     * 普通业务异常 实时报警
     * @param title
     * @param detail
     */
    public void sendAlarm(String title, String detail, ProjectEnum projectId) {
    	AlarmLine alarmLine = new AlarmLine();
    	alarmLine.setAlarmLineEnum(AlarmLineEnum.REAL_TIME);
    	Alarm alarm = new Alarm();
    	alarm.setAlarmLevel(AlarmLevelEnum.ERROR);
    	alarm.setTitle(title);
    	alarm.setDetail(detail);
    	alarm.setProjectId(projectId);
    	alarm.setAlarmDate(new Date());
    	sendAlarm(alarm, alarmLine);
    }

    /**
     * 普通业务异常 阈值报警
     * @param title
     * @param detail
     * @param interval  时间间隔  单位：分钟
     * @param threshold 阈值
     */
    public void sendAlarm(String title, String detail, Integer interval, Integer threshold, ProjectEnum projectId) {
    	AlarmLine alarmLine = new AlarmLine();
    	alarmLine.setInterval(interval);
    	alarmLine.setThreshold(threshold);
    	alarmLine.setAlarmLineEnum(AlarmLineEnum.COUNTER);
    	Alarm alarm = new Alarm();
    	alarm.setAlarmLevel(AlarmLevelEnum.WARNING);
    	alarm.setTitle(title);
    	alarm.setDetail(detail);
    	alarm.setProjectId(projectId);
    	alarm.setAlarmDate(new Date());
    	sendAlarm(alarm, alarmLine);
    }

    /**
     * error send  to specified channel
     *
     * @param alarmLine: error trigger params(实时告警 | 阈值告警)
     */
    public void sendAlarm(Alarm alarm, AlarmLine alarmLine) {
        try {
        	 AlarmLineEnum alarmLineEnum = alarmLine.getAlarmLineEnum();
            if (alarmLineEnum == AlarmLineEnum.REAL_TIME) {
            	alarmMqPipeline.send(alarm);
            } else {
                AlarmLevelEnum flag = alarmTrigger(alarm.getUniqKey(), alarmLine) ? AlarmLevelEnum.ERROR : AlarmLevelEnum.WARNING;
                if (flag == AlarmLevelEnum.ERROR) {
                    alarm.setAlarmLevel(flag);
                    alarmMqPipeline.send(alarm);
                }else {
                	log.error("Alarm alarmLine is warn,alarm:{} ",alarm);
                }
            }
        } catch (Exception e) {
        	log.error("sendAlarm error!", e);
        }
    }


    /**
     * 阈值告警判断触发器
     *
     * @param alarmLine: error trigger params
     * @return true(error 级别告警) | false(warning 级别告警)
     */
    private boolean alarmTrigger(String alarmKey, AlarmLine alarmLine) {

        AlarmCounter alarmCounter = counterMap.get(alarmKey);
        Long currentTime = System.currentTimeMillis();

        //不存在error计数，初始化
        if (null == alarmCounter) {
            firstErrorInit(alarmKey, currentTime);
            return false;
        } else {
            long entranceTime = alarmCounter.getEntranceTime();
            int count = alarmCounter.getCount();

            //业务系统自定义（间隔时间 + 告警阈值）
            int interval = alarmLine.getInterval();
            int threshold = alarmLine.getThreshold();

            boolean inMinutes = ((currentTime - entranceTime) / MILLISECONDS_IN_ONEMINUTE) < interval;

            //超过“间隔时间”，并且告警次数小于“告警阈值”
            //从 counterMap 移除，并新加入当前 error
            // warning 级别告警
            if (!inMinutes) {
                // empty
                counterMap.remove(alarmKey);
                //insert
                firstErrorInit(alarmKey, currentTime);
                //warning
                return false;
            }
            // 未超过“间隔时间”，并且告警次数等于“告警阈值”
            // error 级别告警
            // countMap 移除已报警的error计数器
            else if (count + 1 >= threshold) {
                // empty
                counterMap.remove(alarmKey);
                return true;
            }
            // 未超过“间隔时间”，并且告警次数小于“告警阈值”
            // warning 级别告警
            else {
                alarmCounter.setCount(alarmCounter.getCount() + 1);
                counterMap.put(alarmKey, alarmCounter);
                return false;
            }
        }
    }

    /**
     * 阈值告警
     *
     * @param keyMd5:      alaram message global key
     * @param currentTime: 系统当前时间戳
     * @return AlarmCounter:告警计数器
     */
    private AlarmCounter firstErrorInit(String keyMd5, Long currentTime) {
        AlarmCounter ac = new AlarmCounter();
        ac.setCount(1);
        ac.setEntranceTime(currentTime);
        ac.setAlarmKey(keyMd5);
        counterMap.put(keyMd5, ac);

        return ac;
    }

    public ConcurrentMap<String, AlarmCounter> getCounterMap() {
        return counterMap;
    }

}
