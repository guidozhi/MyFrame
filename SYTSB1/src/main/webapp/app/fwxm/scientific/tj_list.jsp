<%@ page contentType="text/html;charset=UTF-8" %>
<%@page import="java.util.Calendar"%>
<%@page import="com.khnt.utils.DateUtil"%>
<%@page import="java.util.Date"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title></title>
    <%@include file="/k/kui-base-list.jsp" %>
    <%@ taglib uri="http://khnt.com/tags/ui" prefix="u" %>
      <script src="app/fwxm/scientific/js/echarts.js"></script>
    <script src="app/statistics/js/china.js"></script>
    <script test="text/javascript">
 
    $(function () {
        /* $("#btn1").css({"height":"20px","line-height":"18px"})
        $("#btn2").css({"height":"20px","line-height":"18px"})
         $("#btn3").css({"height":"20px","line-height":"20px"}); */
      
        $("#btn1").ligerButton({
            icon:"count",
            click: function (){
                init();
            },text:"统计"
        });
        
        $("#btn2").ligerButton({
            icon:"excel-export",
            click: function (){
                out();
            },text:"导出"
        });
        $("#btn3").ligerButton({
        	icon:"table",
    		click:function(){
    			var button = $('#btn3');
    			var sp1 = button.find("span.l-button-text");
    			var sp2 = button.find("span.l-button-icon");
    			//var span2 = $(el).next("span");
    			if(sp1.html()=="表格"){
    				sp2.attr("class","l-button-icon iconfont l-icon-piechart");
    				sp1.html("图表");
    				$("#main").css("display","none");
    				$("#container").css("display","block");
    			}else{
    				sp2.attr("class","l-button-icon iconfont l-icon-table");
    				sp1.html("表格");
    				$("#main").css("display","block");
    				draw("1");
    				$("#container").css("display","none");
    			}
    			init();
    		},
    		text:"表格"
    	});
        $("#form1").ligerForm();    
        
        var data = <u:dict sql="select t.id code,t.org_name text from SYS_ORG t order by t.orders"/>;
        var tt = new Array();
        tt.push({id:'all',text:'全部'});
        for(var i in data){
           tt.push(data[i]);
        }
        //$("#org_id").ligerComboBox().setData(tt);
       // $("#org_id").ligerGetComboBoxManager().setValue("all");
        init();       
    });
    
    
    function init(){
        var startDate = $("#startDate").val();
        var endDate = $("#endDate").val();
       // var org_id = $("#org_id").ligerGetComboBoxManager().findValueByText($("#org_id").val());
        var org_id=$("#org_id2").val();
        var userName = $("#userName").val();
        if(org_id==""){
            //$.ligerDialog.alert("请先选择部门！");
            //return;
        }
        $.post("tj/getKyData.do",{"startDate":startDate,"endDate":endDate,"org_id":org_id,"userName":userName}, function(resp){
        	draw("1");
        	inputGrid = $("#countGrid").ligerGrid({
                columns: [
                    
                         { display: '项目名称', name: 'xmmc',align: 'center', width: 210},
                         { display: '项目费用报销', name: 'fybx',align: 'center', width: 120,type: 'int'},
                         { display: '项目差旅费', name: 'clf',align: 'center', width: 120,type: 'int'},
                         { display: '项目总费用', name: 'money',align: 'center', width: 120,type: 'int'},
                         { display: '论文数量', name: 'lwts',align: 'center', width: 120,type: 'int'},
                         { display: '专利数量', name: 'zlgs',align: 'center', width: 120,type: 'int'}
                ], 
                data:{Rows:eval(JSON.stringify('[{"xmmc":电梯制停距离测试仪,"fybx":1000.0,"clf":2000.0,"money":3000.0,"lwts":5.0,"zlgs":10.0}]'))},//json格式的字符串转为对象
                height:'100%',
                usePager:false,
                width:'100%',
                onSelectRow: function (rowdata, rowindex){
                	//$("#unitName").val(rowdata.unitId);
                	$("#unitName").ligerGetComboBoxManager().setValue(rowdata.unitId);
                	  $("#org_id2").val(rowdata.depId);
                	$("#org_id").val(rowdata.depName);
                },
                onDblClickRow:function (rowdata, rowindex){
                	  var startDate = $("#startDate").val();
                      var endDate = $("#endDate").val();
                    
                      //var org_id = $("#org_id").ligerGetComboBoxManager().findValueByText($("#org_id").val());
                      var org_id=rowdata.depId;
                      //var org_id=$("#org_id").val();
                      var unitName = $("#unitName").ligerGetComboBoxManager().findValueByText($("#unitName").val());
                	 top.$.dialog({
        		         width: 1000,
        		         height: 500,
        		         lock: true,
        		         parent: api,
        		         data: {
        		       	 window: window
        		         },
        		         title: "详情",
        		         content: 'url:app/finance/cw_bmfytj_list.jsp?pageStatus=detail&startDate='+startDate+"&endDate="+endDate+"&org_id="+org_id+"&unitName="+unitName
        	          });
                }
             },"json");
        });
    }
    function out()
    {
        /* var org_id = $("input[name='org_id']").ligerGetComboBoxManager().getValue(); */
        /* var org_id = $("#org_id").ligerGetComboBoxManager().findValueByText($("#org_id").val()); */
        var org_id=$("#org_id2").val();
        if(org_id==""){
            $.ligerDialog.alert("请先选择部门！");
            return;
        }
        $("#org_id1").val(org_id);
        $("body").mask("正在导出数据,请等待...");
        /* $("#form1").attr("action","sta/analyse/exportCountByUser.do"); */
        $("#form1").attr("action","tj/exportCount.do"); 
        $("#form1").submit();
        $("body").unmask();
    };
    function selectOrg(){
    	 top.$.dialog({
	         width: 350,
	         height: 400,
	         lock: true,
	         parent: api,
	         data: {
	       	 window: window
	         },
	         title: "部门",
	         content: 'url:app/finance/cw_bmfytj_org.jsp'
    	  });
    }
    function clickNodeId(unitId,unitName){
    	$("#org_id").val(unitName);
    	$("#org_id2").val(unitId);
    }
    </script>
