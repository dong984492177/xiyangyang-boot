package com.ywt.console.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ywt.console.entity.UserTask;
import com.ywt.console.models.reqmodel.UserTaskReqModel;
import com.ywt.console.models.resmodel.UserTaskResModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
 * 任务mapper
 */
@Repository
public interface UserTaskMapper extends BaseMapper<UserTask> {

    IPage<UserTaskResModel> findList(@Param("page") Page<UserTaskResModel> page, @Param("param") UserTaskReqModel reqModel);
}
