package com.shsxt.crm.utils;

import com.shsxt.crm.exceptions.ParamsException;

//断言
public class AssertUtil {
    /*静态的*/
    public  static void isTrue(Boolean flag,String msg){
        /*如果为真,表达式成立,就抛异常*/
        if(flag){
            throw  new ParamsException(msg);
        }
    }

}
