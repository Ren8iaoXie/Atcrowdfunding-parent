<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
	<style>
		.tree li {
			list-style-type: none;
			cursor:pointer;
		}
		table tbody tr:nth-child(odd){background:#F4F4F4;}
		table tbody td:nth-child(even){color:#C00;}
	</style>

	<script src="${pageContext.request.contextPath}/jquery/jquery.pagination.js"></script>
	<script src="${pageContext.request.contextPath}/css/pagination.css"></script>
	<script src="${pageContext.request.contextPath}/jquery/jquery-2.1.1.min.js"></script>
	<script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
	<script src="${pageContext.request.contextPath}/script/docs.min.js"></script>
	<script src="${pageContext.request.contextPath}/jquery/layer/layer.js"></script>
	<script>
		function delUser(i) {
			alert(i)
		}
	</script>
	<script >
		$(function () {
			$(".list-group-item").click(function(){
				if ( $(this).find("ul") ) {
					$(this).toggleClass("tree-closed");
					if ( $(this).hasClass("tree-closed") ) {
						$("ul", this).hide("fast");
					} else {
						$("ul", this).show("fast");
					}
				}
			});

            getAllUserByPagePyCondition();
            showMenu();
		});
		$("tbody .btn-success").click(function(){
			window.location.href = "assignRole.html";
		});
		$("tbody .btn-primary").click(function(){
			window.location.href = "edit.html";
		});



		function getAllUserByPagePyCondition() {
			$.ajax({
				type:"post",
				url:"${pageContext.request.contextPath}/user/getUserByPageByCondition.do",
				data:{
					"page":1,
					"size":8,
					"loginacct":$("#loginacct").val()
				},
				error:function(){
					alert("error");
				},
				success:function (data) {
					var pageData = data.pageInfo;
					var pageDataList = pageData.list;
					var content = "";
					$.each(pageDataList,function (i, n) {
						content+='<tr>';
						content+='	<td>'+(i+1)+'</td>';
						content+='	<td><input type="checkbox" id="'+n.id+'"></td>';
						content+='	<td>'+(n.loginacct)+'</td>';
						content+='	<td>'+(n.username)+'</td>';
						content+='	<td>'+(n.email)+'</td>';
						content+='	<td>';
						content+='		<button type="button" onclick="window.location.href=\'${pageContext.request.contextPath}/user/assignRole.htm?id='+n.id+'\' " class="btn btn-success btn-xs"><i class=" glyphicon glyphicon-check"></i></button>';
						content+='		<button type="button" onclick="window.location.href=\'${pageContext.request.contextPath}/user/toUpdate.htm?id='+n.id+'\'" class="btn btn-primary btn-xs"><i class=" glyphicon glyphicon-pencil"></i></button>';
						content+='		<button type="button" onclick="deleteUser('+n.id+',\''+n.loginacct+'\')" class="btn btn-danger btn-xs"><i class=" glyphicon glyphicon-remove"></i></button>';
						// content+='		<button type="button" id="btn" class="btn btn-danger btn-xs"><i class=" glyphicon glyphicon-remove"></i></button>';
						content+='	</td>';
						content+='</tr>';
					});




						//导航条
					var pageContent = "";

					if(pageData.pageNum!=1){
						pageContent += '<li><a href="#" onclick="changePageno('+(pageData.pageNum-1)+')">上一页</a></li>';
					}

					for(var i=1 ; i<=pageData.pages ; i++){
						if(i==pageData.pageNum){
							pageContent += '<li class="active"><a href="#" onclick="changePageno('+i+')">'+i+'</a></li>';

						}else{
							pageContent += '<li><a href="#" onclick="changePageno('+i+')">'+i+'</a></li>';

						}
					}

					if(pageData.pageNum!=pageData.pages){
						pageContent += '<li><a href="#" onclick="changePageno('+(pageData.pageNum+1)+')">下一页</a></li>';
					}

					$(".pagination").html(pageContent);
					$("tbody").html(content);
				}

			});
		}

		//改变页码
		function changePageno( pageNum ) {
			var loginacct=$("#loginacct").val();
			$.ajax({

				url : "${pageContext.request.contextPath}/user/getUserByPageByCondition.do",
				type : "post",
				data : {
					"loginacct":loginacct,
					"page":pageNum,
					"size":8
				},
				beforeSend : function(){
					loadingIndex = layer.msg('数据查询中', {icon: 6});
					return true ;
				},
				success : function(result){
					//显示结果
					layer.close(loadingIndex);
					if(result.success){
						//循环遍历,显示数据
						var pageData = result.pageInfo;
						var pageDataList = pageData.list ;
						var content = "";

						$.each(pageDataList,function(i,n){
							content+='<tr>';
							content+='	<td>'+(i+1)+'</td>';
							content+='	<td><input type="checkbox" id="'+n.id+'"></td>';
							content+='	<td>'+(n.loginacct)+'</td>';
							content+='	<td>'+(n.username)+'</td>';
							content+='	<td>'+(n.email)+'</td>';
							content+='	<td>';
							content+="		<button type='button' onclick='window.location.href=\"${pageContext.request.contextPath}/user/assignRole.htm?id="+n.id+"\"' class='btn btn-success btn-xs'>";
							content+="			<i class=' glyphicon glyphicon-check'></i>";
							content+="		</button>";
							content+="		<button type='button' onclick='window.location.href=\"${pageContext.request.contextPath}/user/toUpdate.htm?id="+n.id+"\"' class='btn btn-primary btn-xs'>";
							content+="			<i class=' glyphicon glyphicon-pencil'></i>";
							content+="		</button>";
							content+="		<button type='button' class='btn btn-danger btn-xs' onclick='deleteRole("+n.id+",\""+n.name+"\")'>";
							content+="			<i class=' glyphicon glyphicon-remove'></i>";
							content+="		</button>";
							content+="	</td>";
							content+="</tr>";
							//$("tbody").append(content);
							$("tbody").html(content);
						});

						//拼接导航条
						var pageContent = "";

						if(pageData.pageNum!=1){
							pageContent += '<li><a href="#" onclick="changePageno('+(pageData.pageNum-1)+')">上一页</a></li>';
						}

						for(var i=1 ; i<=pageData.pages ; i++){
							if(i==pageData.pageNum){
								pageContent += '<li class="active"><a href="#" onclick="changePageno('+i+')">'+i+'</a></li>';

							}else{
								pageContent += '<li><a href="#" onclick="changePageno('+i+')">'+i+'</a></li>';

							}
						}

						if(pageData.pageNum!=pageData.pages){
							pageContent += '<li><a href="#" onclick="changePageno('+(pageData.pageNum+1)+')">下一页</a></li>';
						}

						$(".pagination").html(pageContent);

					}else{

						layer.msg("用户分页查询数据失败", {time:1000, icon:5, shift:6});

					}
				},
				error : function(){
					layer.msg("用户分页查询数据错误", {time:1000, icon:5, shift:6});
				}

			});

		}

		function deleteUser(id,loginacct){
			layer.confirm("确认要删除["+loginacct+"]用户吗?",  {icon: 3, title:'提示'}, function(cindex){
				layer.close(cindex);
				$.ajax({
					type : "POST",
					data : {
						"id" : id
					},
					url : "${pageContext.request.contextPath}/user/doDelete.do",
					beforeSend : function() {
						return true ;
					},
					success : function(result){
						if(result.success){
							window.location.href="${pageContext.request.contextPath}/user/index.htm";
						}else{
							layer.msg("删除用户失败", {time:1000, icon:5, shift:6});
						}
					},
					error : function(){
						layer.msg("删除失败", {time:1000, icon:5, shift:6});
					}
				});
			}, function(cindex){
				layer.close(cindex);
			});
		}



	</script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/script/menu.js"></script>


</head>

<body>



<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
	<div class="container-fluid">
		<div class="navbar-header">
			<div><a class="navbar-brand" style="font-size:32px;" href="#">众筹平台 - 用户维护</a></div>
		</div>
		<div id="navbar" class="navbar-collapse collapse">
			<ul class="nav navbar-nav navbar-right">
				<li style="padding-top:8px;">
					<div class="btn-group">
						<button type="button" class="btn btn-default btn-success dropdown-toggle" data-toggle="dropdown">
							<i class="glyphicon glyphicon-user"></i> ${user.username} <span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#"><i class="glyphicon glyphicon-cog"></i> 个人设置</a></li>
							<li><a href="#"><i class="glyphicon glyphicon-comment"></i> 消息</a></li>
							<li class="divider"></li>
							<li><a href="${pageContext.request.contextPath}/user/logout.htm"><i class="glyphicon glyphicon-off"></i> 退出系统</a></li>
						</ul>
					</div>
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
				<jsp:include page="/jsp/common/menu.jsp"></jsp:include>
			</div>
		</div>
		<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
				</div>
				<div class="panel-body">
					<form class="form-inline" role="form" style="float:left;"  method="post"  >
						<div class="form-group has-feedback">
							<div class="input-group">
								<div class="input-group-addon">查询条件</div>
								<input class="form-control has-success" type="text" placeholder="请输入查询条件" name="loginacct" id="loginacct">
							</div>
						</div>
						<button type="button" class="btn btn-warning" onclick="getAllUserByPagePyCondition()"><i class="glyphicon glyphicon-search"></i> 查询</button>
					</form>
					<button type="button" id="deleteBatchBtn" class="btn btn-danger" style="float:right;margin-left:10px;"><i class=" glyphicon glyphicon-remove"></i> 删除</button>
					<button type="button" class="btn btn-primary" style="float:right;" onclick="window.location.href='${pageContext.request.contextPath}/user/toAdd.htm'"><i class="glyphicon glyphicon-plus"></i> 新增</button>
					<br>
					<hr style="clear:both;">
					<div class="table-responsive">
						<table class="table  table-bordered">
							<thead>
							<tr >
								<th width="30">#</th>
								<th width="30"><input id="allCheckbox" type="checkbox"></th>
								<th>账号</th>
								<th>名称</th>
								<th>邮箱地址</th>
								<th width="100">操作</th>
							</tr>
							</thead>
							<tbody id="tbody">

							<%--<c:forEach items="${pageInfo.list}" var="user" varStatus="s">--%>
								<%--<tr>--%>
									<%--<td>${s.count}</td>--%>
									<%--<td><input type="checkbox"></td>--%>
									<%--<td>${user.loginacct }</td>--%>
									<%--<td>${user.username }</td>--%>
									<%--<td>${user.email }</td>--%>
									<%--<td>--%>
										<%--<button type="button" class="btn btn-success btn-xs"><i class=" glyphicon glyphicon-check"></i></button>--%>
										<%--<button type="button" class="btn btn-primary btn-xs"><i class=" glyphicon glyphicon-pencil"></i></button>--%>
										<%--<button type="button" class="btn btn-danger btn-xs"><i class=" glyphicon glyphicon-remove"></i></button>--%>
									<%--</td>--%>
								<%--</tr>--%>
							<%--</c:forEach>--%>

							</tbody>
							<tfoot>
							<tr >
								<td colspan="6" align="center">
									<ul class="pagination">

									</ul>
								</td>
							</tr>

							</tfoot>
						</table>
					</div>
				</div>

			</div>
		</div>
	</div>
</div>

</body>
<script>
	$("#allCheckbox").click(function () {
		var checkboxStatus=this.checked;
		 // $("tbody tr td input[type='checkbox']").attr("checked",checkboxStatus);
		$("tbody tr td input[type='checkbox']").prop("checked",checkboxStatus);

	});

	$("#deleteBatchBtn").click(function () {
		var batch=$("tbody tr td input:checked");

		if(batch.length==0){
            layer.msg("至少选择一名用户！", {time:1000, icon:5, shift:6});
            return false;
        }
		var idStr="";

		$.each(batch,function (i,n) {
			if (i!=0){
				idStr+="&";
			}
			idStr+="id="+n.id;
		})
		layer.confirm("确认要删除这些用户吗?",  {icon: 3, title:'提示'}, function(cindex){
			layer.close(cindex);
			$.ajax({
				type : "POST",
				data :
					 idStr
				,
				url : "${pageContext.request.contextPath}/user/doDeleteBatch.do",
				beforeSend : function() {
					return true ;
				},
				success : function(result){
					if(result.success){
						window.location.href="${pageContext.request.contextPath}/user/toIndex.htm";
					}else{
						layer.msg("删除用户失败", {time:1000, icon:5, shift:6});
					}
				},
				error : function(){
					layer.msg("删除失败", {time:1000, icon:5, shift:6});
				}
			});
		}, function(cindex){
			layer.close(cindex);
		});
	});
</script>
</html>