package com.ywt.console.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ywt.common.response.DefaultResponseDataWrapper;
import com.ywt.console.models.reqmodel.QueryApiLogReqModel;
import com.ywt.console.models.reqmodel.QueryLoginLogReqModel;
import com.ywt.console.service.IApiLogService;
import com.ywt.console.service.ILoginLogService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: huangchaoyang
 * @Description: 日志管理
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@RestController
@RequestMapping("/system/log")
@Slf4j
public class LogController {


    @Autowired
    private IApiLogService apiLogService;

    @Autowired
    private ILoginLogService loginLogService;

    /**
     * description: 系统日志列表
     * author: hcy
     * param: [model]
     * return: com.xiot.common.response.DefaultResponseDataWrapper
     **/
    @PostMapping("/queryApiLog")
    public DefaultResponseDataWrapper queryApiLog(QueryApiLogReqModel model){
        return apiLogService.queryApiLog(model);
    }

    /**
     * description: 系统日志列表
     * author: hcy
     * param: [model]
     * return: com.xiot.common.response.DefaultResponseDataWrapper
     **/
    @PostMapping("/queryLoginLog")
    public DefaultResponseDataWrapper queryLoginLog(QueryLoginLogReqModel model){

        return loginLogService.queryLoginLog(model);
    }
}
