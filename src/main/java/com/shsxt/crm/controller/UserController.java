package com.shsxt.crm.controller;

import com.shsxt.base.BaseController;
import com.shsxt.crm.model.ResultInfo;
import com.shsxt.crm.model.UserModel;
import com.shsxt.crm.service.UserService;
import com.shsxt.crm.utils.LoginUserUtil;
import com.shsxt.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController extends BaseController {
    @Resource
    private UserService userService;


    @GetMapping("user/queryUserByUserId")   //get请求,requestMapping表示这是一个请求,没有规定类型
    @ResponseBody                           //响应格式是json
    public User queryUserByUserId(Integer userId) {
        return userService.selectByPrimaryKey(userId);
    }


    @RequestMapping("user/login")          //发送的是post请求
    @ResponseBody                       //返回值是json格式
    public ResultInfo login(String userName, String userPwd) {
        UserModel userModel = userService.login(userName, userPwd);
        //成功时候传入msg,和result的信息,涉及到baseController的重写success方法
        return success("用户登录成功",userModel);
    }

    @RequestMapping("user/updatePassword")
    @ResponseBody
    //要获取id,从cookie,cookie从请求中来
    public ResultInfo updatePassword(HttpServletRequest request, String oldPassword, String newPassword, String confirmPassword) {
        userService.updateUserPassword(LoginUserUtil.releaseUserIdFromCookie(request), oldPassword, newPassword, confirmPassword);
        //这个success方法是在baseController中创建的,有重载
        return success("密码更新成功");
    }

}
