<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>模型管理</title>
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
                    jp.post("${ctx}/act/model/create",$('#inputForm').serialize(),function(data){
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
	<form:form id="inputForm" class="form-horizontal">
		<sys:message content="${message}" />
		<table class="table table-bordered">
			<tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">流程分类：</label></td>
					<td class="width-35"><select id="category" name="category"
						class="required form-control ">
							<c:forEach items="${fns:getDictList('act_category')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
					</select></td>
					<td class="width-15 active"><label class="pull-right">模型标识：</label></td>
					<td class="width-35"><input id="key" name="key" type="text"
						class="form-control required" /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">模型名称：</label></td>
					<td class="width-35"><input id="name" name="name" type="text"
						class="form-control required" /></td>
					<td class="width-15 active"><label class="pull-right">模块描述：</label></td>
					<td class="width-35"><textarea id="description"
							name="description" class="form-control required"></textarea></td>
				</tr>
			</tbody>
		</table>
	</form:form>
</body>
</html>