</head>
<%
    String firstDate = DateUtil.getFirstDateStringOfYear("yyyy-MM-dd");
    String curDate  = DateUtil.getDateTime("yyyy-MM-dd", new Date());
%>
<body>
<form name="form1" id="form1" action="" getAction="" target="_blank">
<input type="hidden" name="org_id1" id="org_id1"/>
<input type="hidden" name="org_id2" id="org_id2"/>
<div class="item-tm">
    <div class="l-page-title2 has-icon has-note" style="height: 80px">
        <div class="l-page-title2-div"></div>
        <div class="l-page-title2-text"><h1>科研项目统计</h1></div>
        <div class="l-page-title2-note">以项目名称为统计对象。</div>
        <div class="l-page-title2-icon"><img src="k/kui/images/icons/32/statistics.png" border="0"/></div>
        <div class="l-page-title-content" style="top:25px;height:80px;"> 
            <table border="0" cellpadding="0" cellspacing="0" width="">
                <tr>
                    <%-- <td style="text-align: right;width:50px;">单位：</td>
                    <td width="140px">
                        <u:combo attribute="initValue:''"   name="unitName" code="TJY2_UNIT" />
                       <input text="text" name="unitName" id ="unitName" 
							ltype="select" ligerui="{initValue:'',data:<u:dict code='TJY2_UNIT' />}" />
                    </td> --%>
                    <td width="80" style="text-align: right;width:60px;">项目名称：</td>
                    <td width="110px">
                        <input type="text" name="org_id" id="org_id" ltype="text" onclick="selectOrg()" ligerui="{
                            readonly:true,
                            tree:{checkbox:false,data: }
                        }"/>
                    </td>
                    <td width="90" style="text-align: right;">开始时间从：</td>
                    <td width="110">
                            <input id="startDate" name="startDate" type="text" ltype="date" value="<%=firstDate %>"/>
                    </td>
                    <td align="center">&nbsp;至&nbsp;</td>
                    <td width="110">
                        <input id="endDate" name="endDate" type="text" ltype="date" value="<%=curDate %>"/>
                    </td>
                    <td width="" style="text-align: right;float: left;padding-left: 5px;">
                        <div id="btn1"></div><div id="btn2"></div><div id="btn3"></div>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</div>
