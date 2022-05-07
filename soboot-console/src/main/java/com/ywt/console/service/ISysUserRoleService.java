package com.ywt.console.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ywt.console.entity.SysUserRole;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public interface ISysUserRoleService extends IService<SysUserRole> {

    public List<SysUserRole> queryById(Integer userId);
}
