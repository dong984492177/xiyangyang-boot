package com.ywt.message.component.vendor.executor;

import java.util.List;
import java.util.Map;

import com.ywt.message.bean.MessageTemplate;
import com.ywt.message.util.XingeUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ywt.message.api.exception.MessageSendException;
import com.ywt.message.component.vendor.AbstractBaseMessageVendorExecutor;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: huangchaoyang
 * @Description: 信鸽推送执行器
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Component
@Slf4j
public class XgPushMessageVendorExecutor extends AbstractBaseMessageVendorExecutor {

    @Value("${tencent.xinge.appid}")
    private String appid;
    @Value("${tencent.xinge.secretkey}")
    private String secretkey;

    @Override
    public void send(MessageTemplate messageTemplate, List<String> receiverList, Map<String, String> replacement) throws MessageSendException {
        try {
            String content = this.replaceTemplateContent(messageTemplate.getContent(), replacement);
            for(String receiver: receiverList) {
            	if("text".equals(messageTemplate.getType())) {
            		XingeUtil.sendTextMessage(appid, secretkey, "", content, receiver);
            		log.info("发送信鸽消息成功, receiver={}, content={}", receiver, content);
            	}else if("image".equals(messageTemplate.getType())) {
            		XingeUtil.sendMediaMessage(appid, secretkey, content, receiver);
            		log.info("发送信鸽图片成功, receiver={}, content={}", receiver, content);
            	}else if("audio".equals(messageTemplate.getType())) {
            		XingeUtil.sendMediaMessage(appid, secretkey, content, receiver);
            		log.info("发送信鸽音频成功, receiver={}, content={}", receiver, content);
            	}else {
            		log.error("推送信鸽，必须设置模板type，值为 text/image/audio");
            	}
            }
        } catch (Exception e) {
            throw new MessageSendException("发送信鸽消息出错", e);
        }
    }

}
