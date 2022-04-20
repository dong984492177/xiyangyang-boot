package com.ywt.message.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.ywt.message.bean.MessageTemplate;
import com.ywt.message.service.MessageTemplateApiService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageTemplateApiServiceImpl implements MessageTemplateApiService {
    private final static Map<String, MessageTemplate> templateHashMap = new HashMap<>();

    static {
        MessageTemplate template = new MessageTemplate();
        template.setId(1);
        template.setName("xxx");
        template.setTemplateKey("checkInNotify");
        template.setTitle("xxx通知");
        template.setContent("【xxx】您好{#user#}！欢迎");
        templateHashMap.put(template.getTemplateKey(), template);

        MessageTemplate template2 = new MessageTemplate();
        template2.setId(2);
        template2.setName("xxx验证码");
        template2.setTemplateKey("VerifyCode");
        template2.setTitle("xxx验证码");
        template2.setContent("【xxx】验证码：{#verifyCode#}， 此为xxx基本信息的验证码，请勿泄露，感谢您的支持");
        templateHashMap.put(template2.getTemplateKey(), template2);
    }


    @Override
    public MessageTemplate getByTemplateKey(String templateKey, String hotelId) {
    	log.info("读取模板 hotelId={}, templateKey={}", hotelId, templateKey);
    	//TODO 在数据库里维护模板，从数据库里读取
        return templateHashMap.get(templateKey);
    }
}
