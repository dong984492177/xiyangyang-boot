package com.ywt.console.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: huangchaoyang
 * @Description: 全局拦截器
 * @Version: 1.0
 * @Create: 2021/1/12
 * @Copyright: 云网通信息科技
 */
@Configuration
public class GlobalInterceptor implements  WebMvcConfigurer {

   /*@Bean
    public ApiLogInterceptor apiLogInterceptor(){
        return new ApiLogInterceptor();
    }*/

    /** 添加拦截器 **/
    @Override
    public  void addInterceptors(InterceptorRegistry registry){

       // registry.addInterceptor(apiLogInterceptor()).addPathPatterns("/system/login");
       //registry.addInterceptor(apiLogInterceptor()).addPathPatterns("/**").excludePathPatterns("/job/**");
    }
}
