<%@page import="java.text.SimpleDateFormat"%>
<%@page import="util.DateToChinese"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@page import="java.util.Calendar"%>
<%@page import="com.khnt.utils.DateUtil"%>
<%@page import="java.util.Date"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
	Date lastMonday = DateToChinese.getLastWeekMonday(new Date());
	Date lastSunday = DateToChinese.getLastWeekSunday(new Date());
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String firstDate = sdf.format(lastMonday);
    String curDate  = sdf.format(lastSunday);
%>
    <title></title>
    <%@include file="/k/kui-base-list.jsp" %>
    <%@ taglib uri="http://khnt.com/tags/ui" prefix="u" %>
    <link rel="stylesheet" type="text/css" href="app/finance/css/stylegx.css" media="all" />
    <script type="text/javascript" src="app/finance/js/echarts.min.js"></script>
    <script test="text/javascript">
    var selected = null;
    var grid = null;
    var rows = []; 
    var clsses = [];
    var depts = [];
    $(function () {
        /* $("#btn1").css({"height":"20px","padding-buttom":"10px"});
        $("#btn2").css({"height":"20px","line-height":"18px"});
        $("#btn3").css({"height":"20px","line-height":"18px"});
        $("#btn4").css({"height":"20px","line-height":"18px"}); */
        $("#btn1").ligerButton({
            icon:"count",
            click: function (){
            	getRows();
            },
            text:"统计"
        });
        $("#btn2").ligerButton({
            icon:"excel-export",
            click: function (){
                out();
            },
            text:"导出"
        });
        $("#btn3").ligerButton({
            img:"app/finance/image/qian.png",
            click: function (){
            	if($("#btn3").find('span.l-button-text').html()=='万元'){
            		$("#btn3").find('span.l-button-text').html('元');
            		$("div.l-page-title2-note span").html('万元');
            		loadGrid(rows,clsses,4);
            		drawChart(clsses,rows,4);
            	}else{
            		$("#btn3").find('span.l-button-text').html('万元');
            		$("div.l-page-title2-note span").html('元');
            		loadGrid(rows,clsses,2);
            		drawChart(clsses,rows,2);
            	}
            },
            text:"万元"
        });
        $("#btn4").ligerButton({
        	icon:"piechart",
    		click:function(){
    			var button = $('#btn4');
    			var sp1 = button.find("span.l-button-text");
    			var sp2 = button.find("span.l-button-icon");
    			if(sp1.html()=="表格"){
    				sp2.attr("class","l-button-icon iconfont l-icon-piechart");
    				sp1.html("图表");
    				
    				if($("#btn3").find('span.l-button-text').html()=='万元'){
    					loadGrid(rows,clsses,2);
                	}else{
                		loadGrid(rows,clsses,4);
                	}
    				$("#main").css("display","none");
    				$("#container").css("display","block");
    			}else{
    				sp2.attr("class","l-button-icon iconfont l-icon-table");
    				sp1.html("表格");
    				$("#main").css("display","block");
    				if($("#btn3").find('span.l-button-text').html()=='万元'){
                		drawChart(clsses,rows,2);
                	}else{
                		drawChart(clsses,rows,4);
                	}
    				$("#container").css("display","none");
    			}
    			
    		},
    		text:"图表"
    	});
        $("#form1").ligerForm();
        getRows(true); 
        
        grid = $("#grid").ligerGrid({
	        columns:[{ display: 'id', name: 'CustomerID', align: 'left', width: 120 }], 
	        data:{Rows:[]},
	        height:'100%',
	        usePager:false,
	        width:'100%',
	        allowUnSelectRow:true,
	        onSelectRow: function (data, rowindex, rowobj)
            {
               var dept=data.dept;
               var startDate = $("#startDate").val();
               var endDate = $("#endDate").val();
               //--------------------------------------------------------
              top.$.dialog({
      				width : 900,
      				height : 600,
      				lock : true,
      				title : "部门收入详细列表",
      				data : {
      					"window" : window
      				}, 
      				content : 'url:'+encodeURI("app/finance/statistics/statistics_income_detail.jsp?dept="+dept+"&startDate="+startDate+"&endDate="+endDate)
      			});
            
            }
	     });
    });
   /*  //初始化下拉框
    function initSelect(depts){
    	var items = [];
    	for(var i=0;i<depts.length;i++){
    		var item = {};
    		item.id = depts[i]; 
    		item.text = depts[i]; 
    		items.push(item);
    	}
    	$("#dept").ligerGetComboBoxManager().setData(items);
    	$("#dept").ligerGetComboBoxManager().setValue('');
    } */
    function getRows(isInitSelect){
        var startDate = $("#startDate").val();
        var endDate = $("#endDate").val();
        var unit =null;// $("#unit").ligerGetComboBoxManager().getValue();
      //  var dept = $("#dept").ligerGetTreeManager().getValue();
       // alert(dept)
       var dept = $("#dept").val();
       // alert(dept)
        $.post("feeStatisticsAction/statisticsIncome.do",
        		{"startDate":startDate,"endDate":endDate,"unit":unit,'dept':dept}, 
        		function(res){
        			rows = []; 
		            clsses = [];
		            depts = [];
        			//接收数据
		            var data = res.rows;
		            clsses = res.clsses;
		            depts = res.depts;
		           /*  if(isInitSelect){
			            initSelect(depts);
		            } */
		            //组装数据
		            for(var i=0;i<depts.length;i++){
		            	var row = {};
		            	row['dept'] = depts[i];
		            	row['total'] = 0;
		            	for(var j=0;j<clsses.length;j++){
		            		row[clsses[j]] = 0;
		            		for(var k=0;k<data.length;k++){
		            			if(data[k].DEPT == depts[i] && data[k].CLSS==clsses[j]){
	                      			row[clsses[j]] = data[k].MONEY;
		                      		row['total'] += data[k].MONEY;
		                      	}	
		            		}
		                }
		            	rows.push(row);
		            }
		            
		            if($("#btn3").find('span.l-button-text').html()=='万元'){
		            	 loadGrid(rows,clsses,2);
               		drawChart(clsses,rows,2);
               	}else{
               		 loadGrid(rows,clsses,4);
               		drawChart(clsses,rows,4);
               	}
		            
		            //loadGrid(rows,clsses,2);
		           // drawChart(clsses,rows);
        });
    }
