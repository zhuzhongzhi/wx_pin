<%@ page contentType="text/html;charset=UTF-8" %>
<script>
	    var $testTreeDialogTreeTable=null;  
		$(document).ready(function() {
			$testTreeDialogTreeTable=$('#testTreeDialogTreeTable').treeTable({  
		    	   theme:'vsStyle',	           
					expandLevel : 2,
					column:0,
					checkbox: false,
		            url:'${ctx}/test/tree/testTreeDialog/getChildren?parentId=',  
		            callback:function(item) { 
		            	 var treeTableTpl= $("#testTreeDialogTreeTableTpl").html();

		            	 var result = laytpl(treeTableTpl).render({
								  row: item
							});
		                return result;                   
		            },  
		            beforeClick: function($testTreeDialogTreeTable, id) { 
		                //异步获取数据 这里模拟替换处理  
		                $testTreeDialogTreeTable.refreshPoint(id);  
		            },  
		            beforeExpand : function($testTreeDialogTreeTable, id) {   
		            },  
		            afterExpand : function($testTreeDialogTreeTable, id) {  
		            },  
		            beforeClose : function($testTreeDialogTreeTable, id) {    
		            	
		            }  
		        });
		        
		        $testTreeDialogTreeTable.initParents('${parentIds}', "0");//在保存编辑时定位展开当前节点
		       
		});
		
		function del(con,id){  
			jp.confirm('确认要删除机构吗？', function(){
				jp.loading();
	       	  	$.get("${ctx}/test/tree/testTreeDialog/delete?id="+id, function(data){
	       	  		if(data.success){
	       	  			$testTreeDialogTreeTable.del(id);
	       	  			jp.success(data.msg);
	       	  		}else{
	       	  			jp.error(data.msg);
	       	  		}
	       	  	})
	       	   
       		});
	
		} 
		
		function refresh(){//刷新
			var index = jp.loading("正在加载，请稍等...");
			$testTreeDialogTreeTable.refresh();
			jp.close(index);
		}
</script>
<script type="text/html" id="testTreeDialogTreeTableTpl">
			<td><a  href="#" onclick="jp.openDialogView('查看机构', '${ctx}/test/tree/testTreeDialog/form?id={{d.row.id}}','800px', '500px')">
				{{d.row.name === undefined ? "": d.row.name}}
			</a></td>
			<td>
				{{d.row.remarks === undefined ? "": d.row.remarks}}
			</td>
			<td>
				<div class="btn-group">
			 		<button type="button" class="btn  btn-primary btn-xs dropdown-toggle" data-toggle="dropdown">
						<i class="fa fa-cog"></i>
						<span class="fa fa-chevron-down"></span>
					</button>
				  <ul class="dropdown-menu" role="menu">
					<shiro:hasPermission name="test:tree:testTreeDialog:view">
						<li><a href="#" onclick="jp.openDialogView('查看机构', '${ctx}/test/tree/testTreeDialog/form?id={{d.row.id}}','800px', '500px')"><i class="fa fa-search-plus"></i> 查看</a></li>
					</shiro:hasPermission>
					<shiro:hasPermission name="test:tree:testTreeDialog:edit">
						<li><a href="#" onclick="jp.openDialog('修改机构', '${ctx}/test/tree/testTreeDialog/form?id={{d.row.id}}','800px', '500px', $testTreeDialogTreeTable)"><i class="fa fa-edit"></i> 修改</a></li>
		   			</shiro:hasPermission>
		   			<shiro:hasPermission name="test:tree:testTreeDialog:del">
		   				<li><a  onclick="return del(this, '{{d.row.id}}')"><i class="fa fa-trash"></i> 删除</a></li>
					</shiro:hasPermission>
		   			<shiro:hasPermission name="test:tree:testTreeDialog:add">
						<li><a href="#" onclick="jp.openDialog('添加下级机构', '${ctx}/test/tree/testTreeDialog/form?parent.id={{d.row.id}}','800px', '500px', $testTreeDialogTreeTable)"><i class="fa fa-plus"></i> 添加下级机构</a></li>
					</shiro:hasPermission>
				  </ul>
				</div>
			</td>
	</script>