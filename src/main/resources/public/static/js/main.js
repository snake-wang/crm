//打开选项卡
function openTab(text, url, iconCls){
    if($("#tabs").tabs("exists",text)){
        $("#tabs").tabs("select",text);
    }else{
        var content="<iframe frameborder=0 scrolling='auto' style='width:100%;height:100%' src='" + url + "'></iframe>";
        $("#tabs").tabs("add",{
            title:text,
            iconCls:iconCls,
            closable:true,
            content:content
        });
    }
}

//退出操作
function logout() {
    $.messager.confirm("来自crm","确定退出系统?",function (r) {
        if(r){
            $.removeCookie("userIdStr");
            $.removeCookie("userName");
            $.removeCookie("trueName");
            $.messager.alert("来自crm","系统将在三秒后自动退出...","info");
            setTimeout(function () {
                window.location.href=ctx+"/index";
            },3000);
        }
    })
}


function openPasswordModifyDialog() {
    //打开修改密码的对话框
    $("#dlg").dialog("open").dialog("setTitle","密码修改");
}

function modifyPassword() {
    $("#fm").form("submit",{
        //此处的ctx是一个字符串,在main.ftl中已经有定义了,表示的是:"${ctx}"
        url:ctx+"/user/updatePassword",
        onSubmit:function () {
            //调用表单校验方法
            return $("#fm").form("validate");
        },
        success:function (data) {
            //此处拿到的data是一个源生的json字符串,用fastJson来转成json格式
            data =JSON.parse(data);
            //密码修改成功就要强制退出
            if(data.code==200){
                $.messager.alert("来自crm","密码修改成功,系统将在5秒后执行退出操作...","info");
                $.removeCookie("userIdStr");
                $.removeCookie("userName");
                $.removeCookie("trueName");
                setTimeout(function () {
                    window.location.href=ctx+"/index";
                },5000)
            }else{
                $.messager.alert("来自crm",data.msg,"error");
            }
        }
    })
}