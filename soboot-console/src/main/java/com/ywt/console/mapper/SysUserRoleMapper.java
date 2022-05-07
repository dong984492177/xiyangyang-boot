package com.ywt.console.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ywt.console.entity.SysUserRole;
import org.springframework.stereotype.Repository;


/**
 * 用户角色关联mapper
 */
@Repository
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    List<SysUserRole> queryById(Integer userId);
}
