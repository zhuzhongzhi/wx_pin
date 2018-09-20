<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>商品管理</title>
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
					jp.post("${ctx}/test/manytoone/goods/save",$('#inputForm').serialize(),function(data){
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
	<form:form id="inputForm" modelAttribute="goods"
		class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<table class="table table-bordered">
			<tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>商品名称：</label></td>
					<td class="width-35"><form:input path="name"
							htmlEscape="false" class="form-control required" /></td>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>所属类型：</label></td>
					<td class="width-35"><sys:treeselect id="category"
							name="category.id" value="${goods.category.id}"
							labelName="category.name" labelValue="${goods.category.name}"
							title="所属类型" url="/test/manytoone/category/treeData"
							extId="${goods.id}" cssClass="form-control required"
							allowClear="true" /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>价格：</label></td>
					<td class="width-35"><form:input path="price"
							htmlEscape="false" class="form-control required" /></td>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35"><form:textarea path="remarks"
							htmlEscape="false" rows="4" class="form-control " /></td>
				</tr>
			</tbody>
		</table>
	</form:form>
</body>
</html>