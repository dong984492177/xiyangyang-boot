package com.ywt.console.config;

import com.alibaba.fastjson.JSON;
import com.ywt.common.base.constant.XiotConstant;
import com.ywt.common.config.redis.RedissonService;

import com.ywt.console.constant.ResCode;
import com.ywt.console.entity.SysUser;
import com.ywt.console.entity.SysUserRole;
import com.ywt.console.models.reqmodel.LoginReqModel;
import com.ywt.console.models.reqmodel.systemreqmodels.QueryMenuListReqModel;
import com.ywt.console.models.resmodel.systemresmodels.QueryMenuListResModel;
import com.ywt.console.models.resmodel.systemresmodels.QueryUserListResModel;
import com.ywt.console.service.impl.SysMenuServiceImpl;
import com.ywt.console.service.impl.SysUserRoleServiceImpl;
import com.ywt.console.service.impl.SysUserServiceImpl;
import com.ywt.console.utils.Util;
import org.redisson.api.RBucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: huangchaoyang
 * @Description:  登陆授权认证
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Service
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private SysUserRoleServiceImpl sysUserRoleService;
    @Autowired
    private SysUserServiceImpl sysUserService;
    @Autowired
    private SysMenuServiceImpl menuService;
    @Autowired
    private RedissonService redissonService;
    @Autowired
    private UserDetailsService userDetailsService;

    public CustomAuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取认证的用户名 & 密码
        String c = JSON.toJSONString(authentication.getCredentials());
        LoginReqModel loginReqModel = JSON.parseObject(c, LoginReqModel.class);

        QueryUserListResModel queryUserListResModel = sysUserService.querySysUser(loginReqModel.getAccount(), loginReqModel.getPassword());
        if(queryUserListResModel != null){
            loginReqModel.setAccount(queryUserListResModel.getPhonenumber());
        }
        // 认证逻辑
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginReqModel.getAccount());
        if (null != userDetails) {
            SysUser user = JSON.parseObject(userDetails.getUsername(), SysUser.class);
            if (user.getIsDelete() != 0) {
                throw new BadCredentialsException(ResCode.FORBIDDEN.toString());
            }
            //查询用户角色
            List<SysUserRole> roleList = sysUserRoleService.queryById(user.getId());
            List<SysUserRole> existRoleList = roleList.stream().filter(s->s.getRoleId()==1).collect(Collectors.toList());

            if (StringUtils.isEmpty(loginReqModel.getPassword()) || !loginReqModel.getPassword() .equals(userDetails.getPassword())) {

                if (!ObjectUtils.isEmpty(existRoleList)) {
                    //管理员
                    throw new BadCredentialsException(ResCode.PWD_ERROR.toString());
                } else {
                    String lockNum = redissonService.getString("lock_" + user.getId());
                    if (ObjectUtils.isEmpty(lockNum)) {
                        lockNum = "0";
                    }
                    if (!"1".equals(user.getIsLock())) {
                        if (Integer.valueOf(lockNum) < 5) {
                            int count = Integer.valueOf(lockNum);
                            count++;
                            redissonService.setString("lock_" + user.getId(), String.valueOf(count),1,TimeUnit.MINUTES);
                            if(count == 5){
                                user.setIsLock("1");
                                sysUserService.updateById(user);
                                throw new BadCredentialsException(ResCode.LOCKED.toString());
                            }else{
                                throw new BadCredentialsException(ResCode.PWD_ERROR.toString());
                            }
                        }
                    } else if (user.getIsDelete() != 0) {
                        throw new BadCredentialsException(ResCode.FORBIDDEN.toString());
                    }
                    if (!"0".equals(user.getIsLock())) {
                        throw new BadCredentialsException(ResCode.LOCKED.toString());
                    }
                }
            }else{
                if(loginReqModel.getPassword() .equals(userDetails.getPassword())){
                    String lockNum = redissonService.getString("lock_" + user.getId());
                    if (!ObjectUtils.isEmpty(lockNum)) {
                        if(Integer.valueOf(lockNum) >= 5){
                            throw new BadCredentialsException(ResCode.LOCKED.toString());
                        }else{
                            redissonService.deleteString("lock_" + user.getId());
                        }
                    }else{
                        if(user.getIsLock().equals("1")){
                            user.setIsLock("0");
                            sysUserService.updateById(user);
                        }
                    }
                }
            }

            QueryMenuListReqModel menuListReqModel = new QueryMenuListReqModel();
            List<QueryMenuListResModel> permissons = new ArrayList<>();
            if (user.getId() == 1) {
                permissons = menuService.getMenuList(menuListReqModel);
            } else {
                menuListReqModel.setUserId(user.getId());
                permissons = menuService.getMenuListByUserId(menuListReqModel);
            }
            Set<String> set = new HashSet<>();
            for (QueryMenuListResModel permisson : permissons) {
                if (null != permisson) {
                    set.add(permisson.getPerms());
                }
            }
            ArrayList<GrantedAuthority> authorities = new ArrayList<>();
            for (String s : set) {
                authorities.add(new GrantedAuthorityImpl(s));
            }

            RBucket<Object> rBucket = redissonService.getRBucket(Util.getPermissonKey(user.getPhonenumber()));
            rBucket.set(authorities);
            rBucket.expire(XiotConstant.JWT_TOKEN_TIME, TimeUnit.MILLISECONDS);
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user.getId(), user.getPhonenumber(), userDetails.getAuthorities()));
            return new UsernamePasswordAuthenticationToken(user.getId(), user.getPhonenumber(), userDetails.getAuthorities());
        } else {
            throw new UsernameNotFoundException(ResCode.GET_ERROR.toString());
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
