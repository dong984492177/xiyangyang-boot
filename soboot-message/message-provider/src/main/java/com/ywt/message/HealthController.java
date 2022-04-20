package com.ywt.message;

import com.ywt.common.controller.BaseRestController;
import com.ywt.common.response.DefaultResponseDataWrapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: huangchaoyang
 * @Description: 健康检查
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@RestController
public class HealthController extends BaseRestController {

    @GetMapping("/health")
    public DefaultResponseDataWrapper<String> getHealth() {
        return DefaultResponseDataWrapper.success();
    }
}

