<%@ page contentType="text/html;charset=UTF-8" %>
<script>
	var $treeTable=null;  
	$(document).ready(function() {
		$treeTable=$('#treeTable').treeTable({  
	    	   theme:'vsStyle',	           
				expandLevel : 2,
				column:0,
				checkbox: false,
	            url:'${ctx}/sys/office/getChildren?parentId=',  
	            callback:function(item) { 
	            	var officeItemTpl= $("#officeItemTpl").html();
					item.typeLabel = jp.getDictLabel(${fns:toJson(fns:getDictList('sys_office_type'))}, item.type);
                    var result = laytpl(officeItemTpl).render(item);
	                return result;
	            },  
	            beforeClick: function($treeTable, id) { 
	                //异步获取数据 这里模拟替换处理  
	                $treeTable.refreshPoint(id);  
	            },  
	            beforeExpand : function($treeTable, id) {   
	            },  
	            afterExpand : function($treeTable, id) {  
	            	//layer.closeAll(); 
	            },  
	            beforeClose : function($treeTable, id) {    
	            	
	            }  
	        });  
	       
	});
	
	function del(con,id){  
		jp.confirm('确认要删除机构吗？', function(){
			jp.loading();
       	  	$.get("${ctx}/sys/office/delete?id="+id, function(data){
       	  		if(data.success){
       	  			$treeTable.del(id);
       	  			jp.success(data.msg);
       	  		}else{
       	  			jp.error(data.msg);
       	  		}
       	  	})
       	   
   		});

	} 
	
	function refresh(){//刷新
		var index = jp.loading("正在加载，请稍等...");
		$treeTable.refresh();
		jp.close(index);
	}
</script>
<script type="text/html" id="officeItemTpl">
<td><a  href="#" onclick="jp.openDialogView('查看机构', '${ctx}/sys/office/form?id={{d.id}}','800px', '600px')">{{d.name}}</a></td>
<td>{{# if(d.area){ }} {{d.area.name}} {{# } }}</td>
<td>{{d.code  === undefined ? "": d.code}}</td>
<td>{{d.typeLabel === undefined ? "": d.typeLabel }}</td>
<td>{{d.remarks === undefined ? "":d.remarks}}</td>
<td>
	<div class="btn-group">
 		<button type="button" class="btn  btn-primary btn-xs dropdown-toggle" data-toggle="dropdown">
			<i class="fa fa-cog"></i>
			<span class="fa fa-chevron-down"></span>
		</button>
	  <ul class="dropdown-menu" role="menu">
		<shiro:hasPermission name="sys:office:view">
			<li><a href="#" onclick="jp.openDialogView('查看机构', '${ctx}/sys/office/form?id={{d.id}}','800px', '600px')"  ><i class="fa fa-search-plus"></i>  查看</a></li>
		</shiro:hasPermission>
		<shiro:hasPermission name="sys:office:edit">
			<li><a href="#" onclick="jp.openDialog('修改机构', '${ctx}/sys/office/form?id={{d.id}}','800px', '600px', $treeTable)" ><i class="fa fa-edit"></i> 修改</a></li>
		</shiro:hasPermission>
		<shiro:hasPermission name="sys:office:del">
			<li><a  onclick="return del(this, '{{d.id}}')"><i class="fa fa-trash"></i> 删除</a></li>
		</shiro:hasPermission>
		<shiro:hasPermission name="sys:office:add">
			<li><a href="#" onclick="jp.openDialog('添加下级机构', '${ctx}/sys/office/form?parent.id={{d.id}}','800px', '600px', $treeTable)"><i class="fa fa-plus"></i> 添加下级机构</a></li>
		</shiro:hasPermission>
	  </ul>
	</div>
</td>
</script>