package com.ywt.console.controller.activiti;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ywt.common.bean.PageWrapper;
import com.ywt.common.controller.BaseController;
import com.ywt.common.response.DefaultResponseDataWrapper;
import com.ywt.console.biz.ActivitiBizService;
import com.ywt.console.models.activiti.ActivitiListReqModel;
import com.ywt.console.models.activiti.ActivitiListResModel;
import com.ywt.console.models.reqmodel.QueryProductReqModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.ywt.console.utils.Util.parsePageModel;

/**
 * @Author: zhangsan
 * @Description:
 * @Version: 1.0
 * @Create: 2022-05-03
 * @Coyright: 喜阳阳信息科技
 */
@RestController
@RequestMapping("/activiti")
public class ActivitiController extends BaseController {


    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ActivitiBizService activitiBizService;

    /**
     * 导入流程
     * @param file
     * @return
     */
    @GetMapping("/importProcess")
    @ApiOperation(value = "导入流程文件")
    public DefaultResponseDataWrapper importProcess(@RequestParam("file") MultipartFile file){

        String fileName = file.getOriginalFilename();
        try {
            Deployment deployment = repositoryService.createDeployment()
                    .name(fileName)
                    .addInputStream(fileName, file.getInputStream())
                    .deploy();
            log.info("部署ID：{}" ,deployment.getId());
            log.info("部署Name：{}" ,deployment.getName());
            log.info("部署时间：{}" ,deployment.getDeploymentTime());
        }catch (IOException e){
            log.info(e.getMessage());
        }
        return DefaultResponseDataWrapper.success();
    }

    /**
     * 获取流程定义XML
     * @param deploymentId
     * @param resourceName
     */
    @GetMapping("/getProcessDefineXML")
    @ApiOperation(value = "获取流程定义XML")
    public void processDefineXML(@RequestParam("deploymentId") String deploymentId,@RequestParam("resourceName") String resourceName){
        activitiBizService.getProcessDefineXML(deploymentId,resourceName);
    }

    /**
     * 删除流程定义
     * @param deploymentId
     * @return
     */
    @DeleteMapping("/delDefinition")
    @ApiOperation(value = "删除流程定义")
    public DefaultResponseDataWrapper delDefinition(@PathVariable("deploymentId") String deploymentId){
        if(activitiBizService.delDefinition(deploymentId)){
            return DefaultResponseDataWrapper.success();
        }
        return DefaultResponseDataWrapper.fail(-1,"删除流程定义失败");
    }

    /**
     * 流程定义的激活或者挂起
     * @param definitionId
     * @param state
     * @return
     */
    @GetMapping("/updateStatus")
    @ApiOperation(value = "流程定义的激活或者挂起")
    public DefaultResponseDataWrapper startOrStop(@RequestParam("definitionId") String definitionId,@RequestParam("state") Integer state){
        activitiBizService.suspendOrActiveApply(definitionId,state);
        return DefaultResponseDataWrapper.success();
    }

    /**
     * 获取流程定义列表
     * @param reqModel
     * @return
     */
    @PostMapping("/list")
    @ApiOperation(value = "获取流程定义列表分页")
    public DefaultResponseDataWrapper<List<ActivitiListResModel>> processList(@RequestBody @ApiParam @Validated ActivitiListReqModel reqModel){
        DefaultResponseDataWrapper<List<ActivitiListResModel>> responseModel = new DefaultResponseDataWrapper<>();
        PageWrapper pageModel = new PageWrapper(reqModel.getPageNo(), reqModel.getPageSize());
        IPage<ActivitiListResModel> iPage =  activitiBizService.processList(reqModel);
        if(ObjectUtils.isEmpty(iPage)){
            return responseModel;
        }
        return parsePageModel(pageModel, iPage.getTotal(), iPage.getRecords(), responseModel);
    }
}
