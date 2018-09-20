<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>请假表单管理</title>
<meta name="decorator" content="ani" />
<script type="text/javascript">
		var validateForm;
		var $table; // 父页面table表格id
		var $topIndex;//弹出窗口的 index
		function doSubmit(table, index){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $table = table;
			  $topIndex = index;
			  jp.loading();
			  $("#inputForm").submit();
			  return true;
		  }

		  return false;
		}

		$(document).ready(function() {
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					jp.post("${ctx}/tools/sysDataSource/save",$('#inputForm').serialize(),function(data){
						if(data.success){
	                    	$table.bootstrapTable('refresh');
	                    	jp.success(data.msg);
	                    	jp.close($topIndex);//关闭dialog

	                    }else{
            	  			jp.error(data.msg);
	                    }
					})
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			
		});
	</script>
</head>
<body class="bg-white">
	<form:form id="inputForm" modelAttribute="sysDataSource"
		class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<table class="table table-bordered">
			<tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">连接名称：</label></td>
					<td class="width-35"><form:input path="name"
							htmlEscape="false" class="form-control " /></td>
					<td class="width-15 active"><label class="pull-right">连接英文名：</label></td>
					<td class="width-35"><form:input path="enname"
							htmlEscape="false" class="form-control " /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">数据库用户名：</label></td>
					<td class="width-35"><form:input path="dbUserName"
							htmlEscape="false" class="form-control " /></td>
					<td class="width-15 active"><label class="pull-right">数据库密码：</label></td>
					<td class="width-35"><form:input path="dbPassword"
							htmlEscape="false" class="form-control " /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">数据库链接：</label></td>
					<td class="width-35"><form:input path="dbUrl"
							htmlEscape="false" class="form-control " /></td>
					<td class="width-15 active"><label class="pull-right">数据库驱动类：</label></td>
					<td class="width-35"><form:select path="dbDriver"
							class="form-control ">
							<form:option value="" label="" />
							<form:options items="${fns:getDictList('db_driver')}"
								itemLabel="label" itemValue="value" htmlEscape="false" />
						</form:select></td>
				</tr>
			</tbody>
		</table>
	</form:form>
</body>
</html>