function loadGrid(rows,clsses,fix){
	if(fix == 4){
		rows = tenThousand(rows);
	}
	gird = null;
	//定义头部
    var columns = [];
    columns.push({
    	display : '部门', 
    	name : 'dept',
    	align : 'center', 
    	width : 200,
    	totalSummary:{
				render : function (e){  
    			return "<div>合计</div>"; 
    		} 
        		}
    });
    for(var i = 0;i<clsses.length;i++){
    	 if((selected!=null&&selected[clsses[i]]==true)||selected==null){
	    	columns.push({display: clsses[i], 
	    		name: clsses[i],
	    		align: 'center', 
	    		width: 120, 
	    		totalSummary:{
	                type : 'sum',
	       			render : function (e){  
	       				var tt = e.sum;
	       				tt = parseFloat(tt).toFixed(fix);
	            		return "<div>" + tt +"</div>"; 
	            	} 
		        }
	    	});
    	 }
    }
    columns.push({display: '小计', 
    	name : 'total',
    	align : 'center',
    	width : 120,
    	totalSummary:{
        	type : 'sum',
        	render : function (e) {  
        		var tt = e.sum;
        		tt = parseFloat(tt).toFixed(fix);
        		return "<div>" + tt +"</div>"; 
        	} 
        }
    });	
    //创建表格
    grid.set('columns', columns); 
     grid.loadData({Rows:rows});
// 	grid = $("#grid").ligerGrid({
// 							        columns: columns, 
// 							        data:{Rows:rows},
// 							        height:'100%',
// 							        usePager:false,
// 							        width:'100%'
// 							        onSelectRow: function (data, rowindex, rowobj)
// 					                {
// 					                   var dept=data.dept;
// 					                   var startDate = $("#startDate").val();
// 					                   var endDate = $("#endDate").val();
// 					                   //--------------------------------------------------------
// 					                  top.$.dialog({
// 				              				width : 900,
// 				              				height : 600,
// 				              				lock : true,
// 				              				title : "部门收入详细列表",
// 				              				data : {
// 				              					"window" : window
// 				              				}, 
// 				              				content : 'url:'+encodeURI("app/finance/statistics/statistics_income_detail.jsp?dept="+dept+"&startDate="+startDate+"&endDate="+endDate)
// 				              			});
					                
// 					                }
// 							     });
}
    function out()
    {
        $("body").mask("正在导出数据,请等待...");
        $("#form1").attr("action","feeStatisticsAction/exportDeptIncome.do"); 
        if(selected!=null){
            var item='';
            for (var key in selected) {
    			if(selected[key]==true){
    					if(item==""){
    						item = key;
    					}else{
    						item = item+","+key;
    					}
    			}
    		}
            $("#form1").append('<input type="hidden" name="items" id="items" value="'+item+'"/>');
           }
         $("#form1").submit();
        $("body").unmask();
    };
    function tenThousand(rows){
    	var data = [];
		for(var i=0;i<rows.length;i++){
			var row = rows[i];
			var item = {};
			item.dept=row.dept;
			$.each(row,function(k,v){
				if(typeof v == 'number'){
					item[k]=parseFloat(v/10000).toFixed(4);
				}
			});
			data.push(item);
		}
		return data;
    }
    </script>
