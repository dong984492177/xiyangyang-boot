package com.ywt.console.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywt.console.entity.SysUser;
import com.ywt.console.mapper.SysUserMapper;
import com.ywt.console.models.resmodel.systemresmodels.QueryUserListResModel;
import com.ywt.console.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public QueryUserListResModel querySysUser(String account, String password) {
         return sysUserMapper.querySysUser(account, password);
    }

    @Override
    public int saveSysUser(SysUser sysUser) {
        return sysUserMapper.insert(sysUser);
    }

}
