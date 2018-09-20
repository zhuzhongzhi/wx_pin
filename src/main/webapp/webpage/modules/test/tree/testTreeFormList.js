<%@ page contentType="text/html;charset=UTF-8" %>
<script>
	    var testTreeFormTreeTable=null;  
		$(document).ready(function() {
			testTreeFormTreeTable=$('#testTreeFormTreeTable').treeTable({  
		    	   theme:'vsStyle',	           
					expandLevel : 2,
					column:0,
					checkbox: false,
		            url:'${ctx}/test/tree/testTreeForm/getChildren?parentId=',  
		            callback:function(item) { 
		            	 var treeTableTpl= $("#testTreeFormTreeTableTpl").html();

		            	 var result = laytpl(treeTableTpl).render({
								  row: item
							});
		                return result;                   
		            },  
		            beforeClick: function(testTreeFormTreeTable, id) { 
		                //异步获取数据 这里模拟替换处理  
		                testTreeFormTreeTable.refreshPoint(id);  
		            },  
		            beforeExpand : function(testTreeFormTreeTable, id) {   
		            },  
		            afterExpand : function(testTreeFormTreeTable, id) {  
		            },  
		            beforeClose : function(testTreeFormTreeTable, id) {    
		            	
		            }  
		        });
		        
		        testTreeFormTreeTable.initParents('${parentIds}', "0");//在保存编辑时定位展开当前节点
		       
		});
		
		function del(con,id){  
			jp.confirm('确认要删除区域吗？', function(){
				jp.loading();
	       	  	$.get("${ctx}/test/tree/testTreeForm/delete?id="+id, function(data){
	       	  		if(data.success){
	       	  			testTreeFormTreeTable.del(id);
	       	  			jp.success(data.msg);
	       	  		}else{
	       	  			jp.error(data.msg);
	       	  		}
	       	  	})
	       	   
       		});
	
		} 
		
		function refresh(){//刷新
			var index = jp.loading("正在加载，请稍等...");
			testTreeFormTreeTable.refresh();
			jp.close(index);
		}
</script>
<script type="text/html" id="testTreeFormTreeTableTpl">
			<td><a  href="${ctx}/test/tree/testTreeForm/form?id={{d.row.id}}">
				{{d.row.name}}
			</a></td>
			<td>
				{{d.row.remarks}}
			</td>
			<td>
				<div class="btn-group">
			 		<button type="button" class="btn  btn-primary btn-xs dropdown-toggle" data-toggle="dropdown">
						<i class="fa fa-cog"></i>
						<span class="fa fa-chevron-down"></span>
					</button>
				  <ul class="dropdown-menu" role="menu">
					<shiro:hasPermission name="test:tree:testTreeForm:view">
						<li><a href="${ctx}/test/tree/testTreeForm/form?id={{d.row.id}}"><i class="fa fa-search-plus"></i> 查看</a></li>
					</shiro:hasPermission>
					<shiro:hasPermission name="test:tree:testTreeForm:edit">
		   				<li><a href="${ctx}/test/tree/testTreeForm/form?id={{d.row.id}}"><i class="fa fa-edit"></i> 修改</a></li>
		   			</shiro:hasPermission>
		   			<shiro:hasPermission name="test:tree:testTreeForm:del">
		   				<li><a  onclick="return del(this, '{{d.row.id}}')"><i class="fa fa-trash"></i> 删除</a></li>
					</shiro:hasPermission>
		   			<shiro:hasPermission name="test:tree:testTreeForm:add">
						<li><a href="${ctx}/test/tree/testTreeForm/form?parent.id={{d.row.id}}"><i class="fa fa-plus"></i> 添加下级机构</a></li>
					</shiro:hasPermission>
				  </ul>
				</div>
			</td>
	</script>