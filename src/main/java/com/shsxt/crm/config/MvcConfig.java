package com.shsxt.crm.config;

import com.shsxt.crm.interceptors.NoLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
//继承 WebMvcConfigurerAdapter 这个类,重写addInterceptors方法让拦截器生效!
public class MvcConfig extends WebMvcConfigurerAdapter {
    /**生效拦截器的配置
     */
    //交给Ioc
    @Bean
    public NoLoginInterceptor noLoginInterceptor(){
        return new NoLoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加一个拦截器,第一个参数中 填写要生效的方法 ,第二个参数表示拦截什么资源,第三个参数表示放行什么资源
        registry.addInterceptor(noLoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login","/index","/static/**");
    }
}
