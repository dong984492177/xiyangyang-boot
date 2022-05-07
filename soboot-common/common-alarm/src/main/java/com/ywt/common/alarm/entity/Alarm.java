package com.ywt.common.alarm.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.DigestUtils;

import com.ywt.common.alarm.constant.AlarmLevelEnum;
import com.ywt.common.alarm.constant.ProjectEnum;

import lombok.Data;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Alarm {

	private String title;

	private String detail;

	private String remark;

	private AlarmLevelEnum alarmLevel;

	private ProjectEnum projectId;

	private Date alarmDate;

	public String getUniqKey() {
		String key = title+"::"+detail;
		return DigestUtils.md5DigestAsHex(key.getBytes());
	}
}
