package com.ywt.console.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ywt.console.entity.SysRole;
import com.ywt.console.mapper.SysRoleMapper;
import com.ywt.console.models.reqmodel.systemreqmodels.QueryRoleListReqModel;
import com.ywt.console.models.resmodel.systemresmodels.QueryRoleListResModel;
import com.ywt.console.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public List<SysRole> selectRoleByUserId(int userId) {
        return sysRoleMapper.selectRoleByUserId(userId);
    }

    @Override
    public IPage<QueryRoleListResModel> getRoleList(Page<QueryRoleListResModel> page, QueryRoleListReqModel queryRoleListReqModel) {
        return sysRoleMapper.getRoleList(page,queryRoleListReqModel);
    }

    @Override
    public int insertRole(SysRole sysRole) {
        return sysRoleMapper.insert(sysRole);
    }

    @Override
    public List<SysRole> queryByRoleKeyOrRoleName(QueryWrapper<SysRole> wrapper) {

        return sysRoleMapper.selectList(wrapper);
    }

    @Override
    public int deleteById(Integer id) {
        return sysRoleMapper.deleteById(id);
    }
}
