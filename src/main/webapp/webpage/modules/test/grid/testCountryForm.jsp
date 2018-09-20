<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>国家管理</title>
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
					jp.post("${ctx}/test/grid/testCountry/save",$('#inputForm').serialize(),function(data){
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
	<form:form id="inputForm" modelAttribute="testCountry"
		class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<table class="table table-bordered">
			<tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">国名：</label></td>
					<td class="width-35"><form:input path="name"
							htmlEscape="false" class="form-control " /></td>
					<td class="width-15 active"><label class="pull-right">人口：</label></td>
					<td class="width-35"><form:input path="sum" htmlEscape="false"
							class="form-control " /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">所属洲：</label></td>
					<td class="width-35"><sys:gridselect
							url="${ctx}/test/grid/testContinent/data" id="continent"
							name="continent.id" value="${testCountry.continent.id}"
							labelName="continent.name"
							labelValue="${testCountry.continent.name}" title="选择所属洲"
							cssClass="form-control required" fieldLabels="洲名|备注"
							fieldKeys="name|remarks" searchLabels="洲名|备注"
							searchKeys="name|remarks"></sys:gridselect></td>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35"><form:textarea path="remarks"
							htmlEscape="false" rows="4" class="form-control " /></td>
				</tr>
			</tbody>
		</table>
	</form:form>
</body>
</html>