</head>
<%-- <%
    String firstDate = DateUtil.getFirstDateStringOfYear("yyyy-MM-dd");
    String curDate  = DateUtil.getDateTime("yyyy-MM-dd", new Date());
%> --%>
<body>
<div id="tccontent">
	<div id="light" class="white_content md-show">
		<div class="close">
			<a id="t-close-btn" class="iconfont icon-esc"
				href="javascript:void(0)" title="关闭">x</a>
		</div>
		<div class="tankuang"></div>
		<div class="wtctbg"></div>
	</div>
	<div id="fade" class="black_overlay" style="display: none;"></div>
</div>
<form name="form1" id="form1" action="" getAction="" target="_blank" method="post">
<div class="item-tm">
    <div class="l-page-title2 has-icon has-note" style="height: 80px">
        <div class="l-page-title2-div"></div>
        <div class="l-page-title2-text"><h1>财务收入统计</h1></div>
        <div class="l-page-title2-note" style="height:20px;">按各开票类型，各部门收费统计（单位：<span>元</span>）</div>
        <div class="l-page-title2-icon">
        	<img src="k/kui/images/icons/32/statistics.png" border="0"/>
        </div>
        <div class="l-page-title-content" style="top:25px;height:80px;"> 
            <table border="0" cellpadding="0" cellspacing="0" width="">
                <tr>
                  <!--   <td width="100" style="text-align: right;width:50px;">单位：</td>
                    <td width="120px">
                       <input id="unit" name="unit"
							type="text" ltype="select" value=""
							ligerui="{isTextBoxMode:true,width:325,
							initValue:'检验院',
							data:[{'id':'检验院','text':'四川省特种设备检验研究院'},{'id':'协会','text':'四川省特种设备检验检测协会'}],
							isMultiSelect:false}" />

                    </td> -->
                    
					<td width="60" style="text-align: right;">部门：</td>
                    <td width="110px">
                       <input id="dept" name="dept"
							type="text" ltype="select" value=""
							ligerui="{width:325,
							initValue:'检验院',
							tree:{checkbox:true,data:<u:dict sql="select o.id, o.parent_id pid, o.id code, o.org_name text
									  from sys_org o
									 where o.id not in ('100042', '100047')
									   and o.parent_id not in ('100042', '100047')
									   and o.status= 'used' and o.property='dep'
									 order by o.orders"/>,
							isMultiSelect:false}}" />

                    </td>
                    <td width="70" style="text-align: right;">时间从：</td>
                    <td width="110">
                            <input id="startDate" name="startDate" type="text" ltype="date" value="<%=firstDate %>"/>
                    </td>
                    <td align="center">&nbsp;至&nbsp;</td>
                    <td width="110">
                        <input id="endDate" name="endDate" type="text" ltype="date" value="<%=curDate %>"/>
                    </td>
                    <td width="" style="text-align: right;float: left;padding-left: 5px;">
                        <div id="btn1"></div>
                        <div id="btn2"></div>
                        <div id="btn3"></div>
                        <div id="btn4"></div>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</div>
</form>
<div id="container" position="center" style="width:99%;height:89%;">
    <div id="grid"></div>   
