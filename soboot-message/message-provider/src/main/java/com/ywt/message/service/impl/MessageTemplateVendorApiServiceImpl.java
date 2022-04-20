package com.ywt.message.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ywt.common.enums.EnableDisableStatus;
import com.ywt.message.bean.MessageTemplateVendor;
import com.ywt.message.service.MessageTemplateVendorApiService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageTemplateVendorApiServiceImpl implements MessageTemplateVendorApiService {
    private final static Map<Integer, List<MessageTemplateVendor>> tempVendors = new HashMap<>();

    static {
        MessageTemplateVendor messageTemplateVendor = new MessageTemplateVendor();
        messageTemplateVendor.setId(1);
        messageTemplateVendor.setTemplateId(1);
        messageTemplateVendor.setVendorId(1);
        messageTemplateVendor.setEnableStatus(EnableDisableStatus.ENABLE);
        List<MessageTemplateVendor> messageTemplateVendors = new ArrayList<>();
        messageTemplateVendors.add(messageTemplateVendor);
        tempVendors.put(1, messageTemplateVendors);

        MessageTemplateVendor messageTemplateVendor2 = new MessageTemplateVendor();
        messageTemplateVendor2.setId(2);
        messageTemplateVendor2.setTemplateId(2);
        messageTemplateVendor2.setVendorId(1);
        messageTemplateVendor2.setEnableStatus(EnableDisableStatus.ENABLE);
        List<MessageTemplateVendor> messageTemplateVendors2 = new ArrayList<>();
        messageTemplateVendors2.add(messageTemplateVendor);
        tempVendors.put(2, messageTemplateVendors2);
    }
    @Override
    public List<MessageTemplateVendor> findByTemplateId(Integer templateId) {
        return tempVendors.get(templateId);
    }
}
