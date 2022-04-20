package com.ywt.message.service;

import java.util.List;

import com.ywt.common.service.BaseApiService;
import com.ywt.message.bean.MessageTemplateVendor;

public interface MessageTemplateVendorApiService extends BaseApiService {

    // 根据模板ID查询MessageTemplateVendor列表
    List<MessageTemplateVendor> findByTemplateId(Integer templateId);
}
