package com.ywt.console.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ywt.common.base.util.BeanMapping;
import com.ywt.common.base.util.StringUtils;
import com.ywt.console.entity.activiti.ActDeployment;
import com.ywt.console.mapper.ActDeploymentMapper;
import com.ywt.console.models.activiti.ActivitiHighLineResModel;
import com.ywt.console.models.activiti.ActivitiListReqModel;
import com.ywt.console.models.activiti.ActivitiListResModel;
import com.ywt.console.models.resmodel.ActDefinitionResModel;
import com.ywt.console.service.IActDefinitionService;
import com.ywt.console.utils.Util;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2022-05-03
 */
@Component
public class ActivitiBizService {

    private Logger log = LoggerFactory.getLogger(ActivitiBizService.class);

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ActDeploymentMapper actDeploymentMapper;

    @Autowired
    private IActDefinitionService actDefinitionService;

    @Autowired
    private RuntimeService runtimeService;

    /**
     * 流程定义的激活或者挂起
     * @param definitionId
     * @param state
     */
    public void suspendOrActiveApply(String definitionId,Integer state){
        if(1==state){
            repositoryService.suspendProcessDefinitionById(definitionId);
            return;
        }
        repositoryService.activateProcessDefinitionById(definitionId);
    }

    /**
     * 获取流程定义XML
     * @param deploymentId
     * @param resourceName
     */
    public void getProcessDefineXML(String deploymentId,String resourceName){
        try (InputStream inputStream = repositoryService.getResourceAsStream(deploymentId, resourceName)){
            int count = inputStream.available();
            byte[] bytes = new byte[count];
            HttpServletResponse response = Util.getResponse();
            response.setContentType("text/xml");
            OutputStream outputStream = response.getOutputStream();
            while (inputStream.read(bytes) != -1) {
                outputStream.write(bytes);
            }
        }catch (IOException e){
            log.info(e.getMessage());
        }
    }

    public Boolean delDefinition(List<String> idList){
        try {

            //actDefinitionService.delByBatchId(idList);
             //@TODO 目前先循环删除  id应为部署id
            idList.forEach(v->{
                repositoryService.deleteDeployment(v,false);
            });
        }catch(Exception e){
            log.info(e.getMessage());
            return false;
        }
        return true;
    }

    public IPage<ActivitiListResModel> processList(ActivitiListReqModel reqModel){
        IPage<ActivitiListResModel>  iPage = new Page<>() ;
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionId().orderByProcessDefinitionVersion().desc();
        long count = processDefinitionQuery.count();
        if(count == 0){
            return iPage;
        }
        if (StringUtils.isNotBlank(reqModel.getName())) {
            processDefinitionQuery.processDefinitionNameLike("%" + reqModel.getName() + "%");
        }
        if (StringUtils.isNotBlank(reqModel.getKey())) {
            processDefinitionQuery.processDefinitionKeyLike("%" + reqModel.getKey() + "%");
        }
        List<ProcessDefinition> processDefinitions = processDefinitionQuery.listPage((reqModel.getPageNo() - 1) * reqModel.getPageSize(), reqModel.getPageSize());
        List<ActivitiListResModel> resultList = new ArrayList<>();
        List<ActDeployment> actDeploymentList = actDeploymentMapper.selectByIds(processDefinitions.stream().map(ProcessDefinition::getDeploymentId).collect(Collectors.toList()));
        Map<String,List<ActDeployment>> maps = actDeploymentList.stream().collect(Collectors.groupingBy(ActDeployment::getId));

        processDefinitions.forEach(v->{
            String deploymentId = v.getDeploymentId();
            ActivitiListResModel resModel = ActivitiListResModel.builder()
                    .deploymentId(deploymentId)
                    .id(v.getId())
                    .key(v.getKey())
                    .name(v.getName())
                    .resourceName(v.getResourceName())
                    .deploymentTime(maps.get(deploymentId).get(0).getDeployTime())
                    .version(v.getVersion())
                    .suspendState(v.isSuspended()==true?1:0)
                    .build();
            resultList.add(resModel);
        });
        iPage.setRecords(resultList);
        iPage.setTotal(count);
        return iPage;
    }

