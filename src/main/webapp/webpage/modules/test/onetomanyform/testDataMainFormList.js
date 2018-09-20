<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#testDataMainFormTable').bootstrapTable({
		 
		  //请求方法
               method: 'get',
               //类型json
               dataType: "json",
               //显示刷新按钮
               showRefresh: true,
               //显示切换手机试图按钮
               showToggle: true,
               //显示 内容列下拉框
    	       showColumns: true,
    	       //显示到处按钮
    	       showExport: true,
    	       //显示切换分页按钮
    	       showPaginationSwitch: true,
    	       //显示详情按钮
    	       detailView: true,
    	       	//显示详细内容函数
	           detailFormatter: "detailFormatter",
    	       //最低显示2行
    	       minimumCountColumns: 2,
               //是否显示行间隔色
               striped: true,
               //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）     
               cache: false,    
               //是否显示分页（*）  
               pagination: true,   
                //排序方式 
               sortOrder: "asc",  
               //初始化加载第一页，默认第一页
               pageNumber:1,   
               //每页的记录行数（*）   
               pageSize: 10,  
               //可供选择的每页的行数（*）    
               pageList: [10, 25, 50, 100],
               //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据  
               url: "${ctx}/test/onetomanyform/testDataMainForm/data",
               //默认值为 'limit',传给服务端的参数为：limit, offset, search, sort, order Else
               //queryParamsType:'',   
               ////查询参数,每次调用是会带上这个参数，可自定义                         
               queryParams : function(params) {
               	var searchParam = $("#searchForm").serializeJSON();
               	searchParam.pageNo = params.limit === undefined? "1" :params.offset/params.limit+1;
               	searchParam.pageSize = params.limit === undefined? -1 : params.limit;
               	searchParam.orderBy = params.sort === undefined? "" : params.sort+ " "+  params.order;
                   return searchParam;
               },
               //分页方式：client客户端分页，server服务端分页（*）
               sidePagination: "server",
               contextMenuTrigger:"right",//pc端 按右键弹出菜单
               contextMenuTriggerMobile:"press",//手机端 弹出菜单，click：单击， press：长按。
               contextMenu: '#context-menu',
               onContextMenuItem: function(row, $el){
                   if($el.data("item") == "edit"){
                   	window.location = "${ctx}/test/onetomanyform/testDataMainForm/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该票务代理记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/test/onetomanyform/testDataMainForm/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#testDataMainFormTable').bootstrapTable('refresh');
                   	  			jp.success(data.msg);
                   	  		}else{
                   	  			jp.error(data.msg);
                   	  		}
                   	  	})
                   	   
                   	});
                      
                   } 
               },
              
               onClickRow: function(row, $el){
               },
               columns: [{
		        checkbox: true
		       
		    }
			,{
		        field: 'tuser.name',
		        title: '归属用户',
		        sortable: true
		        ,formatter:function(value, row , index){
		            if(value == null){
		            	return "<a href='${ctx}/test/onetomanyform/testDataMainForm/form?id="+row.id+"'>-</a>";
		            }else{
		            	return "<a href='${ctx}/test/onetomanyform/testDataMainForm/form?id="+row.id+"'>"+value+"</a>";
		            }
		        	
		        }
		       
		    }
			,{
		        field: 'office.name',
		        title: '归属部门',
		        sortable: true
		       
		    }
			,{
		        field: 'area.name',
		        title: '归属区域',
		        sortable: true
		       
		    }
			,{
		        field: 'name',
		        title: '名称',
		        sortable: true
		       
		    }
			,{
		        field: 'sex',
		        title: '性别',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('sex'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'inDate',
		        title: '加入日期',
		        sortable: true
		       
		    }
			,{
		        field: 'remarks',
		        title: '备注信息',
		        sortable: true
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#testDataMainFormTable').bootstrapTable("toggleView");
		}
	  
	  $('#testDataMainFormTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#testDataMainFormTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#testDataMainFormTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/test/onetomanyform/testDataMainForm/import/template';
				  },
			    btn2: function(index, layero){
				        var inputForm =top.$("#importForm");
				        var top_iframe = top.getActiveTab().attr("name");//获取当前active的tab的iframe 
				        inputForm.attr("target",top_iframe);//表单提交成功后，从服务器返回的url在当前tab中展示
				        inputForm.onsubmit = function(){
				        	jp.loading('  正在导入，请稍等...');
				        }
				        inputForm.submit();
					    jp.close(index);
				  },
				 
				  btn3: function(index){ 
					  jp.close(index);
	    	       }
			}); 
		});
		    
	  $("#search").click("click", function() {// 绑定查询按扭
		  $('#testDataMainFormTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		   $("#searchForm  .select-item").html("");
		  $('#testDataMainFormTable').bootstrapTable('refresh');
		});
		
				$('#beginInDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
				$('#endInDate').datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
				});
		
	});
		
  function getIdSelections() {
        return $.map($("#testDataMainFormTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该票务代理记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/test/onetomanyform/testDataMainForm/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#testDataMainFormTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function edit(){
	  window.location = "${ctx}/test/onetomanyform/testDataMainForm/form?id=" + getIdSelections();
  }
  
  
  
  
		   
  function detailFormatter(index, row) {
	  var htmltpl =  $("#testDataMainFormChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/test/onetomanyform/testDataMainForm/detail?id="+row.id, function(testDataMainForm){
    	var testDataMainFormChild1RowIdx = 0, testDataMainFormChild1Tpl = $("#testDataMainFormChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  testDataMainForm.testDataChildList;
		for (var i=0; i<data1.length; i++){
			addRow('#testDataMainFormChild-'+row.id+'-1-List', testDataMainFormChild1RowIdx, testDataMainFormChild1Tpl, data1[i]);
			testDataMainFormChild1RowIdx = testDataMainFormChild1RowIdx + 1;
		}
				
    	var testDataMainFormChild2RowIdx = 0, testDataMainFormChild2Tpl = $("#testDataMainFormChild2Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data2 =  testDataMainForm.testDataChild2List;
		for (var i=0; i<data2.length; i++){
			addRow('#testDataMainFormChild-'+row.id+'-2-List', testDataMainFormChild2RowIdx, testDataMainFormChild2Tpl, data2[i]);
			testDataMainFormChild2RowIdx = testDataMainFormChild2RowIdx + 1;
		}
				
    	var testDataMainFormChild3RowIdx = 0, testDataMainFormChild3Tpl = $("#testDataMainFormChild3Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data3 =  testDataMainForm.testDataChild3List;
		for (var i=0; i<data3.length; i++){
			addRow('#testDataMainFormChild-'+row.id+'-3-List', testDataMainFormChild3RowIdx, testDataMainFormChild3Tpl, data3[i]);
			testDataMainFormChild3RowIdx = testDataMainFormChild3RowIdx + 1;
		}
				
      	  			
      })
     
        return html;
    }
  
	function addRow(list, idx, tpl, row){
		$(list).append(Mustache.render(tpl, {
			idx: idx, delBtn: true, row: row
		}));
	}
			
</script>
<script type="text/template" id="testDataMainFormChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">火车票</a></li>
				<li><a data-toggle="tab" href="#tab-{{idx}}-2" aria-expanded="true">飞机票</a></li>
				<li><a data-toggle="tab" href="#tab-{{idx}}-3" aria-expanded="true">汽车票</a></li>
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
						<table class="ani table">
						<thead>
							<tr>
								<th>出发地</th>
								<th>目的地</th>
								<th>出发时间</th>
								<th>代理价格</th>
								<th>备注信息</th>
							</tr>
						</thead>
						<tbody id="testDataMainFormChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
				<div id="tab-{{idx}}-2" class="tab-pane fade">
					<table class="ani table">
						<thead>
							<tr>
								<th>出发地</th>
								<th>目的地</th>
								<th>出发时间</th>
								<th>代理价格</th>
								<th>备注信息</th>
							</tr>
						</thead>
						<tbody id="testDataMainFormChild-{{idx}}-2-List">
						</tbody>
					</table>
				</div>
				<div id="tab-{{idx}}-3" class="tab-pane fade">
					<table class="ani table">
						<thead>
							<tr>
								<th>出发地</th>
								<th>目的地</th>
								<th>代理价格</th>
								<th>备注信息</th>
							</tr>
						</thead>
						<tbody id="testDataMainFormChild-{{idx}}-3-List">
						</tbody>
					</table>
				</div>
		</div>//-->
	</script>
	<script type="text/template" id="testDataMainFormChild1Tpl">//<!--
				<tr>
					<td>
						{{row.startArea.name}}
					</td>
					<td>
						{{row.endArea.name}}
					</td>
					<td>
						{{row.starttime}}
					</td>
					<td>
						{{row.price}}
					</td>
					<td>
						{{row.remarks}}
					</td>
				</tr>//-->
	</script>
	<script type="text/template" id="testDataMainFormChild2Tpl">//<!--
				<tr>
					<td>
						{{row.startArea.name}}
					</td>
					<td>
						{{row.endArea.name}}
					</td>
					<td>
						{{row.startTime}}
					</td>
					<td>
						{{row.price}}
					</td>
					<td>
						{{row.remarks}}
					</td>
				</tr>//-->
	</script>
	<script type="text/template" id="testDataMainFormChild3Tpl">//<!--
				<tr>
					<td>
						{{row.startArea.name}}
					</td>
					<td>
						{{row.endArea.name}}
					</td>
					<td>
						{{row.price}}
					</td>
					<td>
						{{row.remarks}}
					</td>
				</tr>//-->
	</script>
