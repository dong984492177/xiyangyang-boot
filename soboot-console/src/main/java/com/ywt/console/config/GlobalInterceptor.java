package com.ywt.console.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: huangchaoyang
 * @Description: 全局拦截器
 * @Version: 1.0
 * @Create: 2021/1/12
 */
@Configuration
public class GlobalInterceptor implements  WebMvcConfigurer {

    @Autowired
    private ApiLogInterceptor apiLogInterceptor;

    /** 添加拦截器 **/
    @Override
    public  void addInterceptors(InterceptorRegistry registry){

       registry.addInterceptor(apiLogInterceptor).addPathPatterns("/system/login");
       //registry.addInterceptor(apiLogInterceptor()).addPathPatterns("/**").excludePathPatterns("/job/**");
    }
}
