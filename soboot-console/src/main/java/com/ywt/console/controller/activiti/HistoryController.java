package com.ywt.console.controller.activiti;

import com.ywt.common.controller.BaseController;
import com.ywt.common.response.DefaultResponseDataWrapper;
import com.ywt.console.biz.TaskBizService;
import com.ywt.console.models.activiti.HistoryTaskResModel;
import com.ywt.console.service.ITaskCategoryService;
import com.ywt.console.service.IUserTaskService;
import io.swagger.annotations.ApiOperation;
import org.activiti.api.task.runtime.TaskRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: zhangsan
 * @Description:
 * @Version: 1.0
 * @Create: 2022-05-03
 * @Coyright: 喜阳阳信息科技
 */
@RestController
@RequestMapping("/history")
public class HistoryController extends BaseController {

    @Autowired
    private ITaskCategoryService taskCategoryService;

    @Autowired
    private IUserTaskService userTaskService;

    @Autowired
    private TaskBizService taskBizService;

    @Autowired
    private TaskRuntime taskRuntime;

    @GetMapping("/task/{instanceId}")
    @ApiOperation(value = "申请详情")
    public DefaultResponseDataWrapper<List<HistoryTaskResModel>> historyTask(@PathVariable(value = "instanceId",required = true) String instanceId){

        List<HistoryTaskResModel> resultList = taskBizService.listByBusinessKey(instanceId);
        return DefaultResponseDataWrapper.success(resultList);
    }
}
