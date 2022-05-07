package com.ywt.console.biz;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ywt.common.base.util.BeanMapping;
import com.ywt.common.base.util.CoreDateUtils;
import com.ywt.console.entity.UserTask;
import com.ywt.console.entity.activiti.ActWorkflowFormData;
import com.ywt.console.entity.activiti.TaskCheckDetail;
import com.ywt.console.models.QueryModel;
import com.ywt.console.models.UserPhoneToken;
import com.ywt.console.models.activiti.HistoryTaskResModel;
import com.ywt.console.models.activiti.TaskFormDataResModel;
import com.ywt.console.models.reqmodel.UpdateUserTaskReqModel;
import com.ywt.console.models.reqmodel.activiti.ActFormDataReqModel;
import com.ywt.console.models.resmodel.ActTaskResModel;
import com.ywt.console.service.IActFlowFormDataService;
import com.ywt.console.service.ISysUserService;
import com.ywt.console.service.ITaskCheckDetailService;
import com.ywt.console.service.IUserTaskService;
import com.ywt.console.utils.Util;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.model.payloads.StartProcessPayload;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.bpmn.model.FormProperty;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2022-05-03
 */
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

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private IActFlowFormDataService actFlowFormDataService;

    @Autowired
    private ITaskCheckDetailService taskCheckDetailService;

    @Autowired
    private MQBizService mqBizService;

    private static final String label = "radio";

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
                    .withVariable("deptLeader","1")
                    .build();
            ProcessInstance processInstance = processRuntime.start(startProcessPayload);
            userTask.setInstanceId(processInstance.getId());
            userTaskService.updateById(userTask);
        }
    }

    public IPage<ActTaskResModel>  getTaskWaitedList(QueryModel queryModel){

        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        IPage<ActTaskResModel> iPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>() ;
        Pageable pageable = Pageable.of((queryModel.getPageNo() - 1) * queryModel.getPageSize(), queryModel.getPageSize());
        Page<Task> pageTasks = taskRuntime.tasks(pageable);
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

    /**
     * 任务审核
     * @param taskId
     * @param reqModelList
     * @return
     */
    public Boolean applySave(String taskId,List<ActFormDataReqModel> reqModelList){

        Task task = taskRuntime.task(taskId);
        org.activiti.engine.runtime.ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();

        Boolean checkSuccess = false;
        Boolean hasVariables = false;
        HashMap<String, Object> variablesMap = new HashMap<String, Object>(10);
        //前端传来的字符串，拆分成每个控件
        List<ActWorkflowFormData> formDataList = new ArrayList<>();
        for (ActFormDataReqModel model : reqModelList) {
            String controlValue = model.getControlValue();
            ActWorkflowFormData actWorkflowFormData = ActWorkflowFormData.builder()
                    .businessKey(processInstance.getBusinessKey())
                    .formKey(task.getFormKey())
                    .controlId(model.getControlId())
                    .controlName(model.getControlLable())
                    .taskNodeName(task.getName())
                    .build();
            if(label.equals(model.getControlType())){
                Integer i = Integer.parseInt(controlValue);
                if(0==i){
                    checkSuccess = true;
                }
                actWorkflowFormData.setControlValue(model.getControlDefault().split("--__--")[i]);
            }else{
                actWorkflowFormData.setControlValue(controlValue);
            }

            formDataList.add(actWorkflowFormData);
            //构建参数集合
            if(!"f".equals(model.getControlIsParam())) {
                variablesMap.put(model.getControlId(), controlValue);
                hasVariables = true;
            }
        }
        if (task.getAssignee() == null) {
            taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
        }
        if (hasVariables) {
            //带参数完成任务
            taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(taskId)
                    .withVariables(variablesMap)
                    .build());
        } else {
            taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(taskId)
                    .build());
        }

        //保存
        boolean result = actFlowFormDataService.saveBatch(formDataList);
        //添加任务日志记录
        UserPhoneToken userPhoneToken = Util.getUserToken();
        TaskCheckDetail taskCheckDetail = TaskCheckDetail.builder()
                .checkTime(new Date())
                .checkUserId(userPhoneToken.getUserId())
                .taskId(taskId)
                .status(checkSuccess?"1":"0")
                .checkUserName(userPhoneToken.getUserName())
                .build();
        mqBizService.writeTaskDetailLog(JSON.toJSONString(taskCheckDetail));

        return result;
    }

    /**
     * 获取审核详情
     * @param taskId
     * @return
     */
    public List<String> applyDetail(String taskId){

        Task task = taskRuntime.task(taskId);
        org.activiti.bpmn.model.UserTask userTask = (org.activiti.bpmn.model.UserTask) repositoryService.getBpmnModel(task.getProcessDefinitionId())
                .getFlowElement(task.getFormKey());

        if (userTask == null) {
            return null;
        }

        List<FormProperty> formProperties = userTask.getFormProperties();
        List<String> resultList = formProperties.stream().map(fp -> fp.getId()).collect(Collectors.toList());
        return resultList;
    }

    /**
     * 获取formData详情
     * @param businessKey
     * @return
     */
    public List<HistoryTaskResModel> listByBusinessKey(String businessKey){
        List<HistoryTaskResModel> resultList = new ArrayList<>();
        List<ActWorkflowFormData> actWorkflowFormData = actFlowFormDataService.queryByBusinessKey(businessKey);
        Map<String, List<ActWorkflowFormData>> collect = actWorkflowFormData.stream().collect(Collectors.groupingBy(ActWorkflowFormData::getTaskNodeName));
        collect.entrySet().forEach(
                entry -> {
                    ActWorkflowFormData formData = entry.getValue().get(0);
                    List<TaskFormDataResModel> formDataResModelList = entry.getValue().stream()
                            .map(p->TaskFormDataResModel.builder().title(p.getControlName()).value(p.getControlValue()).build()).collect(Collectors.toList());
                    HistoryTaskResModel historyTaskResModel = HistoryTaskResModel.builder()
                            .taskNodeName(formData.getTaskNodeName())
                            .createName(formData.getCreateName())
                            .createdDate(CoreDateUtils.formatDateTime(formData.getCreateTime()))
                            .formDataResModelList(formDataResModelList)
                            .build();
                    resultList.add(historyTaskResModel);
                }
        );

        //降序
        List<HistoryTaskResModel> results = resultList.stream().sorted((left, right) -> left.getCreatedDate().compareTo(right.getCreatedDate())).collect(Collectors.toList());
        return results;
    }
}
