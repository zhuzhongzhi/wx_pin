<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#pinProductTable').bootstrapTable({
		 
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
               url: "${ctx}/pin/pinProduct/data",
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
                   	window.location = "${ctx}/pin/pinProduct/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该产品信息记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/pin/pinProduct/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#pinProductTable').bootstrapTable('refresh');
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
		        field: 'id',
		        title: 'id',
		        sortable: true
		        ,formatter:function(value, row , index){
		        	return "<a href='${ctx}/pin/pinProduct/form?id="+row.id+"'>"+value+"</a>";
		         }
		       
		    }
			,{
		        field: 'name',
		        title: '产品名称',
		        sortable: true
		       
		    }
			,{
		        field: 'description',
		        title: '产品简介',
		        sortable: true
		       
		    }
			,{
		        field: 'specifications',
		        title: '产品规格，数量，容量等描述',
		        sortable: true
		       
		    }
			,{
		        field: 'mallPrice',
		        title: '商城价',
		        sortable: true
		       
		    }
			,{
		        field: 'minPrice',
		        title: '拼团最低价',
		        sortable: true
		       
		    }
			,{
		        field: 'maxPrice',
		        title: '拼团最高价',
		        sortable: true
		       
		    }
			,{
		        field: 'picUrl',
		        title: '产品图片url',
		        sortable: true
		       
		    }
			,{
		        field: 'createTime',
		        title: '创建时间',
		        sortable: true
		       
		    }
			,{
		        field: 'status',
		        title: '产品状态 0 当前售卖产品 1 其他产品',
		        sortable: true
		       
		    }
			,{
		        field: 'iconUrl',
		        title: '产品图标url',
		        sortable: true
		       
		    }
			,{
		        field: 'detailUrl',
		        title: '产品详情url',
		        sortable: true
		       
		    }
		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		 
		  $('#pinProductTable').bootstrapTable("toggleView");
		}
	  
	  $('#pinProductTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#pinProductTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#pinProductTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/pin/pinProduct/import/template';
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
		  $('#pinProductTable').bootstrapTable('refresh');
		});
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#pinProductTable').bootstrapTable('refresh');
		});
		
		
	});
		
  function getIdSelections() {
        return $.map($("#pinProductTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该产品信息记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/pin/pinProduct/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#pinProductTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  function edit(){
	  window.location = "${ctx}/pin/pinProduct/form?id=" + getIdSelections();
  }
  
</script>