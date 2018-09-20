<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>群组管理</title>
<meta name="decorator" content="ani" />
<script type="text/javascript">
        var validateForm;
        var $refreshGroup; // 父页面table表格id
        var $topIndex;//弹出窗口的 index
        function doSubmit(refreshFn, index){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
            if(validateForm.form()){
                $refreshGroup = refreshFn;
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
                    jp.post("${ctx}/iim/layGroup/save",$('#inputForm').serialize(),function(data){
                        if(data.success){
                            $refreshGroup();
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
	    function searchFriend(layGroupId){
	    	jp.open({
			    type: 2, 
			    area: ['900px', '560px'],
			    title:"选择用户",
			    auto:true,
		        maxmin: true, //开启最大化最小化按钮
			    content: ctx+"/sys/user/userSelect?isMultiSelect=true",
			    btn: ['确定', '关闭'],
			    yes: function(index, layero){
			    	var rows = layero.find("iframe")[0].contentWindow.getSelections();
					if(rows.length ==0){
						jp.warning("请选择至少一个用户!");
						return;
					}
			    	
					for(var i=0; i<rows.length; i++){
						alert(rows[i].photo);
						alert(rows[i].name);
					}
					
			    	
			    	top.layer.close(index);
				  },
				  cancel: function(index){ 
					  //取消默认为空，如需要请自行扩展。
					  top.layer.close(index);
	   	         }
			}); 
		};
		


	</script>
</head>
<body class="bg-white">
	<form:form id="inputForm" modelAttribute="layGroup" method="post"
		class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<table
			class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
			<tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>群组名：</label></td>
					<td class="width-35"><form:input path="groupname"
							htmlEscape="false" maxlength="64" class="form-control required" />
					</td>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>群头像：</label></td>
					<td class="width-35"><form:hidden id="avatar" path="avatar"
							htmlEscape="false" maxlength="256" class="form-control required" />
						<sys:ckfinder input="avatar" type="files"
							uploadPath="/iim/layGroup" selectMultiple="false" /></td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font
							color="red">*</font>简介：</label></td>
					<td class="width-35"><form:textarea path="remarks"
							htmlEscape="false" rows="4" maxlength="255"
							class="form-control required" /></td>
					<td class="width-15 active"></td>
					<td class="width-35">
						<div class="text-center">
					</td>
				</tr>
			</tbody>
		</table>
	</form:form>
</body>
</html>