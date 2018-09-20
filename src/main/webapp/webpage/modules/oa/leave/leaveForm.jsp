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
					jp.loading('正在提交，请稍等...');
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


            $('#startTime').datetimepicker({
                format: "YYYY-MM-DD HH:mm:ss"
            });
            $('#endTime').datetimepicker({
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
							<a class="panelButton" href="#" onclick="history.go(-1)"><i
								class="ti-angle-left"></i> 返回</a>
						</h3>
					</div>
					<div class="panel-body">
						<form:form id="inputForm" modelAttribute="leave"
							action="${ctx}/oa/leave/save" method="post"
							class="form-horizontal">
							<form:hidden path="id" />
							<sys:message content="${message}" />
							<div class="form-group">
								<label class="col-sm-2 control-label">请假类型：</label>
								<div class="col-sm-10">
									<form:select path="leaveType" cssClass="form-control input-sm">
										<form:options items="${fns:getDictList('oa_leave_type')}"
											itemLabel="label" itemValue="value" htmlEscape="false" />
									</form:select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label"><font color="red">*</font>开始时间：</label>
								<div class="col-sm-10">
									<p class="input-group">
									<div class='input-group form_datetime' id='startTime'>
										<input type='text' name="startTime"
											class="form-control required" pattern="yyyy-MM-dd HH:mm:ss" />
										<span class="input-group-addon"> <span
											class="glyphicon glyphicon-calendar"></span>
										</span>
									</div>
									</p>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label"><font color="red">*</font>结束时间：</label>
								<div class="col-sm-10">
									<p class="input-group">
									<div class='input-group form_datetime' id='endTime'>
										<input type='text' name="endTime"
											class="form-control required" pattern="yyyy-MM-dd HH:mm:ss" />
										<span class="input-group-addon"> <span
											class="glyphicon glyphicon-calendar"></span>
										</span>
									</div>
									</p>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label"><font color="red">*</font>请假原因：</label>
								<div class="col-sm-10">
									<form:textarea path="reason" class="form-control required"
										rows="5" maxlength="20" />
								</div>
							</div>
							<div class="col-sm-3"></div>
							<div class="col-sm-6">
								<div class="form-group text-center">
									<label></label>

									<div>
										<button type="submit"
											class="btn btn-primary btn-block btn-lg btn-parsley"
											data-loading-text="正在提交...">提 交</button>
									</div>
								</div>
							</div>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>

