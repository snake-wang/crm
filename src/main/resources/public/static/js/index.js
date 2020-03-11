function login() {
    var userName=$("input[name='userName']").val();
    var userPwd=$("input[name='password']").val();

    if(isEmpty(userName)){
        alert("请输入用户名!");
        return;
    }
    //isEmpty是common.js的一个方法
    if(isEmpty(userPwd)){
        alert("请输入密码!");
        return;
    }

    $.ajax({
        type:"post",
        //ctx是字符串,这是字符串的拼接
        url:ctx+"/user/login",
        data:{
            userName:userName,
            userPwd:userPwd
        },
        dataType:"json",
        success:function (data) {
            console.log(data);
            if(data.code==200){
                //data表示controller层传递过来的对象,包含code,msg,result
                //var出来的result是真的userModel对象
                var result =data.result;
                /**
                 * 写入cookie 到浏览器
                 * 此处用的是一个jequery的插件jquery.cookie.js来写cookie
                 * 用法是:1.导入jquery.cookie.js  2.使用时:$.cookie(key,value,option)
                 */
                $.cookie("userIdStr",result.userIdStr);
                $.cookie("userName",result.userName);
                $.cookie("trueName",result.trueName);
                //界面跳转到主界面
                window.location.href=ctx+"/main";
            }else{
                alert(data.msg);
            }
        }
    })




}