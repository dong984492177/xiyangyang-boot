package com.ywt.console.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywt.common.base.util.BeanMapping;
import com.ywt.common.config.redis.RedissonService;
import com.ywt.console.entity.TaskCategory;
import com.ywt.console.exception.ConsoleException;
import com.ywt.console.mapper.TaskCategoryMapper;
import com.ywt.console.models.DeleteModel;
import com.ywt.console.models.reqmodel.*;
import com.ywt.console.models.resmodel.TaskCategoryResModel;
import com.ywt.console.service.ITaskCategoryService;
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
public class TaskCategoryServiceImpl extends ServiceImpl<TaskCategoryMapper, TaskCategory> implements ITaskCategoryService {

    @Autowired
    private TaskCategoryMapper mapper;

    @Autowired
    private RedissonService redissonService;

    @Override
    public TaskCategoryResModel saveCategory(UpdateTaskCategoryReqModel updateTaskCategoryReqModel) throws ConsoleException {

        Integer id = updateTaskCategoryReqModel.getId();
        TaskCategory taskCategory = BeanMapping.map(updateTaskCategoryReqModel,TaskCategory.class);
        if(ObjectUtils.isEmpty(id)){
            mapper.insert(taskCategory);
        }
        mapper.updateById(taskCategory);
        return BeanMapping.map(updateTaskCategoryReqModel,TaskCategoryResModel.class);
    }

    @Override
    public void delCategory(DeleteModel deleteModel) throws ConsoleException {
        if (deleteModel.getIds().size() > 0) {
            List<TaskCategory> taskCategoryList = new ArrayList<>();
            for (Integer id : deleteModel.getIds()) {
                taskCategoryList.add(TaskCategory.builder().id(id).isDelete(1).build());
            }
            if (!updateBatchById(taskCategoryList)) {
                throw new ConsoleException("删除失败");
            }
        }
    }

    @Override
    public IPage<TaskCategoryResModel> queryList(TaskCategoryReqModel taskCategoryReqModel) throws ConsoleException {

        Page<TaskCategoryResModel> page = new Page<>(taskCategoryReqModel.getPageNo(), taskCategoryReqModel.getPageSize());
        return mapper.findList(page, taskCategoryReqModel);
    }
}
