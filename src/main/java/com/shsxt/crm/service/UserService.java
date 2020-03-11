package com.shsxt.crm.service;

import com.github.pagehelper.PageException;
import com.shsxt.base.BaseService;
import com.shsxt.crm.dao.UserMapper;
import com.shsxt.crm.exceptions.ParamsException;
import com.shsxt.crm.model.UserModel;
import com.shsxt.crm.utils.AssertUtil;
import com.shsxt.crm.utils.Md5Util;
import com.shsxt.crm.utils.UserIDBase64;
import com.shsxt.crm.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService extends BaseService<User,Integer> {

    @Resource
    private UserMapper userMapper;

    public UserModel login(String userName,String userPwd){
        /**
         * 1.参数校验非空
         *      用户名 非空
         *      密码 非空
         * 2.根据用户名 查询用户记录
         * 3.校验用户存在性
         *      不存在    -->提示用户不存在  方法结束
         * 4.用户存在
         *       校验密码
         *       密码错误  -->提示密码错误
         *       密码正确  用户登录成功,返回用户相关信息
         */
        //1.校验参数非空
        checkLoginParams(userName,userPwd);
        //2.根据用户名查询用户记录
        User user =  userMapper.queryUSerByUserName(userName);
        //3.校验用户是否存在
        AssertUtil.isTrue(null==user,"用户不存在或者已注销!");
        //4.用户存在校验密码
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(userPwd)),"密码不正确!");
        //5.密码正确返回用户信息
        return buildUserModelInfo(user);
    }

    private UserModel buildUserModelInfo(User user) {
        //在将数据库中查到的数据展现出来的时候加密敏感字段ID
        return new UserModel(UserIDBase64.encoderUserID(user.getId()),user.getUserName(),user.getTrueName());
    }

    private void checkLoginParams(String userName,String userPwd){
        //如果为真,抛异常
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不可为空!");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"用户密码不可为空!");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserPassword(Integer userId,String oldPassword,String newPassword,String confirmPassword){
        /**
         * 1.参数校验
         *      userId 非空 必须有这个id
         *      oldPassword 非空 必须与数据库的一致
         *      newPassword 非空 新密码不能与原始密码相同
         *      confirmPassword 非空 与新密码必须一一致
         * 2.设置用户新密码
         *      新密码加密
         * 3.执行更新
         */
        //1.参数校验
        checkParams(userId,oldPassword,newPassword,confirmPassword);
        //2.设置用户的新密码
        //用的是baseService的,所以不需要用userService
        User user = selectByPrimaryKey(userId);
        //3.将新密码加密
        user.setUserPwd(Md5Util.encode(newPassword));
        //4.执行更新,也是basedao的方法
        AssertUtil.isTrue(updateByPrimaryKeySelective(user)<1,"密码更新失败!");


    }

    private void checkParams(Integer userId, String oldPassword, String newPassword, String confirmPassword) {
        User user = userMapper.selectByPrimaryKey(userId);
        AssertUtil.isTrue(null==userId||null==user,"用户不存在或未登录!");
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword),"请输入原始密码");
        AssertUtil.isTrue(StringUtils.isBlank(newPassword),"请输入新密码");
        AssertUtil.isTrue(StringUtils.isBlank(confirmPassword),"请输入确认密码");
        AssertUtil.isTrue(!(newPassword.equals(confirmPassword)),"新密码与确认密码输入不一致! ");
        AssertUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(oldPassword))),"输入密码有误!");
        AssertUtil.isTrue(newPassword.equals(oldPassword),"新密码不能与旧密码相同!");
    }


}
