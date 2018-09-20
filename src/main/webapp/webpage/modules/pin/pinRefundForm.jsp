<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>退款管理管理</title>
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
				<a class="panelButton" href="${ctx}/pin/pinRefund"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="pinRefund" action="${ctx}/pin/pinRefund/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label">团id：</label>
					<div class="col-sm-10">
						<form:input path="groupId" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">会员id：</label>
					<div class="col-sm-10">
						<form:input path="memberId" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">微信订单号：</label>
					<div class="col-sm-10">
						<form:input path="transactionId" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">商户订单号：</label>
					<div class="col-sm-10">
						<form:input path="outTradeNo" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">微信退款单号：</label>
					<div class="col-sm-10">
						<form:input path="refundId" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">商户退款单号：</label>
					<div class="col-sm-10">
						<form:input path="outRefundNo" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">订单金额：</label>
					<div class="col-sm-10">
						<form:input path="totalFee" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">申请退款金额：</label>
					<div class="col-sm-10">
						<form:input path="refundFee" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">应结订单金额：</label>
					<div class="col-sm-10">
						<form:input path="settlementTotalFee" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">退款金额：</label>
					<div class="col-sm-10">
						<form:input path="settlementRefundFee" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">退款状态：</label>
					<div class="col-sm-10">
						<form:input path="refundStatus" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">退款成功时间：</label>
					<div class="col-sm-10">
						<form:input path="successTime" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">退款入账账户：</label>
					<div class="col-sm-10">
						<form:input path="refundRecvAccout" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">退款资金来源：</label>
					<div class="col-sm-10">
						<form:input path="refundAccount" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">退款发起来源：</label>
					<div class="col-sm-10">
						<form:input path="refundRequestSource" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
		<c:if test="${fns:hasPermission('pin:pinRefund:edit') || isAdd}">
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