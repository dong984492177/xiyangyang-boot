package com.ywt.console.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ywt.common.base.util.BeanMapping;
import com.ywt.common.base.util.BeanUtils;
import com.ywt.common.base.util.StringUtils;
import com.ywt.common.response.DefaultResponseDataWrapper;
import com.ywt.console.models.activiti.ActivitiListReqModel;
import com.ywt.console.models.activiti.ActivitiListResModel;
import com.ywt.console.models.resmodel.QueryProductResModel;
import com.ywt.console.utils.Util;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: zhangsan
 * @Description:
 * @Version: 1.0
 * @Create: 2022-05-03
 * @Coyright: 喜阳阳信息科技
 */
@Component
public class ActivitiBizService {

    private Logger log = LoggerFactory.getLogger(ActivitiBizService.class);

    @Autowired
    private RepositoryService repositoryService;

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

    public Boolean delDefinition(String deploymentId){
        try {
            repositoryService.deleteDeployment(deploymentId, false);
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
        List<ActivitiListResModel> resultList = BeanMapping.mapList(processDefinitions,ActivitiListResModel.class);
        iPage.setRecords(resultList);
        iPage.setTotal(count);
        return iPage;
    }
}
