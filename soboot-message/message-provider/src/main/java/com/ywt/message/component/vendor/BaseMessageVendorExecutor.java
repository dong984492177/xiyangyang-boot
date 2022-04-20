package com.ywt.message.component.vendor;

import java.util.List;
import java.util.Map;

import com.ywt.message.api.exception.MessageSendException;
import com.ywt.message.bean.MessageTemplate;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public interface BaseMessageVendorExecutor {

    /**
     * 发送消息
     * @param messageTemplate 消息模板
     * @param receiverList 消息供应商
     * @param replacement 发送消息任务
     * @throws MessageSendException
     */
    void send(MessageTemplate messageTemplate, List<String> receiverList, Map<String, String> replacement) throws MessageSendException;
}
