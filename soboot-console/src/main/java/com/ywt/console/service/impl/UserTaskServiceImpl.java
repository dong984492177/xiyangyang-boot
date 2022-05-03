package com.ywt.console.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywt.common.base.util.BeanMapping;
import com.ywt.common.config.redis.RedissonService;
import com.ywt.console.entity.TaskCategory;
import com.ywt.console.entity.UserTask;
import com.ywt.console.exception.ConsoleException;
import com.ywt.console.mapper.TaskCategoryMapper;
import com.ywt.console.mapper.UserTaskMapper;
import com.ywt.console.models.DeleteModel;
import com.ywt.console.models.reqmodel.TaskCategoryReqModel;
import com.ywt.console.models.reqmodel.UpdateTaskCategoryReqModel;
import com.ywt.console.models.reqmodel.UpdateUserTaskReqModel;
import com.ywt.console.models.reqmodel.UserTaskReqModel;
import com.ywt.console.models.resmodel.TaskCategoryResModel;
import com.ywt.console.models.resmodel.UserTaskResModel;
import com.ywt.console.service.ITaskCategoryService;
import com.ywt.console.service.IUserTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Service
public class UserTaskServiceImpl extends ServiceImpl<UserTaskMapper, UserTask> implements IUserTaskService {

    @Autowired
    private UserTaskMapper mapper;

    @Override
    public void saveTask(UpdateUserTaskReqModel updateUserTaskReqModel) throws ConsoleException {

        UpdateWrapper<UserTask> wrapper = new UpdateWrapper<>();
        wrapper.eq("id",updateUserTaskReqModel.getId());
        wrapper.set("state",updateUserTaskReqModel.getState());
        mapper.update(null,wrapper);
    }

    @Override
    public void delTask(DeleteModel deleteModel) throws ConsoleException {
        if (deleteModel.getIds().size() > 0) {
            List<UserTask> userTaskList = new ArrayList<>();
            for (Integer id : deleteModel.getIds()) {
                userTaskList.add(UserTask.builder().id(id).isDelete(1).build());
            }
            if (!updateBatchById(userTaskList)) {
                throw new ConsoleException("删除失败");
            }
        }
    }

    @Override
    public IPage<UserTaskResModel> queryList(UserTaskReqModel userTaskReqModel) throws ConsoleException {
        Page<UserTaskResModel> page = new Page<>(userTaskReqModel.getPageNo(), userTaskReqModel.getPageSize());
        return mapper.findList(page, userTaskReqModel);
    }
}
