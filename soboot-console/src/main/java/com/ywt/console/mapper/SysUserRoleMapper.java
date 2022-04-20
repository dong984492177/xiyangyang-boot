package com.ywt.console.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ywt.console.entity.SysUserRole;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 用户和角色关联表 Mapper 接口
 * </p>
 *
 * @author xijun.shao
 * @since 2020-06-05
 */
@Repository
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    List<SysUserRole> queryById(Integer userId);
}
