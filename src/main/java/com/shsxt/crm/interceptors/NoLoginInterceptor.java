package com.shsxt.crm.interceptors;

import com.shsxt.crm.exceptions.NoLoginException;
import com.shsxt.crm.service.UserService;
import com.shsxt.crm.utils.LoginUserUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//拦截器
//继承这个可以只重写部分的,不用全部重写
public class NoLoginInterceptor extends HandlerInterceptorAdapter {
    @Resource
    private UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**
         * 获取cookie 解析用户id
         *      如果用户id存在 并且 数据库存在对应的用户记录  放行, 否则进行拦截,重定向到登录
         */
        //获取用户的id
        int userId = LoginUserUtil.releaseUserIdFromCookie(request);
        /*//不用重定向,会有套娃问题
        if (userId == 0|| null==userService.selectByPrimaryKey(userId)){
            //重定向到登录界面
            response.sendRedirect(request.getContextPath()+"/index");
            return false;
        }*/
        if(userId==0||null==userService.selectByPrimaryKey(userId)){
            //如果没有userId或者找不到,抛一个特定的异常(这个异常还不能是一个参数异常,专门写一个异常来表示),这个异常全局异常会处理
            throw new NoLoginException();
        }

        return true;
    }
}