    public ActivitiHighLineResModel getHighLine(String instanceId) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(instanceId).singleResult();
        //获取bpmnModel对象
        BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
        //因为我们这里只定义了一个Process 所以获取集合中的第一个即可
        Process process = bpmnModel.getProcesses().get(0);
        //获取所有的FlowElement信息
        Collection<FlowElement> flowElements = process.getFlowElements();

        Map<String, String> map = new HashMap<>();
        for (FlowElement flowElement : flowElements) {
            //判断是否是连线
            if (flowElement instanceof SequenceFlow) {
                SequenceFlow sequenceFlow = (SequenceFlow) flowElement;
                String ref = sequenceFlow.getSourceRef();
                String targetRef = sequenceFlow.getTargetRef();
                map.put(ref + targetRef, sequenceFlow.getId());
            }
        }

        //获取流程实例 历史节点(全部)
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(instanceId)
                .list();
        //各个历史节点   两两组合 key
        Set<String> keyList = new HashSet<>();
        for (HistoricActivityInstance i : list) {
            for (HistoricActivityInstance j : list) {
                if (i != j) {
                    keyList.add(i.getActivityId() + j.getActivityId());
                }
            }
        }
        //高亮连线ID
        Set<String> highLine = new HashSet<>();
        keyList.forEach(s -> highLine.add(map.get(s)));


        //获取流程实例 历史节点（已完成）
        List<HistoricActivityInstance> listFinished = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(instanceId)
                .finished()
                .list();
        //高亮节点ID
        Set<String> highPoint = new HashSet<>();
        listFinished.forEach(s -> highPoint.add(s.getActivityId()));

        //获取流程实例 历史节点（待办节点）
        List<HistoricActivityInstance> listUnFinished = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(instanceId)
                .unfinished()
                .list();

        //需要移除的高亮连线
        Set<String> set = new HashSet<>();
        //待办高亮节点
        Set<String> waitingToDo = new HashSet<>();
        listUnFinished.forEach(s -> {
            waitingToDo.add(s.getActivityId());

            for (FlowElement flowElement : flowElements) {
                //判断是否是 用户节点
                if (flowElement instanceof UserTask) {
                    UserTask userTask = (UserTask) flowElement;

                    if (userTask.getId().equals(s.getActivityId())) {
                        List<SequenceFlow> outgoingFlows = userTask.getOutgoingFlows();
                        //因为 高亮连线查询的是所有节点  两两组合 把待办 之后  往外发出的连线 也包含进去了  所以要把高亮待办节点 之后 即出的连线去掉
                        if (outgoingFlows != null && outgoingFlows.size() > 0) {
                            outgoingFlows.forEach(a -> {
                                if (a.getSourceRef().equals(s.getActivityId())) {
                                    set.add(a.getId());
                                }
                            });
                        }
                    }
                }
            }
        });

        highLine.removeAll(set);
        Set<String> iDo = new HashSet<>(); //存放 高亮 我的办理节点
        //当前用户已完成的任务
        List<HistoricTaskInstance> taskInstanceList = historyService.createHistoricTaskInstanceQuery()
//                    .taskAssignee(SecurityUtils.getUsername())
                .finished()
                .processInstanceId(instanceId).list();

        taskInstanceList.forEach(a -> iDo.add(a.getTaskDefinitionKey()));

        ActivitiHighLineResModel resModel = ActivitiHighLineResModel.builder()
                .highLine(highLine)
                .highPoint(highPoint)
                .waitingToDo(waitingToDo)
                .iDo(iDo)
                .build();

        return resModel;
    }

    /**
     * 获取流程图
     * @param instanceId
     * @return
     */
    public ActDefinitionResModel getDefinitionByInstanceId(String instanceId){

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
        String deploymentId = processInstance.getDeploymentId();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
        return BeanMapping.map(processDefinition,ActDefinitionResModel.class);
    }
}
