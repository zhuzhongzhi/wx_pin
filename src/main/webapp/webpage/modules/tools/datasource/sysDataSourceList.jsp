<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>数据源管理</title>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<meta name="decorator" content="ani" />
<%@ include file="/webpage/include/bootstraptable.jsp"%>
<%@include file="/webpage/include/treeview.jsp"%>
<%@include file="sysDataSourceList.js"%>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="panel panel-primary">
			<div class="panel-heading">
				<h3 class="panel-title">数据库连接列表</h3>
			</div>
			<div class="panel-body">
				<sys:message content="${message}" />

				<!-- 搜索 -->
				<div class="accordion-group">
					<div id="collapseTwo" class="accordion-body collapse">
						<div class="accordion-inner">
							<form id="searchForm" class="form form-horizontal well clearfix">
								<div class="col-xs-12 col-sm-6 col-md-4">
									<label class="label-item single-overflow pull-left"
										title="数据库名称：">数据库名称：</label> <input name="name"
										value="${sysDataSource.name}" maxlength="64"
										class=" form-control" />
								</div>
								<div class="col-xs-12 col-sm-6 col-md-4">
									<div style="margin-top: 26px">
										<a id="search"
											class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i
											class="fa fa-search"></i> 查询</a> <a id="reset"
											class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i
											class="fa fa-refresh"></i> 重置</a>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>

				<!-- 工具栏 -->
				<div id="toolbar">
					<shiro:hasPermission name="tools:sysDataSource:add">
						<a id="add" class="btn btn-primary" onclick="add()"><i
							class="glyphicon glyphicon-plus"></i> 新建</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="tools:sysDataSource:edit">
						<button id="edit" class="btn btn-success" disabled
							onclick="edit()">
							<i class="glyphicon glyphicon-edit"></i> 修改
						</button>
					</shiro:hasPermission>
					<shiro:hasPermission name="tools:sysDataSource:del">
						<button id="remove" class="btn btn-danger" disabled
							onclick="deleteAll()">
							<i class="glyphicon glyphicon-remove"></i> 删除
						</button>
					</shiro:hasPermission>
					<a class="accordion-toggle btn btn-default" data-toggle="collapse"
						data-parent="#accordion2" href="#collapseTwo"> <i
						class="fa fa-search"></i> 检索
					</a>
				</div>

				<!-- 表格 -->
				<table id="sysDataSourceTable" data-toolbar="#toolbar"></table>
			</div>
		</div>
	</div>
</body>
</html>