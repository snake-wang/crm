package com.shsxt.crm.controller;

import com.shsxt.base.BaseController;
import com.shsxt.crm.exceptions.ParamsException;
import com.shsxt.crm.service.UserService;
import com.shsxt.crm.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController extends BaseController {


    @Resource
    private UserService userService;

    /**
     * 登录页
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "index";
    }


    /**
     * 后端管理主页面
     * @return
     */
    @RequestMapping("main")
    public String main(HttpServletRequest request){
        //获取解密后的id
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //ID在cookie中一直存在,当有请求来时候,就会查询一次,作为一次请求转发附带数据
        request.setAttribute("user",userService.selectByPrimaryKey(userId));
        return "main";
    }
}
