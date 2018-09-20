<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>请假表单管理</title>
<meta name="decorator" content="ani" />
<script type="text/javascript">

		$(document).ready(function() {
			$("#inputForm").validate({
				submitHandler: function(form){
					jp.loading();
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
			
	        $('#beginDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        $('#endDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
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
							<a class="panelButton" href="${ctx}/test/one/leaveForm"><i
								class="ti-angle-left"></i> 返回</a>
						</h3>
					</div>
					<div class="panel-body">
						<form:form id="inputForm" modelAttribute="leaveForm"
							action="${ctx}/test/one/leaveForm/save" method="post"
							class="form-horizontal">
							<form:hidden path="id" />
							<sys:message content="${message}" />
							<div class="form-group">
								<label class="col-sm-2 control-label"><font color="red">*</font>归属部门：</label>
								<div class="col-sm-10">
									<sys:treeselect id="office" name="office.id"
										value="${leaveForm.office.id}" labelName="office.name"
										labelValue="${leaveForm.office.name}" title="部门"
										url="/sys/office/treeData?type=2"
										cssClass="form-control required" allowClear="true"
										notAllowSelectParent="true" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label"><font color="red">*</font>员工：</label>
								<div class="col-sm-10">
									<sys:userselect id="tuser" name="tuser.id"
										value="${leaveForm.tuser.id}" labelName="tuser.name"
										labelValue="${leaveForm.tuser.name}"
										cssClass="form-control required" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label"><font color="red">*</font>归属区域：</label>
								<div class="col-sm-10">
									<div class=" input-group" style="width: 100%;">
										<form:input path="area" htmlEscape="false" class="required"
											data-toggle="city-picker" style="height: 34px;" />
									</div>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">请假开始日期：</label>
								<div class="col-sm-10">
									<p class="input-group">
									<div class='input-group form_datetime' id='beginDate'>
										<input type='text' name="beginDate" class="form-control"
											value="<fmt:formatDate value="${leaveForm.beginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
										<span class="input-group-addon"> <span
											class="glyphicon glyphicon-calendar"></span>
										</span>
									</div>
									</p>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">请假结束日期：</label>
								<div class="col-sm-10">
									<p class="input-group">
									<div class='input-group form_datetime' id='endDate'>
										<input type='text' name="endDate" class="form-control"
											value="<fmt:formatDate value="${leaveForm.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>" />
										<span class="input-group-addon"> <span
											class="glyphicon glyphicon-calendar"></span>
										</span>
									</div>
									</p>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">备注信息：</label>
								<div class="col-sm-10">
									<form:textarea path="remarks" htmlEscape="false" rows="4"
										class="form-control " />
								</div>
							</div>
							<c:if
								test="${fns:hasPermission('test:one:leaveForm:edit') || isAdd}">
								<div class="col-lg-3"></div>
								<div class="col-lg-6">
									<div class="form-group text-center">
										<div>
											<button class="btn btn-primary btn-block btn-lg btn-parsley"
												data-loading-text="正在提交...">提 交</button>
										</div>
									</div>
								</div>
							</c:if>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>