package com.ywt.console.config;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ywt.common.enums.DeleteStatus;
import com.ywt.console.entity.SysUser;
import com.ywt.console.service.impl.SysUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserServiceImpl userService;

    @Override
    public UserDetails loadUserByUsername(String username)throws UsernameNotFoundException {
        List<SysUser> users = userService.list(new LambdaQueryWrapper<SysUser>().eq(SysUser::getPhonenumber, username)
                .eq(SysUser::getIsDelete, DeleteStatus.NOT_DELETE.getValue()));
        if (users.size() == 0) {
            return null;
        }
        return new User(JSON.toJSONString(users.get(0)), users.get(0).getPassword(), Collections.emptyList());
    }
}
