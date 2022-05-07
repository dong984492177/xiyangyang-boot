package com.ywt.console.service;

import java.util.List;

import com.ywt.console.entity.SysRole;
import com.ywt.console.models.reqmodel.systemreqmodels.QueryRoleListReqModel;
import com.ywt.console.models.resmodel.systemresmodels.QueryRoleListResModel;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public interface ISysRoleService extends IService<SysRole> {

    List<SysRole> selectRoleByUserId(int userId);

    IPage<QueryRoleListResModel> getRoleList(Page<QueryRoleListResModel> page, QueryRoleListReqModel queryRoleListReqModel);

    int insertRole(SysRole sysRole);

    List<SysRole> queryByRoleKeyOrRoleName(QueryWrapper<SysRole> wrapper);

    int deleteById(Integer id);
}