</form>
<div id="container"  position="center" style="display:none">
    <div id="countGrid"></div>   
</div>
<div id="main"  style="width:100%;height:400px;"></div> 
<script>
// 路径配置
function draw(data){
    				
    				var clf = ['10000','100000','200000','5000','7000','4549'];
    				var fybx =['20000','200000','300000','6000','8999','2345'];
    				var xj = ['30000','300000','500000','9900','6999','6789'];
    				var myChart = echarts.init(document.getElementById('main'));
    				 // 添加点击事件  
    			  
    			    myChart.on("click", eConsole);    
    				//x轴 label
    				var xData = function() {
    				    var xdata = ['电梯制停距离测试仪','电梯井道数据综合测试仪','急冷水过滤器塑性变形分析','基于移动端的特检知识分答系统','永磁同步电梯退磁现象研究','基于微信小程序的会议室管理系统'];
    				    /* for (var i = 0; i < data.length; i++) {
    				    		xdata.push(data[i].depName);
    				    		clbz.push(data[i].clBz);
    				    		clf.push(data[i].clf);
    				    		pxf.push(data[i].pxfy);
    				    		fybx.push(data[i].fybx);
    				    		lk.push(data[i].draw);
    				    		xj.push(data[i].taotal);
    				    } */
    				    return xdata;
    				}();
    				var colors = ['#5793f3', '#d14a61', '#675bba'];

    				option = {"tooltip": {
    								        "trigger": "axis",
    								        "axisPointer": {
    								            "type": "shadow",
    								            textStyle: {
    								                color: "#fff"
    								            }
    					
    								        }
    						   	 		 },
    					     "grid": {
    						    	 "borderWidth": 0,
    							     "top": 150,
    							     "bottom": 95,
    						         "textStyle": {
    						            color: "#fff"
    						         }
    					     },
    				     toolbox: {
    				        feature: {
    				            /* dataView: {show: true, readOnly: false},
    				            restore: {show: true}, */
    				            saveAsImage: {show: true}
    				        }
    				    }, 
    				    legend: {
    				    	 	x: '4%',
    					        top: '11%',
    					        textStyle: {
    					            color: '#90979c',
    					        },
    				        data:['差旅费','费用报销','小计']
    				    },
    				    color: colors,
    				    xAxis: [
    				        {
    				            type: 'category',
    				            axisTick: {
    				                alignWithLabel: true
    				            },
    				            data: xData
    				        }
    				    ],
    				    yAxis: [
    				        {
    				            type: 'value',
    				            name: '费用', 
    				            position: 'left',
    				            axisLine: {
    				                lineStyle: {
    				                    color: colors[0]
    				                }
    				            },
    				            axisLabel: {
    				                formatter: '{value} ￥'
    				            }
    				        }
    				    ],
    				    dataZoom: [
    					                {
    					                    show: true,
    					                    start: 20,
    					                    end: 50
    					                },
    					                {
    					                    type: 'inside',
    					                    start: 54,
    					                    end: 100
    					                }
    					            ],
    				    series: [
    				       
    				        {
    				        	clickable : true,
    				            name:'差旅费',
    				            type:'bar',
    				            stack:'cl',
    				            data:clf
    				        },
    				        {
    				        	clickable : true,
    				            name:'费用报销',
    				            type:'bar',
    				            stack:'fy',
    				            data:fybx
    				            
    				        },
    				        {
    				        	clickable : true,
    				            name:'小计',
    				            type:'line',
    				            data:xj
    				        }
    				    ]
    				};
    				
    				myChart.setOption(option);
    			}
function eConsole(param) {    
	 top.$.dialog({
         width: 600,
         height: 400,
         lock: true,
         parent: api,
         data: {
       	 window: window
         },
         title: "统计详情",
         content: 'url:app/fwxm/scientific/tj_detail.jsp?name='+param.name
      });
}
</script>
</body>

</html>