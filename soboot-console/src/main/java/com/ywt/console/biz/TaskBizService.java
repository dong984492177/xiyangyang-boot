package com.ywt.console.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ywt.common.base.util.BeanMapping;
import com.ywt.console.entity.UserTask;
import com.ywt.console.models.QueryModel;
import com.ywt.console.models.reqmodel.UpdateUserTaskReqModel;
import com.ywt.console.models.resmodel.ActTaskResModel;
import com.ywt.console.service.ISysUserService;
import com.ywt.console.service.IUserTaskService;
import com.ywt.console.utils.Util;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.model.payloads.StartProcessPayload;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TaskBizService {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private IUserTaskService userTaskService;

    @Autowired
    private TaskRuntime taskRuntime;

    @Autowired
    private RuntimeService runtimeService;

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

    public IPage<ActTaskResModel>  getTaskWaitedList(QueryModel queryModel){

        IPage<ActTaskResModel> iPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>() ;
        Page<Task> pageTasks = taskRuntime.tasks(Pageable.of((queryModel.getPageNo() - 1) * queryModel.getPageSize(), queryModel.getPageSize()));
        List<Task> tasks = pageTasks.getContent();
        int totalItems = pageTasks.getTotalItems();
        iPage.setTotal(totalItems);
        if (totalItems != 0) {
            Set<String> processInstanceIdIds = tasks.parallelStream().map(t -> t.getProcessInstanceId()).collect(Collectors.toSet());
            List<org.activiti.engine.runtime.ProcessInstance> processInstanceList = runtimeService.createProcessInstanceQuery().processInstanceIds(processInstanceIdIds).list();
            List<ActTaskResModel> actTaskList = tasks.stream()
                    .map(t -> new ActTaskResModel(t, processInstanceList.parallelStream().filter(pi -> t.getProcessInstanceId().equals(pi.getId())).findAny().get()))
                    .collect(Collectors.toList());
            iPage.setRecords(actTaskList);

        }
        return iPage;
    }
}
