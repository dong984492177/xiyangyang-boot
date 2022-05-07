package com.ywt.console.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ywt.console.entity.SysUser;
import com.ywt.console.models.resmodel.systemresmodels.QueryUserListResModel;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
public interface ISysUserService extends IService<SysUser> {

    QueryUserListResModel querySysUser(String account, String password);

    /**
     * 插入sysuser 返回 主键id
     * @param sysUser
     * @return
     */
    int saveSysUser(SysUser sysUser);
}
