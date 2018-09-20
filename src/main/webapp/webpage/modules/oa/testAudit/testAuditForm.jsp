<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>薪酬调整申请</title>
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
						<div class="form-group text-center">
							<h3>薪酬调整申请</h3>
						</div>
						<form:form id="inputForm" modelAttribute="testAudit"
							action="${ctx}/oa/testAudit/save" method="post"
							class="form-horizontal">
							<form:hidden path="id" />
							<form:hidden path="act.taskId" />
							<form:hidden path="act.taskName" />
							<form:hidden path="act.taskDefKey" />
							<form:hidden path="act.procInsId" />
							<form:hidden path="act.procDefId" />
							<form:hidden id="flag" path="act.flag" />
							<sys:message content="${message}" />
							<table
								class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
								<tr>
									<td class="tit">姓名</td>
									<td><sys:userselect id="user" name="user.id"
											value="${testAudit.user.id}" labelName="user.name"
											labelValue="${testAudit.user.name}" isMultiSelected="false"
											cssClass="form-control required" /></td>
									<td class="tit">部门</td>
									<td><sys:treeselect id="office" name="office.id"
											value="${testAudit.office.id}" labelName="office.name"
											labelValue="${testAudit.office.name}" title="用户"
											url="/sys/office/treeData?type=2"
											cssClass="form-control required" allowClear="true"
											notAllowSelectParent="true" /></td>
									<td class="tit">岗位职级</td>
									<td><form:input path="post" htmlEscape="false"
											class="form-control " maxlength="50" /></td>
								</tr>
								<tr>
									<td class="tit">调整原因</td>
									<td colspan="5"><form:textarea path="content" rows="5"
											maxlength="200" class="form-control required" /></td>
								</tr>
								<tr>
									<td class="tit" rowspan="3">调整原因</td>
									<td class="tit" width="15%">薪酬档级</td>
									<td><form:input path="olda" class="form-control"
											htmlEscape="false" maxlength="50" /></td>
									<td class="tit" width="15%" rowspan="3">拟调整标准</td>
									<td class="tit" width="15%">薪酬档级</td>
									<td><form:input path="newa" class="form-control"
											htmlEscape="false" maxlength="50" /></td>
								</tr>
								<tr>
									<td class="tit">月工资额</td>
									<td><form:input path="oldb" class="form-control"
											htmlEscape="false" maxlength="50" /></td>
									<td class="tit">月工资额</td>
									<td><form:input path="newb" class="form-control"
											htmlEscape="false" maxlength="50" /></td>
								</tr>
								<tr>
									<td class="tit">年薪金额</td>
									<td><form:input path="oldc" class="form-control"
											htmlEscape="false" maxlength="50" /></td>
									<td class="tit">年薪金额</td>
									<td><form:input path="newc" class="form-control"
											htmlEscape="false" maxlength="50" /></td>
								</tr>
								<tr>
									<td class="tit">月增资</td>
									<td colspan="2"><form:input path="addNum"
											class="form-control" htmlEscape="false" maxlength="50" /></td>
									<td class="tit">执行时间</td>
									<td colspan="2"><form:input path="exeDate"
											class="form-control" htmlEscape="false" maxlength="50" /></td>
								</tr>
								<tr>
									<td class="tit">人力资源部意见</td>
									<td colspan="5">${testAudit.hrText}</td>
								</tr>
								<tr>
									<td class="tit">分管领导意见</td>
									<td colspan="5">${testAudit.leadText}</td>
								</tr>
								<tr>
									<td class="tit">集团主要领导意见</td>
									<td colspan="5">${testAudit.mainLeadText}</td>
								</tr>
							</table>
							<div class="form-group">
								<div class="col-sm-3"></div>
								<div class="col-sm-6">
									<div class="form-group text-center">
										<input id="btnSubmit"
											class="btn  btn-primary btn-lg btn-parsley" type="submit"
											value="提交申请" onclick="$('#flag').val('yes')" />&nbsp;
										<c:if test="${not empty testAudit.id}">
											<input id="btnSubmit2"
												class="btn  btn-danger btn-lg btn-parsley" type="submit"
												value="销毁申请" onclick="$('#flag').val('no')" />&nbsp;
							</c:if>
									</div>
								</div>
							</div>

							<c:if test="${not empty testAudit.id}">
								<act:flowChart procInsId="${testAudit.act.procInsId}" />
								<act:histoicFlow procInsId="${testAudit.act.procInsId}" />
							</c:if>
						</form:form>
					</div>
				</div>
			</div>
</body>
</html>

