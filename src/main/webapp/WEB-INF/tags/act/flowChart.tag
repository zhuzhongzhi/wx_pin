<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ attribute name="procInsId" type="java.lang.String" required="true"
	description="流程实例ID"%>
<%@ attribute name="startAct" type="java.lang.String" required="false"
	description="开始活动节点名称"%>
<%@ attribute name="endAct" type="java.lang.String" required="false"
	description="结束活动节点名称"%>
<fieldset>
	<legend>流转信息</legend>
	<div id="flowChart">正在加载流程信息...</div>
</fieldset>
<script type="text/javascript">
	$.get("${ctx}/act/task/flowChart?procInsId=${procInsId}&startAct=${startAct}&endAct=${endAct}&t="+new Date().getTime(), function(data){
		$("#flowChart").html(data);
	});
</script>