package com.ywt.console.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ywt.console.entity.SysDept;
import com.ywt.console.models.reqmodel.systemreqmodels.QueryDeptListReqModel;
import com.ywt.console.models.resmodel.systemresmodels.QueryDeptListResModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
 * 部门mapper
 */
@Repository
public interface SysDeptMapper extends BaseMapper<SysDept> {

    IPage<QueryDeptListResModel> queryDeptList(@Param("page") Page<QueryDeptListResModel> page, @Param("param") QueryDeptListReqModel queryDeptListReqModel);
}
