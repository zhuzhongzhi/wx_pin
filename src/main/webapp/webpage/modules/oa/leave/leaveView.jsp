<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>请假申请</title>
<meta name="decorator" content="ani" />
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
						<form:form id="inputForm" modelAttribute="leave"
							action="${ctx}/oa/leave/saveAudit" method="post"
							class="form-horizontal">
							<fieldset>
								<legend>${leave.act.taskName}</legend>
								<div class="form-group">
									<label class="col-sm-2 control-label">请假类型：</label>
									<div class="col-sm-10">
										<form:select path="leaveType" disabled="true"
											cssClass="form-control input-sm">
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
								<act:flowChart procInsId="${leave.act.procInsId}" />
								<act:histoicFlow procInsId="${leave.act.procInsId}" />

							</fieldset>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>


