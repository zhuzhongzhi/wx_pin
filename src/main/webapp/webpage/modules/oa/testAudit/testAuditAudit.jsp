<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>审批管理</title>
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
							<h3>${testAudit.act.taskName}</h3>
						</div>
						<form:form id="inputForm" modelAttribute="testAudit"
							action="${ctx}/oa/testAudit/saveAudit" method="post"
							class="form-horizontal">
							<form:hidden path="id" />
							<form:hidden path="act.taskId" />
							<form:hidden path="act.taskName" />
							<form:hidden path="act.taskDefKey" />
							<form:hidden path="act.procInsId" />
							<form:hidden path="act.procDefId" />
							<form:hidden id="flag" path="act.flag" />
							<sys:message content="${message}" />
							<table id="contentTable"
								class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
								<tr>
									<td class="tit">姓名</td>
									<td>${testAudit.user.name}</td>
									<td class="tit">部门</td>
									<td>${testAudit.office.name}</td>
									<td class="tit">岗位职级</td>
									<td>${testAudit.post}</td>
								</tr>
								<tr>
									<td class="tit">调整原因</td>
									<td colspan="5">${testAudit.content}</td>
								</tr>
								<tr>
									<td class="tit" rowspan="3">调整原因</td>
									<td class="tit">薪酬档级</td>
									<td>${testAudit.olda}</td>
									<td class="tit" rowspan="3">拟调整标准</td>
									<td class="tit">薪酬档级</td>
									<td>${testAudit.newa}</td>
								</tr>
								<tr>
									<td class="tit">月工资额</td>
									<td>${testAudit.oldb}</td>
									<td class="tit">月工资额</td>
									<td>${testAudit.newb}</td>
								</tr>
								<tr>
									<td class="tit">年薪金额</td>
									<td>${testAudit.oldc}</td>
									<td class="tit">年薪金额</td>
									<td>${testAudit.newc}</td>
								</tr>
								<tr>
									<td class="tit">月增资</td>
									<td colspan="2">${testAudit.addNum}</td>
									<td class="tit">执行时间</td>
									<td colspan="2">${testAudit.exeDate}</td>
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
								<tr>
									<td class="tit">您的意见</td>
									<td colspan="5"><form:textarea path="act.comment"
											class="form-control required" rows="5" maxlength="20" /></td>
								</tr>
							</table>
							<div class="form-group">

								<div class="col-sm-3"></div>
								<div class="col-sm-6">
									<div class="form-group text-center">
										<c:if test="${testAudit.act.taskDefKey eq 'apply_end'}">
											<input id="btnSubmit"
												class="btn  btn-primary btn-block btn-lg btn-parsley"
												type="submit" value="兑 现" onclick="$('#flag').val('yes')" />&nbsp;
										</c:if>
										<c:if test="${testAudit.act.taskDefKey ne 'apply_end'}">
											<input id="btnSubmit"
												class="btn  btn-primary btn-lg btn-parsley" type="submit"
												value="同 意" onclick="$('#flag').val('yes')" />&nbsp;
											<input id="btnSubmit"
												class="btn  btn-danger btn-lg btn-parsley" type="submit"
												value="驳 回" onclick="$('#flag').val('no')" />&nbsp;
										</c:if>
									</div>
								</div>
							</div>
							<act:flowChart procInsId="${testAudit.act.procInsId}" />
							<act:histoicFlow procInsId="${testAudit.act.procInsId}" />
						</form:form>

					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
