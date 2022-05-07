package com.ywt.console.config;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Data
public class GrantedAuthorityImpl implements GrantedAuthority {
    public GrantedAuthorityImpl(String authority){
        super();
        this.authority = authority;
    }
    private String authority;
    @Override
    public String getAuthority() {
        return authority;
    }
}
