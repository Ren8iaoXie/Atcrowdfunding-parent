<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" isELIgnored="false" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/font-awesome.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/doc.min.css">
    <style>
        .tree li {
            list-style-type: none;
            cursor: pointer;
        }
    </style>

    <script src="${pageContext.request.contextPath}/jquery/jquery-2.1.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/script/docs.min.js"></script>
    <script type="text/javascript">

        $(function () {
            $(".list-group-item").click(function () {
                if ($(this).find("ul")) {
                    $(this).toggleClass("tree-closed");
                    if ($(this).hasClass("tree-closed")) {
                        $("ul", this).hide("fast");
                    } else {
                        $("ul", this).show("fast");
                    }
                }
            });
        });
    </script>

    <script>
        function add() {
            $.ajax({
                type:"post",
                url:"${pageContext.request.contextPath}/permission/doAdd.do",
                data:$("#from").serialize(),
                success:function (data) {
                    if (data.success) {
                        alert(data.message);
                        location.href="${pageContext.request.contextPath}/permission/index.htm"
                    }
                }
            })
        }

    </script>

</head>

<body>

<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <div><a class="navbar-brand" style="font-size:32px;" href="user.html">众筹平台 - 用户维护</a></div>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav navbar-right">
                <li style="padding-top:8px;">
                    <%@include file="/jsp/common/userinfo.jsp" %>
                </li>
                <li style="margin-left:10px;padding-top:8px;">
                    <button type="button" class="btn btn-default btn-danger">
                        <span class="glyphicon glyphicon-question-sign"></span> 帮助
                    </button>
                </li>
            </ul>
            <form class="navbar-form navbar-right">
                <input type="text" class="form-control" placeholder="Search...">
            </form>
        </div>
    </div>
</nav>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">
            <div class="tree">
                <%@include file="/jsp/common/menu.jsp" %>
            </div>
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <ol class="breadcrumb">
                <li><a href="#">首页</a></li>
                <li><a href="#">数据列表</a></li>
                <li class="active">新增</li>
            </ol>
            <div class="panel panel-default">
                <div class="panel-heading">表单数据
                    <div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal"><i
                            class="glyphicon glyphicon-question-sign"></i></div>
                </div>
                <div class="panel-body">




                    <form  id="from">
                        <input type="hidden" class="form-control" id="pid" name="pid" value="${param.id}" >

                        <div class="form-group">
                            <label for="name">许可名称</label>
                            <input type="text" class="form-control" id="name" name="name" placeholder="请输入许可名称">
                        </div>
                        <div class="form-group">
                            <label for="icon">许可图标</label>
                            <select name="icon">
                                <option value="glyphicon glyphicon-th-list">glyphicon glyphicon-th-list</option>
                                <option value="glyphicon glyphicon-dashboard">glyphicon glyphicon-dashboard</option>
                                <option value="glyphicon glyphicon glyphicon-tasks">glyphicon glyphicon glyphicon-tasks</option>
                                <option value="glyphicon glyphicon-user">glyphicon glyphicon-user</option>
                                <option value="glyphicon glyphicon-king">glyphicon glyphicon-king</option>
                                <option value="glyphicon glyphicon-lock">glyphicon glyphicon-lock</option>
                                <option value="glyphicon glyphicon-ok">glyphicon glyphicon-ok</option>
                                <option value="glyphicon glyphicon-check">glyphicon glyphicon-check</option>
                                <option value="glyphicon glyphicon-th-large">glyphicon glyphicon-th-large</option>
                                <option value="glyphicon glyphicon-picture">glyphicon glyphicon-picture</option>
                                <option value="glyphicon glyphicon-equalizer">glyphicon glyphicon-equalizer</option>
                                <option value="glyphicon glyphicon-random">glyphicon glyphicon-random</option>
                                <option value="glyphicon glyphicon-hdd">glyphicon glyphicon-hdd</option>
                                <option value="glyphicon glyphicon-comment">glyphicon glyphicon-comment</option>
                                <option value="glyphicon glyphicon-list">glyphicon glyphicon-list</option>
                                <option value="glyphicon glyphicon-tags">glyphicon glyphicon-tags</option>
                                <option value="glyphicon glyphicon-list-alt">glyphicon glyphicon-list-alt</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="url">许可URL</label>
                            <input type="text" class="form-control" id="url" name="url" placeholder="请输入许可URL">
                            <p class="help-block label label-warning">请输入许可URL</p>
                        </div>

                        <button type="button"  class="btn btn-success" onclick="add()"><i class="glyphicon glyphicon-plus"></i> 新增 </button>
                        <button id="resetBtn" type="button" class="btn btn-danger"><i class="glyphicon glyphicon-refresh"></i> 重置  </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
                        class="sr-only">Close</span></button>
                <h4 class="modal-title" id="myModalLabel">帮助</h4>
            </div>
            <div class="modal-body">
                <div class="bs-callout bs-callout-info">
                    <h4>测试标题1</h4>
                    <p>测试内容1，测试内容1，测试内容1，测试内容1，测试内容1，测试内容1</p>
                </div>
                <div class="bs-callout bs-callout-info">
                    <h4>测试标题2</h4>
                    <p>测试内容2，测试内容2，测试内容2，测试内容2，测试内容2，测试内容2</p>
                </div>
            </div>
            <!--
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
              <button type="button" class="btn btn-primary">Save changes</button>
            </div>
            -->
        </div>
    </div>
</div>

</body>
<script>
    $("#resetBtn").click(function () {
        $("#from")[0].reset();
    });
</script>
</html>