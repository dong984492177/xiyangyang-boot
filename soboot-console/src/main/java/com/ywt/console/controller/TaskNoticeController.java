package com.ywt.console.controller;

import com.ywt.common.controller.BaseController;
import com.ywt.common.response.DefaultResponseDataWrapper;
import com.ywt.console.models.reqmodel.UpdateTaskCategoryReqModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @Author: zhangsan
 * @Description:  任务提醒
 * @Version: 1.0
 * @Create: 2022-05-03
 * @Coyright: 喜阳阳信息科技
 */
@RestController
@RequestMapping("/notice")
public class TaskNoticeController extends BaseController {

    /**
     * 保存
     * @param reqModel
     * @return
     */
    @PostMapping("/create")
    @ApiOperation(value = "新建任务提醒")
    public DefaultResponseDataWrapper<String> saveCategory(@RequestBody @ApiParam @Validated UpdateTaskCategoryReqModel reqModel) {


        return DefaultResponseDataWrapper.success("操作成功!");
    }

    /**
     * 获取我的提醒列表
     * @param reqModel
     * @return
     */
    @PostMapping("/myList")
    @ApiOperation(value = "我的提醒列表")
    public DefaultResponseDataWrapper<String> myList(@RequestBody @ApiParam @Validated UpdateTaskCategoryReqModel reqModel) {


        return DefaultResponseDataWrapper.success("操作成功!");
    }

    /**
     * 获取任务详情
     * @param reqModel
     * @return
     */
    @PostMapping("/detail")
    @ApiOperation(value = "查看任务详情")
    public DefaultResponseDataWrapper<String> detail(@RequestBody @ApiParam @Validated UpdateTaskCategoryReqModel reqModel) {


        return DefaultResponseDataWrapper.success("操作成功!");
    }

    /**
     * 获取任务反馈详情
     * @param reqModel
     * @return
     */
    @PostMapping("/feedbackDetail")
    @ApiOperation(value = "查看任务反馈详情")
    public DefaultResponseDataWrapper<String> feedbackDetail(@RequestBody @ApiParam @Validated UpdateTaskCategoryReqModel reqModel) {


        return DefaultResponseDataWrapper.success("操作成功!");
    }

    /**
     * 添加任务反馈
     * @param reqModel
     * @return
     */
    @PostMapping("/createFeedback")
    @ApiOperation(value = "添加任务反馈")
    public DefaultResponseDataWrapper<String> createFeedback(@RequestBody @ApiParam @Validated UpdateTaskCategoryReqModel reqModel) {


        return DefaultResponseDataWrapper.success("操作成功!");
    }

    /**
     * 添加任务问题
     * @param reqModel
     * @return
     */
    @PostMapping("/createProblem")
    @ApiOperation(value = "添加任务问题")
    public DefaultResponseDataWrapper<String> createProblem(@RequestBody @ApiParam @Validated UpdateTaskCategoryReqModel reqModel) {


        return DefaultResponseDataWrapper.success("操作成功!");
    }

    /**
     * 获取问题列表
     * @param reqModel
     * @return
     */
    @PostMapping("/getProblemList")
    @ApiOperation(value = "获取问题列表")
    public DefaultResponseDataWrapper<String> getProblemList(@RequestBody @ApiParam @Validated UpdateTaskCategoryReqModel reqModel) {


        return DefaultResponseDataWrapper.success("操作成功!");
    }

    /**
     * 审核反馈
     * @param reqModel
     * @return
     */
    @PostMapping("/checkFeedback")
    @ApiOperation(value = "审核反馈")
    public DefaultResponseDataWrapper<String> checkFeedback(@RequestBody @ApiParam @Validated UpdateTaskCategoryReqModel reqModel) {


        return DefaultResponseDataWrapper.success("操作成功!");
    }
}
