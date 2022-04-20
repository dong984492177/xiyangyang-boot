package com.ywt.message.api.service;

import com.ywt.common.service.BaseApiService;

import java.util.List;
import java.util.Map;


public interface MessageSendApiService extends BaseApiService {

    void sendMessage(String templateKey, String hotelId, List<String> receiver, Map<String, String> replacement);

	/**
	 * 普通业务报警
	 * @param title
	 * @param detail
	 */
	void sendBizAlarmMessage(String title, String detail, String projectId);
}
