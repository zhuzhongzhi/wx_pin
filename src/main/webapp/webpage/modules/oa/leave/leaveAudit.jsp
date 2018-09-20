<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>请假申请</title>
<meta name="decorator" content="ani" />
<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $("#inputForm").submit();
			  return true;
		  }
	
		  return false;
		}
		$(document).ready(function() {
			$("#name").focus();
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
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
<body>
	<div class="wrapper wrapper-content">
		<div class="row">
			<div class="col-md-12">
				<div class="panel panel-primary">
					<div class="panel-heading">
						<h3 class="panel-title">
							<a class="panelButton" href="#" onclick="history.go(-1)"><i
								class="ti-angle-left"></i> 返回</a>
						</h3>
					</div>
					<div class="panel-body">
						当前步骤--[${leave.act.taskName}]
						<form:form id="inputForm" modelAttribute="leave"
							action="${ctx}/oa/leave/saveAudit" method="post"
							class="form-horizontal">
							<form:hidden path="id" />
							<form:hidden path="act.taskId" />
							<form:hidden path="act.taskName" />
							<form:hidden path="act.taskDefKey" />
							<form:hidden path="act.procInsId" />
							<form:hidden path="act.procDefId" />
							<form:hidden id="flag" path="act.flag" />
							<sys:message content="${message}" />
							<div class="form-group">
								<label class="col-sm-2 control-label">请假类型：</label>
								<div class="col-sm-10">
									<form:select path="leaveType" cssClass="form-control input-sm"
										readonly="true">
										<form:options items="${fns:getDictList('oa_leave_type')}"
											itemLabel="label" itemValue="value" htmlEscape="false" />
									</form:select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">开始时间：</label>
								<div class="col-sm-10">
									<input id="startTime" name="startTime" type="text"
										readonly="readonly" maxlength="20" class="form-control"
										value="<fmt:formatDate value="${leave.startTime}" pattern="yyyy-MM-dd"/>" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">结束时间：</label>
								<div class="col-sm-10">
									<input id="endTime" name="endTime" type="text"
										readonly="readonly" maxlength="20" class="form-control"
										value="<fmt:formatDate value="${leave.endTime}" pattern="yyyy-MM-dd"/>" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">请假原因：</label>
								<div class="col-sm-10">
									<form:textarea path="reason" class="form-control" rows="5"
										readonly="true" maxlength="20" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label"><font color="red">*</font>审批意见：</label>
								<div class="col-sm-10">
									<form:textarea path="act.comment" class="form-control required"
										rows="5" maxlength="20" />
								</div>
							</div>

							<div class="form-group">
								<label class="col-sm-2 control-label"></label>
								<div class="col-sm-10">
									<c:if test="${leave.act.taskDefKey ne 'apply_end'}">
										<input id="btnSubmit" class="btn btn-primary" type="submit"
											value="同 意" onclick="$('#flag').val('yes')" />&nbsp;
						<input id="btnSubmit" class="btn btn-inverse" type="submit"
											value="驳 回" onclick="$('#flag').val('no')" />&nbsp;
					</c:if>
								</div>
							</div>

							<act:flowChart procInsId="${leave.act.procInsId}" />
							<act:histoicFlow procInsId="${leave.act.procInsId}" />
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>

