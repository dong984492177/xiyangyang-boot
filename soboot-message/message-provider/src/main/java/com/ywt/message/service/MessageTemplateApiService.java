package com.ywt.message.service;

import com.ywt.common.service.BaseApiService;
import com.ywt.message.bean.MessageTemplate;

public interface MessageTemplateApiService extends BaseApiService {

    // 根据模板key查询消息模板
    MessageTemplate getByTemplateKey(String templateKey, String hotelId);
}
