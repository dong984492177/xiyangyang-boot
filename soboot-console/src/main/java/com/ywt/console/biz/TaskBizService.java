package com.ywt.console.biz;

import com.ywt.common.base.util.BeanMapping;
import com.ywt.console.entity.UserTask;
import com.ywt.console.models.reqmodel.UpdateUserTaskReqModel;
import com.ywt.console.service.ISysUserService;
import com.ywt.console.service.IUserTaskService;
import com.ywt.console.utils.Util;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.model.payloads.StartProcessPayload;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class TaskBizService {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private IUserTaskService userTaskService;

    /**
     * 绑定任务
     * @param userTaskReqModel
     */
    public void bindTask(UpdateUserTaskReqModel userTaskReqModel){

        Integer taskId = userTaskReqModel.getId();
        String userName = Util.getUserToken().getUserName();
        UserTask userTask = BeanMapping.map(userTaskReqModel,UserTask.class);
        userTaskService.saveTask(userTask);
        if(ObjectUtils.isEmpty(taskId)){
            StartProcessPayload startProcessPayload = ProcessPayloadBuilder.start()
                    .withProcessDefinitionKey("leave")
                    .withName(userName.concat("的申请"))
                    .withBusinessKey(userTask.getId().toString())
                    .withVariable("deptLeader","admin")
                    .build();
            ProcessInstance processInstance = processRuntime.start(startProcessPayload);
            userTask.setInstanceId(processInstance.getId());
            userTaskService.updateById(userTask);
        }
    }
}
