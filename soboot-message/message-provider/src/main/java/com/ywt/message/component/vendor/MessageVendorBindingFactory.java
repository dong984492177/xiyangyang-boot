package com.ywt.message.component.vendor;

import javax.annotation.PostConstruct;

import com.ywt.message.component.vendor.executor.TclPushMessageVendorExecutor;
import com.ywt.message.component.vendor.executor.VirtualPushMessageVendorExecutor;
import com.ywt.message.component.vendor.executor.XgPushMessageVendorExecutor;
import com.ywt.message.constant.MessageType;
import com.ywt.message.constant.MessageVendorType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Component
public class MessageVendorBindingFactory {

    private Table<MessageVendorType, MessageType, BaseMessageVendorExecutor> vendorExecutorTable = HashBasedTable.create();

    @Autowired
    private VirtualPushMessageVendorExecutor virtualPushMessageVendorExecutor;
    @Autowired
    private XgPushMessageVendorExecutor xgPushMessageVendorExecutor;
    @Autowired
    private TclPushMessageVendorExecutor tclPushMessageVendorExecutor;

    @PostConstruct
    private void init() {
        vendorExecutorTable.put(MessageVendorType.VIRTUAL, MessageType.PUSH_NOTICE, virtualPushMessageVendorExecutor);
        vendorExecutorTable.put(MessageVendorType.VIRTUAL, MessageType.PUSH_MESSAGE, virtualPushMessageVendorExecutor);
        vendorExecutorTable.put(MessageVendorType.XG, MessageType.PUSH_NOTICE, xgPushMessageVendorExecutor);
        vendorExecutorTable.put(MessageVendorType.XG, MessageType.PUSH_MESSAGE, xgPushMessageVendorExecutor);
    }

    public BaseMessageVendorExecutor getExecutor(MessageVendorType messageVendorType, MessageType messageType) {
        return vendorExecutorTable.get(messageVendorType, messageType);
    }
}
