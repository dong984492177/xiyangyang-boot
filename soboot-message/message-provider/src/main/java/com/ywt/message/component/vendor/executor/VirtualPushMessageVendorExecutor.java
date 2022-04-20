package com.ywt.message.component.vendor.executor;

import java.util.List;
import java.util.Map;

import com.ywt.message.bean.MessageTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.ywt.message.api.exception.MessageSendException;
import com.ywt.message.component.vendor.AbstractBaseMessageVendorExecutor;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class VirtualPushMessageVendorExecutor extends AbstractBaseMessageVendorExecutor {

	@Override
    public void send(MessageTemplate messageTemplate, List<String> receiverList, Map<String, String> replacement) throws MessageSendException {
        try {
            String receiver = StringUtils.join(receiverList, ",");

            String content = this.replaceTemplateContent(messageTemplate.getContent(), replacement);

            log.info("发送虚拟文本短信消息成功, receiver={}, content={}", receiver, content);
        } catch (Exception e) {
            throw new MessageSendException("发送虚拟文本短信消息出错", e);
        }
    }

}
