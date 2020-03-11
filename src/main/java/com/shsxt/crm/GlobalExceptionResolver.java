package com.shsxt.crm;


import com.alibaba.fastjson.JSON;
import com.shsxt.crm.exceptions.NoLoginException;
import com.shsxt.crm.exceptions.ParamsException;
import com.shsxt.crm.interceptors.NoLoginInterceptor;
import com.shsxt.crm.model.ResultInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Component
//实现接口,这个节后就是用来处理异常的
public class GlobalExceptionResolver implements HandlerExceptionResolver {


    @Override
    //默认返回的是视图 ModelAndView
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {


        ModelAndView mv = new ModelAndView();
        //设置默认的错误页面
        mv.setViewName("errors");
        mv.addObject("code",400);
        mv.addObject("msg","系统异常,请稍后再试...");

        /**
         * 首先判断异常类型
         *      如果异常类型为未登录异常 执行视图转发
         */
        if (ex instanceof NoLoginException){
            NoLoginException ne = (NoLoginException) ex;
            mv.setViewName("no_login");
            mv.addObject("msg",ne.getMsg());
            mv.addObject("ctx",request.getContextPath());
            return mv;
        }


        /**
         * 方法的返回值   可能是视图,也可能是json格式的字符串
         * 如何判断是视图还是json?                   我们约定,有@ResponseBody这个注解的返回值是json,其他都是视图
         * 如何获取注解?可以用handler这个参数        我们约定,当handler 参数类型是HandlerMethod这个类型时候
         * HandlerMethod是什么?当在地址栏输入地址时候,前面的http:localhost:8080/crm这是个前缀,
         *                                          后面的/main或者/index等等这些uri对应后端的方法,这些方法在mvc中被包装成HandlerMethod
         *                                          注意是要符合mvc请求的,静态资源也是可以直接通过输入地址栏去方法,所以要排除
         * 如果是视图:返回默认的错误页面
         * 如果是json:返回错误的json信息
         */


        if (handler instanceof HandlerMethod){
            HandlerMethod hm = (HandlerMethod) handler;
            //hm.getMethod()拿的是这个方法的反射对象的值,每一个类都有一个反射
            //再去获取方法的注解
            ResponseBody responseBody = hm.getMethod().getDeclaredAnnotation(ResponseBody.class);
            //如果没有这个注解,表示方法返回的是一个视图
            if (null == responseBody){
                /**
                 * 如果后端方法的返回值是视图
                 */
                if (ex instanceof ParamsException){
                    ParamsException pe = (ParamsException) ex;
                    mv.addObject("code",pe.getCode());
                    mv.addObject("msg",pe.getMsg());
                }
                return mv;
            }else{
                /**
                 * 如果后端方法的返回值是json
                 */
                ResultInfo resultInfo = new ResultInfo();
                //定义一些默认的json报错
                resultInfo.setCode(300);
                resultInfo.setMsg("系统错误,请稍后再试...");
                //如果是前面定义的那些参数异常,报对应的错误
                //如果有其他异常,继续往下if就行了
                if (ex instanceof ParamsException){
                    ParamsException pe = (ParamsException) ex;
                    resultInfo.setCode(pe.getCode());
                    resultInfo.setMsg(pe.getMsg());
                }

                //这是一个重写方法,必须返回的是视图,如果是json,如何处理:变成字符串写出去
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json;charset=utf-8");
                //创建一个输入流
                PrintWriter pw = null;
                //凡是涉及到流的都会出异常
                // 这是一个全局异常统一处理,这里的异常就不能再抛了;
                try {
                    pw = response.getWriter();
                    //拿到pw,我们就去写,把resultInfo以字符串的形式写
                    //把对象转成字符串?  用 fastJson
                    pw.write(JSON.toJSONString(resultInfo));
                    pw.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    //如果有这个pw,就要关闭
                    if (null!=pw){
                        pw.close();
                    }
                    //如果是json,返回值为空
                    return null;
                }
            }
        }else{
            return mv;
        }

    }
}
