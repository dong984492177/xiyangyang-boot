package com.ywt.console.controller.activiti;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ywt.common.base.util.BeanMapping;
import com.ywt.common.bean.PageWrapper;
import com.ywt.common.controller.BaseController;
import com.ywt.common.response.DefaultResponseDataWrapper;
import com.ywt.console.biz.TaskBizService;
import com.ywt.console.entity.UserTask;
import com.ywt.console.models.DeleteModel;
import com.ywt.console.models.reqmodel.TaskCategoryReqModel;
import com.ywt.console.models.reqmodel.UpdateTaskCategoryReqModel;
import com.ywt.console.models.reqmodel.UpdateUserTaskReqModel;
import com.ywt.console.models.reqmodel.UserTaskReqModel;
import com.ywt.console.models.resmodel.TaskCategoryResModel;
import com.ywt.console.models.resmodel.UserTaskResModel;
import com.ywt.console.service.ITaskCategoryService;
import com.ywt.console.service.IUserTaskService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private IUserTaskService userTaskService;

    @Autowired
    private TaskBizService taskBizService;

    /**
     * 获取流程定义XML
     */
    @PostMapping("/list")
    public void list(){

    }

    /**
     * 获取分类列表分页
     * @param reqModel
     * @return
     */
    @PostMapping("/categoryListWithPage")
    @ApiOperation(value = "获取任务分类列表分页")
    public DefaultResponseDataWrapper<List<TaskCategoryResModel>> categoryListWithPage(@RequestBody @ApiParam @Validated TaskCategoryReqModel reqModel){

        DefaultResponseDataWrapper<List<TaskCategoryResModel>> responseModel = new DefaultResponseDataWrapper<>();
        PageWrapper pageModel = new PageWrapper(reqModel.getPageNo(), reqModel.getPageSize());
        IPage<TaskCategoryResModel> iPage = taskCategoryService.queryListWithPage(reqModel);
        if(ObjectUtils.isEmpty(iPage)){
            return responseModel;
        }
        return parsePageModel(pageModel, iPage.getTotal(), iPage.getRecords(), responseModel);
    }

    /**
     * 获取分类列表
     * @param id
     * @param parentId
     * @return
     */
    @GetMapping("/categoryList")
    @ApiOperation(value = "获取任务分类列表")
    public DefaultResponseDataWrapper<List<TaskCategoryResModel>> categoryList( @ApiParam(value="分类父id",required = false) @RequestParam(value = "parentId",required = false) Integer parentId,
                                                                                @ApiParam(value="分类id",required = false) @RequestParam(value = "id",required = false) Integer id                                            ){

        List<TaskCategoryResModel> resultList = taskCategoryService.queryList(parentId,id);
        return DefaultResponseDataWrapper.success(resultList);
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

    /**
     * 获取任务列表分页
     * @param reqModel
     * @return
     */
    @PostMapping("/taskList")
    @ApiOperation(value = "获取任务列表分页")
    public DefaultResponseDataWrapper<List<UserTaskResModel>> categoryList(@RequestBody @ApiParam @Validated UserTaskReqModel reqModel){

        DefaultResponseDataWrapper<List<UserTaskResModel>> responseModel = new DefaultResponseDataWrapper<>();
        PageWrapper pageModel = new PageWrapper(reqModel.getPageNo(), reqModel.getPageSize());
        IPage<UserTaskResModel> iPage =  userTaskService.queryList(reqModel);
        if(ObjectUtils.isEmpty(iPage)){
            return responseModel;
        }
        return parsePageModel(pageModel, iPage.getTotal(), iPage.getRecords(), responseModel);
    }


    /**
     * 更新任务
     * @param reqModel
     * @return
     */
    @PostMapping("/taskSave")
    @ApiOperation(value = "更新任务")
    public DefaultResponseDataWrapper<String> saveTask(@RequestBody @ApiParam @Validated UpdateUserTaskReqModel reqModel){

        taskBizService.bindTask(reqModel);
        return DefaultResponseDataWrapper.success();
    }

    @PostMapping("/taskDel")
    @ApiOperation(value = "删除任务")
    public DefaultResponseDataWrapper<String> deleteTask(@RequestBody @ApiParam @Validated DeleteModel deleteModel) {

        userTaskService.delTask(deleteModel);
        return DefaultResponseDataWrapper.success();
    }
}
