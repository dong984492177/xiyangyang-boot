package com.ywt.message.component.vendor.executor;

import com.ywt.message.api.exception.MessageSendException;
import com.ywt.message.bean.MessageTemplate;
import com.ywt.message.component.vendor.AbstractBaseMessageVendorExecutor;
import com.ywt.message.util.XingeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author: huangchaoyang
 * @Description: 信鸽发送消息
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Component
@Slf4j
public class TclPushMessageVendorExecutor extends AbstractBaseMessageVendorExecutor {

    @Value("${tencent.xinge.tclappid:1500012545}")
    private String appid;
    @Value("${tencent.xinge.tclsecretkey:7e6ad8a4256739c6be4c845eddd2512b}")
    private String secretkey;

    @Override
    public void send(MessageTemplate messageTemplate, List<String> receiverList, Map<String, String> replacement) throws MessageSendException {
        try {
            String content = this.replaceTemplateContent(messageTemplate.getContent(), replacement);
            for(String receiver: receiverList) {
                if("text".equals(messageTemplate.getType())) {
                    XingeUtil.sendTextMessage(appid, secretkey, "", content, receiver);
                    log.info("发送信鸽消息成功, receiver={}, content={}", receiver, content);
                }
            }
        } catch (Exception e) {
            throw new MessageSendException("发送信鸽消息出错", e);
        }
    }
}
