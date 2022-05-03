package com.ywt.console.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ywt.console.entity.TaskCategory;
import com.ywt.console.models.reqmodel.TaskCategoryReqModel;
import com.ywt.console.models.resmodel.TaskCategoryResModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
 * 任务分类mapper
 */
@Repository
public interface TaskCategoryMapper extends BaseMapper<TaskCategory> {

    IPage<TaskCategoryResModel> findList(@Param("page") Page<TaskCategoryResModel> page, @Param("param") TaskCategoryReqModel reqModel);
}
