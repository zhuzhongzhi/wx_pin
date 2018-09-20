<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>优惠券管理管理</title>
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
				<a class="panelButton" href="${ctx}/pin/pinWxCoupon"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="pinWxCoupon" action="${ctx}/pin/pinWxCoupon/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label">微信代金券批次号：</label>
					<div class="col-sm-10">
						<form:input path="stockId" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">面值：</label>
					<div class="col-sm-10">
						<form:input path="faceValue" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">用户领券之后，券的生效方式 0 立即生效 1 第二天生效：</label>
					<div class="col-sm-10">
						<form:input path="effectType" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">有效期开始时间：</label>
					<div class="col-sm-10">
						<form:input path="startTime" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">有效期结束时间：</label>
					<div class="col-sm-10">
						<form:input path="endTime" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">使用条件， 满多少元可用。0表示不限制：</label>
					<div class="col-sm-10">
						<form:input path="condition" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">说明：</label>
					<div class="col-sm-10">
						<form:input path="description" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
		<c:if test="${fns:hasPermission('pin:pinWxCoupon:edit') || isAdd}">
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