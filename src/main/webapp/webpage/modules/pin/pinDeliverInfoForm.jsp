<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>递送信息管理</title>
	<meta name="decorator" content="ani"/>
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
				<a class="panelButton" href="${ctx}/pin/pinDeliverInfo"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="pinDeliverInfo" action="${ctx}/pin/pinDeliverInfo/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label">团id：</label>
					<div class="col-sm-10">
						<form:input path="groupId" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">物流编码：</label>
					<div class="col-sm-10">
						<form:input path="logisticsCodes" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">创建时间：</label>
					<div class="col-sm-10">
						<form:input path="createTime" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">递送方式</label>
					<div class="col-sm-10">
						<form:input path="deliverType" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">省：</label>
					<div class="col-sm-10">
						<form:input path="province" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">市：</label>
					<div class="col-sm-10">
						<form:input path="city" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">区：</label>
					<div class="col-sm-10">
						<form:input path="district" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">详细地址：</label>
					<div class="col-sm-10">
						<form:input path="address" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">接收人名字：</label>
					<div class="col-sm-10">
						<form:input path="recvName" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">递送状态 0 进行中 1 已经送达：</label>
					<div class="col-sm-10">
						<form:input path="status" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">接收人手机：</label>
					<div class="col-sm-10">
						<form:input path="recvPhone" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">会员id：</label>
					<div class="col-sm-10">
						<form:input path="memberId" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">团状态</label>
					<div class="col-sm-10">
						<form:input path="groupStatus" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
		<c:if test="${fns:hasPermission('pin:pinDeliverInfo:edit') || isAdd}">
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                     <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">提 交</button>
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