</div>
<div id="main" style="width:100%;height:100%;display:none;"></div>
<script type="text/javascript">
 	var barChart = null;
 	
 	function drawChart(clsses,rows,fix){
 		$("#main").find("div").remove();
 		var div = document.createElement('div');
 		$("#main").append(div);
 		$(div).css({width:'100%',height:'90%'});
 		barChart = echarts.init(div);
 		if(fix == 4){
 			rows = tenThousand(rows);
 		}
 		var depts = [];
 		var ss = [];
 		for(var i=0;i<clsses.length;i++){
 			var s = {};
 			s.name = clsses[i];
			s.type = 'bar';
			s.stack = 'income';
			s.yAxisIndex = 0;
 			if('一般'==clsses[i]){
	 			//s.yAxisIndex = 0;
	 			s.color='#666699'
 			}else{
 				//s.yAxisIndex = 1;
 				s.color='#993366'
 				
 			}
 			s.data = [];
 			for(var j=0;j<rows.length;j++){
 				if(depts.length<rows.length){
	 				depts.push(rows[j]['dept']);
 				}
 				s.data.push(rows[j][clsses[i]]?rows[j][clsses[i]]:0);
 			}
 			ss.push(s);
 		}
 		//合计
 		/* var tt = [];
 		for(var i=0;i<rows.length;i++){
 			tt.push(rows[i]['total']);
		}
 		clsses[clsses.length]="合计";
 		ss.push({
 			name : '合计',
	 		type :'bar',
			yAxisIndex : 2,
			data : tt
 		});  */
 		var option = {
 				 backgroundColor: '#eee',
 				tooltip : {
 			        trigger: 'axis',
 			        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
 			            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
 			        },
 			       formatter: function (params,ticket,callback) {  
 		                 var res = params[0].axisValue + '<br>';
 		                var tvalue = 0;
 		                 for(var i=0;i<params.length;i++){
 		                	if(params[i].value == 0){
 		                		continue;
 		                	}else{
 		                		res += params[i].marker + params[i].seriesName + ':' + params[i].value +'<br>';
 		                		tvalue += parseFloat(params[i].value);
 		                	}
 		                 }
 		                res += "合计" + ':' + tvalue +'<br>';
 	 		             return res;  
 		             }  
 			    },
 			   toolbox: {
 			        show : true,
 			        feature : {
 			            mark : {show: true},
 			            dataView : {show: true, readOnly: false},
 			            magicType : {
 			                show: true,
 			                type: ['pie', 'funnel']
 			            },
 			            restore : {show: true},
 			            saveAsImage : {show: true}
 			        }
 			    },
 			    legend: {
			        type: 'scroll',
			        orient: 'vertical',
			        left: 20,
			        top: 100,
			        bottom: 20,
			        data: clsses
			    },
 			    grid: {
 			        left: '10%',
 			        right: '11%',
 			        bottom: '6%',
 			        containLabel: true
 			    },
 			    
 			   "xAxis": [
	    	        {
	    	            "type": "category", 
	    	            "splitLine": {
	    	                "show": false
	    	            }, 
	    	            "axisTick": {
	    	                "show": false
	    	            }, 
	    	            "splitArea": {
	    	                "show": false
	    	            }, 
	    	            "axisLabel": {
	    	                "interval": 0, 
	    	                "rotate": 45, 
	    	                "show": true, 
	    	                "splitNumber": 15, 
	    	                "textStyle": {
	    	                    "fontFamily": "微软雅黑", 
	    	                    "fontSize": 12
	    	                }
	    	            }, 
	    	            "data": depts
	    	        }
	    	    ], 
	    	    "yAxis": [
		    	        {
		    	            "type": "value", 
		    	            "splitLine": {
		    	                "show": false
		    	            }, 
		    	            "axisLine": {
		    	                "show": true
		    	            }, 
		    	            "axisTick": {
		    	                "show": false
		    	            }, 
		    	            "splitArea": {
		    	                "show": false
		    	            }
		    	        }
		    	    ], 
 			   /*  yAxis : [
		 			        {
		 			            type : 'value',
		 			            name: '一般',
		 			            position: 'left',
		 			            offset: 80
		 			        }  ,
		 			        {
		 			            type : 'value',
		 			            name: '税票',
		 			            position: 'left'
		 			        },
		 			      {
		 			            type: 'value',
		 			            name: '合计',
		 			            position: 'right',
		 			            offset: 80
		 			        } 
		 			    ], */
 			 /*   dataZoom: [
 			             {
 			                 show: true,
 			                 start: 0,
 			                 end: rows.length>8?60:100,
 			                 top: '95%'
 			             },
 			             {
 			                 type: 'inside',
 			                 start: 0,
 			                 end: rows.length>8?60:100,
 			             }
 			         ], */
 			    series : ss
 		    };

 		barChart.setOption(option);
 		barChart.on('click', function (param) {
 			
 			//弹框加事件
 			$(".white_content").addClass("md-show");
    		$("#light,#tccontent").show();
    		var ligh=$(window).height()*0.8;
    		$(".tankuang").css({"height":ligh+"px"}); 
    		$("#fade").show();
    		//画出饼图
    		for(var i=0;i<rows.length;i++){
 				if(rows[i].dept == param.name){
 					drawPie(rows[i]);
 					break;
 				}
 			}
 		});
 	    //legend点击选中事件
        barChart.on('legendselectchanged', function (param){
	        selected = param["selected"];
        });  
    	$("#fade,#t-close-btn").click(function(){
    		$("#light,#tccontent,#fade").hide();
    	});
 	}
 	var pie = null;
 	function drawPie(row){
 		var data = {};
 		data.legendData = [];
 		data.seriesData = [];
 		data.selected = {};
 		$.each(row,function(k,v){
 			for(var i=0;i<clsses.length;i++){
 				if(v == 0 || k == 'total'){
 					break;
 				}else if(k == 'other'){
 					var obj = {};
 	 				obj.name='其他';
 	 				obj.value=v;
 	 				data.seriesData.push(obj);
 	 				data.legendData.push('其他');
 					data.selected['其他']=true;
 	 				break;
 				}else if(k == clsses[i]){
 	 				var obj = {};
 	 				obj.name=k;
 	 				obj.value=v;
 	 				data.seriesData.push(obj);
 	 				data.legendData.push(clsses[i]);
 					data.selected[clsses[i]]=true;
 	 				break;
 	 			}
 			}
 		});
 		$(".tankuang").find("div").remove();
 		var div = document.createElement('div');
 		$(".tankuang").append(div);
 		$(div).css({
 			width : '100%',
 			height : '100%'
 		});
 		pie = echarts.init(div);
 		var option = {
 			    title : {
 			        text: row.dept,
 			        subtext: '部门收费统计',
 			        x:'center',
 			        textStyle: {
 			            color: '#ccc'
 			        }
 			    },
 			    tooltip : {
 			        trigger: 'item',
 			        formatter: "{a} <br/>{b} : {c} ({d}%)"
 			    },
 			    legend: {
 			        type: 'scroll',
 			        orient: 'vertical',
 			        right: 20,
 			        top: 100,
 			        bottom: 20,
 			        data: data.legendData,
 			        selected: data.selected
 			    },
 			    
 			    series : [
 			        {
 			            name: '开票类型',
 			            type: 'pie',
 			            radius : '55%',
 			            center: ['40%', '50%'],
 			            data: data.seriesData.sort(function (a, b) { return a.value - b.value; }),
 			            itemStyle: {
 			                emphasis: {
 			                    shadowBlur: 10,
 			                    shadowOffsetX: 0,
 			                    shadowColor: 'rgba(0, 0, 0, 0.5)'
 			                }
 			            }
 			        }
 			    ]
 			};
 		pie.setOption(option);

        var startDate = $("#startDate").val();
        var endDate = $("#endDate").val();
 		<!-- 财务详细列表 ------------------------------------>
 		pie.on('click', function (param) {
 			alert(param.name);
 			top.$.dialog({
 				width : 900,
 				height : 600,
 				lock : true,
 				title : "部门收入详细列表",
 				data : {
 					"window" : window
 				}, 
 				content : 'url:'+encodeURI("app/finance/statistics/statistics_income_detailed.jsp?dept="+row.dept+"&clss="+param.name
 				 						+"&startDate="+startDate+"&endDate="+endDate)
 			});
 			
 			
 		});
 	}
</script>
</body>
</html>