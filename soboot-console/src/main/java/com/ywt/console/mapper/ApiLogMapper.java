package com.ywt.console.mapper;


import com.ywt.console.entity.log.ApiLog;
import com.ywt.console.models.reqmodel.QueryApiLogReqModel;
import com.ywt.console.models.resmodel.QueryApiLogResModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

@Repository
public interface ApiLogMapper extends BaseMapper<ApiLog> {

    IPage<QueryApiLogResModel> findList(@Param("page") Page<QueryApiLogResModel> page, @Param("param") QueryApiLogReqModel queryApiLogReqModel);

}
