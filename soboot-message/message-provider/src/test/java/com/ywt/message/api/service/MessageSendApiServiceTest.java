package com.ywt.message.api.service;

import org.apache.dubbo.config.annotation.Reference;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageSendApiServiceTest {
    @Reference
    private MessageSendApiService messageSendApiService;

    @Test
    public void testSend() {
        Map<String, String> replacementMap = new HashMap<>();
        replacementMap.put("hotelName", "苏州云顶酒店");
        replacementMap.put("roomNo", "202");
        replacementMap.put("controlUrl", "http://www.baidu.com");

        messageSendApiService.sendMessage("checkInNotify", "0", Lists.newArrayList("15851426564"), replacementMap);
    }

    @Test
    public void testSendControlCode() {
        Map<String, String> replacementMap = new HashMap<>();
        replacementMap.put("hotelName", "苏州云顶酒店");
        replacementMap.put("roomNo", "202");
        replacementMap.put("verifyCode", "252655");

        messageSendApiService.sendMessage("hotelControlCode", "0", Lists.newArrayList("13521119652"), replacementMap);
    }
}
