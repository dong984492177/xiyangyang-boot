package com.ywt.console.mapper;

import com.ywt.console.entity.log.LoginLog;
import com.ywt.console.models.reqmodel.QueryLoginLogReqModel;
import com.ywt.console.models.resmodel.QueryLoginLogResModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 登陆日志mapper
 */
@Repository
public interface LoginLogMapper extends BaseMapper<LoginLog> {

    IPage<QueryLoginLogResModel> findList(@Param("page") Page<QueryLoginLogResModel> page, @Param("param") QueryLoginLogReqModel queryLoginLogReqModel);

}
