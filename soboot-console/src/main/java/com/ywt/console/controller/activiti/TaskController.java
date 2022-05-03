package com.ywt.console.controller.activiti;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ywt.common.bean.PageWrapper;
import com.ywt.common.controller.BaseController;
import com.ywt.common.response.DefaultResponseDataWrapper;
import com.ywt.console.entity.TaskCategory;
import com.ywt.console.models.DeleteModel;
import com.ywt.console.models.reqmodel.TaskCategoryReqModel;
import com.ywt.console.models.reqmodel.UpdateTaskCategoryReqModel;
import com.ywt.console.models.resmodel.TaskCategoryResModel;
import com.ywt.console.service.ITaskCategoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/task")
public class TaskController extends BaseController {

    @Autowired
    private ITaskCategoryService taskCategoryService;

    /**
     * 获取流程定义XML
     * @param deploymentId
     * @param resourceName
     */
    @PostMapping("/list")
    public void list(){

    }

    /**
     * 获取分类列表分页
     * @param reqModel
     * @return
     */
    @PostMapping("/categoryList")
    @ApiOperation(value = "获取任务分类列表分页")
    public DefaultResponseDataWrapper<List<TaskCategoryResModel>> categoryList(@RequestBody @ApiParam @Validated TaskCategoryReqModel reqModel){
        DefaultResponseDataWrapper<List<TaskCategoryResModel>> responseModel = new DefaultResponseDataWrapper<>();
        PageWrapper pageModel = new PageWrapper(reqModel.getPageNo(), reqModel.getPageSize());
        IPage<TaskCategoryResModel> iPage =  taskCategoryService.queryList(reqModel);
        if(ObjectUtils.isEmpty(iPage)){
            return responseModel;
        }
        return parsePageModel(pageModel, iPage.getTotal(), iPage.getRecords(), responseModel);
    }

    /**
     * 保存
     * @param reqModel
     * @return
     */
    @PostMapping("/categorySave")
    @ApiOperation(value = "更新任务分类")
    public DefaultResponseDataWrapper<TaskCategoryResModel> saveCategory(@RequestBody @ApiParam @Validated UpdateTaskCategoryReqModel reqModel) {

        DefaultResponseDataWrapper<TaskCategoryResModel> responseModel = new DefaultResponseDataWrapper<>();
        responseModel.setData(taskCategoryService.saveCategory(reqModel));
        return responseModel;
    }

    @PostMapping("/categoryDel")
    @ApiOperation(value = "删除任务分类")
    /*@PreAuthorize("hasAuthority('category:del')")*/
    public DefaultResponseDataWrapper<String> deleteCategory(@RequestBody @ApiParam @Validated DeleteModel deleteModel) {
        taskCategoryService.delCategory(deleteModel);
        return DefaultResponseDataWrapper.success();
    }
}
