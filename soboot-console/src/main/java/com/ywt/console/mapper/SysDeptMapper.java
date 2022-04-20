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
 * <p>
 * 部门表 Mapper 接口
 * </p>
 *
 * @author xijun.shao
 * @since 2020-06-10
 */
@Repository
public interface SysDeptMapper extends BaseMapper<SysDept> {

    IPage<QueryDeptListResModel> queryDeptList(@Param("page") Page<QueryDeptListResModel> page, @Param("param") QueryDeptListReqModel queryDeptListReqModel);
}
