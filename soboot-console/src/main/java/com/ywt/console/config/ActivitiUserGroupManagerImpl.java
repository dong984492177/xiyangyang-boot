package com.ywt.console.config;

import org.activiti.api.runtime.shared.identity.UserGroupManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: huangchaoyang
 * @Description:  重写activiti用户组服务
 * @Version: 1.0
 * @Create: 2022-05-06 11:20:06
 * @Copyright: 互邦老宝贝
 */
@Primary
@Service("ActivitiUserGroupManagerImpl")
public class ActivitiUserGroupManagerImpl implements UserGroupManager {

    private String GROUP_ROLE = "GROUP_";

    private List<String> getAuthorities(String role){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<Object> list = authentication.getAuthorities().stream().filter(s->String.valueOf(s).startsWith(role)).collect(Collectors.toSet());
        List<String> result = new ArrayList<>();
        list.forEach(v->{
            result.add(String.valueOf(v));
        });
        return result;
    }
    @Override
    public List<String> getUserGroups(String authenticationUserId) {

        return getAuthorities(GROUP_ROLE);
    }

    @Override
    public List<String> getUserRoles(String s) {

        return getAuthorities("ROLE_");
    }

    @Override
    public List<String> getGroups() {
        return null;
    }

    @Override
    public List<String> getUsers() {
        return null;
    }
}
