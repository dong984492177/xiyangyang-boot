package com.ywt.message.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.ywt.common.enums.EnableDisableStatus;
import com.ywt.message.bean.MessageVendor;
import com.ywt.message.constant.MessageType;
import com.ywt.message.constant.MessageVendorType;
import com.ywt.message.service.MessageVendorApiService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageVendorApiServiceImpl implements MessageVendorApiService{
    private final static Map<Integer, MessageVendor> vendors = new HashMap<>();

    static {
        
        MessageVendor xgVendor = new MessageVendor();
        xgVendor.setId(2);
        xgVendor.setName("信鸽");
        xgVendor.setAccessUrl("https://cloud.tencent.com/document/product/548/39064");
        xgVendor.setEnableStatus(EnableDisableStatus.ENABLE);
        xgVendor.setVendorType(MessageVendorType.XG);
        xgVendor.setMessageType(MessageType.PUSH_MESSAGE);
        vendors.put(2, xgVendor);

    }

    @Override
    public MessageVendor getById(Integer id) {
        return vendors.get(id);
    }
}
