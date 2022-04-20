package com.ywt.console.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ywt.console.entity.SysRole;
import com.ywt.console.models.reqmodel.systemreqmodels.QueryRoleListReqModel;
import com.ywt.console.models.resmodel.systemresmodels.QueryRoleListResModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 角色信息表 Mapper 接口
 * </p>
 *
 * @author xijun.shao
 * @since 2020-06-05
 */
@Repository
public interface SysRoleMapper extends BaseMapper<SysRole> {

    IPage<QueryRoleListResModel> getRoleList(@Param("page") Page<QueryRoleListResModel> page, @Param("param") QueryRoleListReqModel queryRoleListReqModel);

    List<SysRole> selectRoleByUserId(@Param("userId") int userId);
}
