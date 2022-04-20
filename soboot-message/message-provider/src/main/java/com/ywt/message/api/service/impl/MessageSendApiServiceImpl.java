package com.ywt.message.api.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ywt.common.enums.EnableDisableStatus;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.ywt.common.base.util.CoreJsonUtils;
import com.ywt.message.api.service.MessageSendApiService;
import com.ywt.message.bean.MessageTemplate;
import com.ywt.message.bean.MessageTemplateVendor;
import com.ywt.message.bean.MessageVendor;
import com.ywt.message.component.vendor.BaseMessageVendorExecutor;
import com.ywt.message.component.vendor.MessageVendorBindingFactory;
import com.ywt.message.component.vendor.executor.CompanyWechatVendorExecutor;
import com.ywt.message.constant.MessageType;
import com.ywt.message.constant.MessageVendorType;
import com.ywt.message.service.MessageTemplateApiService;
import com.ywt.message.service.MessageTemplateVendorApiService;
import com.ywt.message.service.MessageVendorApiService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MessageSendApiServiceImpl implements MessageSendApiService {
    @Autowired
    private MessageVendorBindingFactory messageVendorBindingFactory;
    @Autowired
    private MessageVendorApiService messageVendorApiService;
    @Autowired
    private MessageTemplateApiService messageTemplateApiService;
    @Autowired
    private MessageTemplateVendorApiService messageTemplateVendorApiService;
    @Autowired
    private CompanyWechatVendorExecutor companyWechatVendorExecutor;

    @Override
    public void sendMessage(String templateKey, String hotelId, List<String> receiverList, Map<String, String> replacement) {
        log.info("开始处理发送消息任务, templateKey={}, receiver={}, replacement={}",
        		templateKey,
        		receiverList == null ? StringUtils.EMPTY : StringUtils.join(receiverList, ","),
                replacement == null ? StringUtils.EMPTY : CoreJsonUtils.objToString(replacement));

        // 添加时间戳
        if (replacement == null) {
            replacement = new HashMap<>();
        }

        // 读取模板对象
        MessageTemplate messageTemplate = messageTemplateApiService.getByTemplateKey(templateKey, hotelId);
        if (messageTemplate == null) {
            log.error("消息模板不存在, templateKey={}", templateKey);
            return;
        }

        List<MessageTemplateVendor> messageTemplateVendorList = messageTemplateVendorApiService.findByTemplateId(messageTemplate.getId());
        if (CollectionUtils.isEmpty(messageTemplateVendorList)) {
            log.error("消息模板没有配置对应的供应商, templateKey={}, templateId={}", templateKey, messageTemplate.getId());
            return;
        }

        String identifier = replacement.get("IDENTIFIER");

        for (MessageTemplateVendor messageTemplateVendor : messageTemplateVendorList) {
            if (messageTemplateVendor.getEnableStatus() != EnableDisableStatus.ENABLE) {
                // 未启用的关系
                continue;
            }
            if (StringUtils.isNotBlank(identifier)) {
                // 如果指明了要用指定标识符的 vendor , 进行判断, 不匹配则跳过
                if (!StringUtils.equals(identifier, messageTemplateVendor.getIdentifier())) {
                    continue;
                }
            }

            MessageVendor messageVendor = messageVendorApiService.getById(messageTemplateVendor.getVendorId());
            if (messageVendor == null) {
                log.error("消息模板配置对应的供应商不存在, templateKey={}, templateId={}, vendorId={}", templateKey, messageTemplate.getId(), messageTemplateVendor.getVendorId());
                continue;
            }
            if (messageVendor.getEnableStatus() != EnableDisableStatus.ENABLE) {
                log.warn("消息模板配置的供应商未启用, templateKey={}, templateId={}, vendorId={}", templateKey, messageTemplate.getId(), messageTemplateVendor.getVendorId());
                continue;
            }

            MessageVendorType messageVendorType = messageVendor.getVendorType();
            MessageType messageType = messageVendor.getMessageType();
            BaseMessageVendorExecutor executor = messageVendorBindingFactory.getExecutor(messageVendorType, messageType);
            if (executor == null) {
                log.error("消息供应商未找到对应的执行器, vendorType={}, messageType={}", messageVendorType, messageType);
                continue;
            }
            try {
                executor.send(messageTemplate, receiverList, replacement);
            } catch (Exception e) {
                log.error("当前消息供应商发送消息出错, vendorType={}, messageType={}", messageVendorType, messageType);
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 普通业务报警
     * @param title
     * @param detail
     */
	@Override
	public void sendBizAlarmMessage(String title, String detail, String projectId) {
		companyWechatVendorExecutor.sendBizMessage(title, detail, projectId);
	}
}
