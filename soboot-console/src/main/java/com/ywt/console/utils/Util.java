package com.ywt.console.utils;

import com.ywt.common.base.constant.XiotConstant;
import com.ywt.common.bean.PageWrapper;
import com.ywt.common.response.DefaultResponseDataWrapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author: huangchaoyang
 * @Description:
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
public class Util {

    public static <T> DefaultResponseDataWrapper<List<T>> parsePageModel(PageWrapper pageModel, Long count, List<T> data, DefaultResponseDataWrapper<List<T>> res) {
        pageModel.setTotal(Integer.parseInt(count.toString()));
        res.setData(data);
        res.setPage(pageModel);
        return res;
    }

    public static Integer getTokenUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return 0;
        Object principal = authentication.getPrincipal();
        return null == principal ? 0 : Integer.parseInt(principal.toString());
    }

    public static String getTokenPhone() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return "";
        Object principal = authentication.getCredentials();
        return null == principal ? "" : principal.toString();
    }

    public static boolean isAdmin() {
        return getTokenUserId() == 1;
    }


    public static String getPermissonKey(String phoneNumber) {
        return XiotConstant.JWT_PERMISSON + ":" + phoneNumber;
    }

    public static String getRouterKey() {
        return isAdmin() ?  XiotConstant.JWT_ROUTERS + ":ADMIN" : XiotConstant.JWT_ROUTERS + ":" + getTokenUserId();
    }

    public static String getRouterKey(Integer userId) {
        return userId == 1 ?  XiotConstant.JWT_ROUTERS + ":ADMIN" : XiotConstant.JWT_ROUTERS + ":" + userId;
    }

    /**
     * 获取response
     * @return
     */
    public static HttpServletResponse getResponse(){
        HttpServletResponse response =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        return response;
    }
}
