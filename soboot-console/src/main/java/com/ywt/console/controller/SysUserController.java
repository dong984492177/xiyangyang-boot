package com.ywt.console.controller;

import com.ywt.common.response.DefaultResponseDataWrapper;
import com.ywt.console.models.UserDetailReqModel;
import com.ywt.console.models.UserRegisterReqModel;
import com.ywt.console.models.resmodel.systemresmodels.QueryLoginInfoResModel;
import com.ywt.console.service.SystemService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@RestController
@RequestMapping("/User")
@Slf4j
public class SysUserController {

    @Autowired
    private SystemService systemService;

    @PostMapping("/Register")
    @ApiOperation(value = "新增用户")
    public DefaultResponseDataWrapper<String> register(@RequestBody @ApiParam @Validated UserRegisterReqModel registerReqModel) {
        systemService.register(registerReqModel);
        return new DefaultResponseDataWrapper<>();
    }

    @PostMapping("/Exist")
    @ApiOperation(value = "判断用户是否注册过")
    public DefaultResponseDataWrapper<Boolean> exist(@RequestBody @ApiParam @Validated UserRegisterReqModel registerReqModel) {

        return DefaultResponseDataWrapper.success(systemService.exist(registerReqModel));
    }

    @PostMapping("/Get")
    @ApiOperation(value = "获取登录所需信息")
    public DefaultResponseDataWrapper<QueryLoginInfoResModel> getInfo(@RequestBody @ApiParam @Validated UserDetailReqModel detailReqModel) {
        DefaultResponseDataWrapper<QueryLoginInfoResModel> resmodel = new DefaultResponseDataWrapper<>();
        resmodel.setData(systemService.getInfo(detailReqModel));
        return resmodel;
    }
}
