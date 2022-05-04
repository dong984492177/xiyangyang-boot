package com.ywt.console.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ywt.console.entity.UserTask;
import com.ywt.console.exception.ConsoleException;
import com.ywt.console.models.DeleteModel;
import com.ywt.console.models.reqmodel.UpdateTaskCategoryReqModel;
import com.ywt.console.models.reqmodel.UpdateUserTaskReqModel;
import com.ywt.console.models.reqmodel.UserTaskReqModel;
import com.ywt.console.models.resmodel.UserTaskResModel;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public interface IUserTaskService extends IService<UserTask> {

    /**
     * 添加/更新
     *
     * @param updateUserTaskReqModel
     * @return
     * @throws Exception
     */
    void saveTask(UpdateUserTaskReqModel updateUserTaskReqModel) throws ConsoleException;

    /**
     * 删除
     *
     * @param deleteModel
     * @return
     * @throws Exception
     */
    void delTask(DeleteModel deleteModel) throws ConsoleException;

    /**
     * 查询
     *
     * @param userTaskReqModel
     * @return
     * @throws Exception
     */
    IPage<UserTaskResModel> queryList(UserTaskReqModel userTaskReqModel) throws ConsoleException;
}