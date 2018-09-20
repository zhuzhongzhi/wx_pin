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
					jp.post("${ctx}/test/one/leaveDialog/save",$('#inputForm').serialize(),function(data){
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
			
	        $('#beginDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#endDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
		});
	</script>
</head>
<body class="bg-white">
	<form:form id="inputForm" modelAttribute="leaveDialog"
		class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<table class="table table-bordered">
			<tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>归属部门：</label></td>
					<td class="width-35"><sys:treeselect id="office"
							name="office.id" value="${leaveDialog.office.id}"
							labelName="office.name" labelValue="${leaveDialog.office.name}"
							title="部门" url="/sys/office/treeData?type=2"
							cssClass="form-control required" allowClear="true"
							notAllowSelectParent="true" /></td>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>员工：</label></td>
					<td class="width-35"><sys:userselect id="tuser"
							name="tuser.id" value="${leaveDialog.tuser.id}"
							labelName="tuser.name" labelValue="${leaveDialog.tuser.name}"
							cssClass="form-control required" /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>归属区域：</label></td>
					<td class="width-35">
						<div class=" input-group" style="width: 100%;">
							<form:input path="area" htmlEscape="false" class="required"
								data-toggle="city-picker" style="height: 34px;" />
						</div>
					</td>
					<td class="width-15 active"><label class="pull-right">请假开始日期：</label></td>
					<td class="width-35">
						<p class="input-group">
						<div class='input-group form_datetime' id='beginDate'>
							<input type='text' name="beginDate" class="form-control"
								value="<fmt:formatDate value="${leaveDialog.beginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
							<span class="input-group-addon"> <span
								class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
						</p>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">请假结束日期：</label></td>
					<td class="width-35">
						<p class="input-group">
						<div class='input-group form_datetime' id='endDate'>
							<input type='text' name="endDate" class="form-control"
								value="<fmt:formatDate value="${leaveDialog.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
							<span class="input-group-addon"> <span
								class="glyphicon glyphicon-calendar"></span>
							</span>
						</div>
						</p>
					</td>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35"><form:textarea path="remarks"
							htmlEscape="false" rows="4" class="form-control " /></td>
				</tr>
			</tbody>
		</table>
	</form:form>
</body>
</html>