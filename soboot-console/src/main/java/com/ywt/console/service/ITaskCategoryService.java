package com.ywt.console.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ywt.console.entity.TaskCategory;
import com.ywt.console.exception.ConsoleException;
import com.ywt.console.models.DeleteModel;
import com.ywt.console.models.reqmodel.TaskCategoryReqModel;
import com.ywt.console.models.reqmodel.UpdateTaskCategoryReqModel;
import com.ywt.console.models.resmodel.TaskCategoryResModel;

import java.util.List;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public interface ITaskCategoryService extends IService<TaskCategory> {

    /**
     * 添加/更新
     *
     * @param reqModel
     * @return
     * @throws Exception
     */
    TaskCategoryResModel saveCategory(UpdateTaskCategoryReqModel reqModel) throws ConsoleException;

    /**
     * 删除
     *
     * @param deleteModel
     * @return
     * @throws Exception
     */
    void delCategory(DeleteModel deleteModel) throws ConsoleException;

    /**
     * 查询分页
     *
     * @param taskCategoryReqModel
     * @return
     * @throws Exception
     */
    IPage<TaskCategoryResModel> queryListWithPage(TaskCategoryReqModel taskCategoryReqModel) throws ConsoleException;

    /**
     * 查询
     * @param id
     * @param parentId
     * @return
     * @throws Exception
     */
    List<TaskCategoryResModel> queryList(Integer parentId,Integer id) throws ConsoleException;
}
