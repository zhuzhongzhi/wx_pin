<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>群组管理</title>
<meta name="decorator" content="ani" />
<link href="${ctxStatic}/plugin/layui/css/manager.css" type="text/css"
	rel="stylesheet" />
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
			validateForm = $("#inputForm").validate({
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
            refreshMembers();
		});




        function refreshMembers() {
            jp.get("${ctx}/iim/layGroup/memberData?id=${layGroup.id}", function (data) {
                debugger
                var memberTpl = $('#memberTpl').html(); //读取模版
                laytpl(memberTpl).render(data, function(render){
                    $("#group-members-view").html(render);
                });

            })
        }
        function addMemberToGroup(){
            jp.openUserSelectDialog(true,function (ids) {
                jp.get("${ctx}/iim/layGroup/addUser?ids="+ids+"&groupid=${layGroup.id}", function (result) {
                    if(result.success){
                        refreshMembers()
                        jp.success(result.msg);
                    }
                })
            });
        };
        function delFromGroup(id){
            jp.get("${ctx}/iim/layGroup/logout?user.id="+id+"&group.id=${layGroup.id}",function (data) {
				if(data.success){
                    refreshMembers()
				    jp.success(data.msg);
				}
            })
        }


	</script>
</head>
<body class="bg-white">

	<div class="wrapper wrapper-content animated fadeInRight">
		<div class="row">
			<div id="group-members-view"></div>
		</div>
	</div>

	<script id="memberTpl" type="text/html">
			{{# for(var i = 0, len = d.length; i < len; i++){ }}
			<div class="col-xs-2">
				<div class="contact-box">
					<a href="#">
						<div class="text-center photo">
							<img alt="image" class="img-circle m-t-xs img-responsive" src="{{# if(d[i].user.photo != ''){ }}{{  d[i].user.photo }}{{# }else{ }}${ctxStatic}/common/images/flat-avatar.png {{# } }}" />
							<h5>{{  d[i].user.name }}</h5>
						</div>
					</a>
				</div>
			</div>
			{{# } }}
		</script>

</body>
</html>