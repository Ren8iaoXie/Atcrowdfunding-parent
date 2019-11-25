<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="content-type" content="text/html" charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="keys" content="">
    <meta name="author" content="">
    <link rel="stylesheet" href="${pageContext.request.contextPath }/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath }/css/font-awesome.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath }/css/login.css">
    <script src="${pageContext.request.contextPath }/jquery/jquery-2.1.1.min.js"></script>
    <script src="${pageContext.request.contextPath }/bootstrap/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/jquery/layer/layer.js"></script>
    <script>
        function dologin() {
            //同步方式
            // $("#loginForm").submit();
            //异步方式
            var floginacct=$("#loginacct");
            var option=$("#type option:selected");
            var ftype=option.val();
            //判断用户名是否为空 若为空则不走后台直接清除
            if($.trim(floginacct.val())==""){
                    layer.msg("用户账户名不能为空，请重新输入！",{time:1000, icon:5, shift:6},function () {
                    //回调函数
                    floginacct.val("");
                    floginacct.focus();
                })

                return false;
            }
            if ("user" ==ftype){
                $.ajax({
                    type:"post",
                    url:"${pageContext.request.contextPath}/user/doLogin.do",
                    data:$("#loginForm").serialize(),
                    success:function (data) {
                        if (data.success) {
                            window.location.href="${pageContext.request.contextPath}/main.htm"
                        } else {
                            // alert(data.message)
                            layer.msg(data.message,{time:1000, icon:5, shift:6})
                        }
                    },
                    error:function () {
                    }
                });
            } else {
                $.ajax({
                    type:"post",
                    url:"${pageContext.request.contextPath}/member/doLogin.do",
                    data:$("#loginForm").serialize(),
                    success:function (data) {
                        if (data.success) {
                            window.location.href="${pageContext.request.contextPath}/member/member.htm"
                        } else {
                            // alert(data.message)
                            layer.msg(data.message,{time:1000, icon:5, shift:6})
                        }
                    },
                    error:function () {
                    }
                });
            }

        }
    </script>
</head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <div><a class="navbar-brand" href="index.html" style="font-size:32px;">尚筹网-创意产品众筹平台</a></div>
        </div>
    </div>
</nav>



    <form id="loginForm" method="POST" class="form-signin" role="form">
        <%--${exception.message }--%>
        <h2 class="form-signin-heading"><i class="glyphicon glyphicon-log-in"></i> 用户登录</h2>

        <div class="form-group has-success has-feedback">
            <input type="text" class="form-control" id="loginacct" name="loginacct" value="superadmin" placeholder="请输入登录账号" autofocus>
            <span class="glyphicon glyphicon-user form-control-feedback"></span>
        </div>

        <div class="form-group has-success has-feedback">
            <input type="password" class="form-control" id="userpswd" name="userpswd" value="123" placeholder="请输入登录密码" style="margin-top:10px;">
            <span class="glyphicon glyphicon-lock form-control-feedback"></span>
        </div>

        <div class="form-group has-success has-feedback">
            <select id="type" class="form-control" name="type">
                <option value="member" selected>会员</option>
                <option value="user" >管理</option>
            </select>
        </div>
        <%--<div class="checkbox">--%>
            <%--<label>--%>
                <%--<input type="checkbox" value="remember-me"> 记住我--%>
            <%--</label>--%>
            <%--<br>--%>
            <%--<label>--%>
                <%--忘记密码--%>
            <%--</label>--%>
            <%--<label style="float:right">--%>
                <%--<a href="reg.html">我要注册</a>--%>
            <%--</label>--%>
        <%--</div>--%>
            <br>
            <p align="right"><a  href="reg.html">我要注册</a></p>
        <a class="btn btn-lg btn-success btn-block" onclick="dologin()" > 登录</a>
    </form>

<div style="color:#FF6699" align="center"> ${error_login} </div>

</body>
</html>