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
     <script src="app/statistics/js/echarts.js"></script>
    <script test="text/javascript">
    var allData;
    $(function () {
        $("#btn1").css({"height":"20px","line-height":"18px"});
        $("#btn2").css({"height":"20px","line-height":"18px"});
      	$("#btn3").css({"height":"20px","line-height":"20px"});
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
    				sp2.css('background','url(k/kui/images/16/icons/piechart.png) no-repeat center center');
    				sp1.html("图表");
    				$("#main").css("display","none");
    				$("#container").css("display","block");
    			}else{
    				sp2.css('background','url(k/kui/images/16/icons/table.png) no-repeat center center');
    				sp1.html("表格");
    				$("#main").css("display","block");
    				draw(allData);
    				$("#container").css("display","none");
    			}
    			
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
        $("#org_id").ligerComboBox().setData(tt);
        $("#org_id").ligerGetComboBoxManager().setValue("all");
        init();       
    });
    
    
    function init(){
        var startDate = $("#startDate").val();
        var endDate = $("#endDate").val();
        var org_id = $("#org_id").ligerGetComboBoxManager().findValueByText($("#org_id").val());
        var userName = $("#userName").val();
        if(org_id==""){
            //$.ligerDialog.alert("请先选择部门！");
            //return;
        }
        $.post("cw/fytj/getCwData.do",{"startDate":startDate,"endDate":endDate,"org_id":org_id,"userName":userName}, function(resp){
        	allData = resp.data;
	    	draw(resp.data);
        	inputGrid = $("#countGrid").ligerGrid({
                columns: [
                        { display: '姓名', name: 'userName',align: 'center', width: 150},
                         { display: '所在部门', name: 'depName',align: 'center', width: 250},
                         { display: '差旅补助', name: 'clBz',align: 'center', width: 120,type: 'int'},
                         { display: '培训费', name: 'pxf',align: 'center', width: 120,type: 'int'},
                         { display: '培训补助', name: 'pxBz',align: 'center', width: 120,type: 'int'},
                         { display: '交通费（培训）', name: 'pxJt',align: 'center', width: 120,type: 'int'},
                         { display: '住宿费（培训）', name: 'pxZs',align: 'center', width: 120,type: 'int'},
                         { display: '小计', name: 'taotal',align: 'center', width: 150,type: 'int'}
                ], 
                data:{Rows:eval(JSON.stringify(resp.data))},//json格式的字符串转为对象
                height:'100%',
                usePager:false,
                width:'100%',
                onSelectRow: function (rowdata, rowindex){
                	$("#userName").val(rowdata.userName);
                	$("#org_id").val(rowdata.depName);
                }
             },"json");
        });
    }
    function out()
    {
        /* var org_id = $("input[name='org_id']").ligerGetComboBoxManager().getValue(); */
        var org_id = $("#org_id").ligerGetComboBoxManager().findValueByText($("#org_id").val());
        if(org_id==""){
            $.ligerDialog.alert("请先选择部门！");
            return;
        }
        $("#org_id1").val(org_id);
        $("body").mask("正在导出数据,请等待...");
        /* $("#form1").attr("action","sta/analyse/exportCountByUser.do"); */
        $("#form1").attr("action","cw/fytj/exportCount.do"); 
        $("#form1").submit();
        $("body").unmask();
    };
    </script>
    <style>
    div{margin: 0.5px;}
    .l-button {padding:0 8px 2px 9px;}
    </style>
</head>
<%
    String firstDate = DateUtil.getFirstDateStringOfYear("yyyy-MM-dd");
    String curDate  = DateUtil.getDateTime("yyyy-MM-dd", new Date());
%>
<body style="overflow:scroll;">
<form name="form1" id="form1" action="" getAction="" target="_blank">
<input type="hidden" name="org_id1" id="org_id1"/>
<div class="item-tm">
    <div class="l-page-title2 has-icon has-note" style="height: 80px">
        <div class="l-page-title2-div"></div>
        <div class="l-page-title2-text"><h1>财务报账费用统计</h1></div>
        <div class="l-page-title2-note">以部门、人员为统计对象。</div>
        <div class="l-page-title2-icon"><img src="k/kui/images/icons/32/statistics.png" border="0"/></div>
        <div class="l-page-title-content" style="top:25px;height:80px;"> 
            <table border="0" cellpadding="0" cellspacing="0" width="">
                <tr>
                    <td style="text-align: right;width:50px;">姓名：</td>
                    <td width="140px">
                        <input type="text" name="userName" id="userName" ltype="text"/>
                    </td>
                    <td width="100" style="text-align: right;width:50px;">部门：</td>
                    <td width="150px">
                        <input type="text" name="org_id" id="org_id" ltype="select" ligerui="{
                            readonly:true,
                            tree:{checkbox:false,data: }
                        }"/>
                    </td>
                    <td style="text-align: right;width:120px;">审批通过时间从：</td>
                    <td width="100">
                            <input id="startDate" name="startDate" type="text" ltype="date" value="<%=firstDate %>"/>
                    </td>
                    <td align="center">&nbsp;至&nbsp;</td>
                    <td width="100">
                        <input id="endDate" name="endDate" type="text" ltype="date" value="<%=curDate %>"/>
                    </td>
                    <td colspan="1" align="right">
                        <div id="btn1"></div><div id="btn2"></div><div id="btn3"></div>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</div>
</form>
<div id="container" position="center" style="display:none">
    <div id="countGrid"></div>   
</div>
<div id="main" style="width:100%;height:400px;"></div> 
</body>
</body>
<script>
if (typeof Array.prototype['max'] == 'undefined') { 
	//最小值
	Array.prototype.min = function() {
		var min = this[0];
		var len = this.length;
		for (var i = 1; i < len; i++){ 
			if (this[i] < min){ 
				min = this[i]; 
			} 
		} 
		return min;
	}
	//最大值
	Array.prototype.max = function() { 
		var max = this[0];
		var len = this.length; 
		for (var i = 1; i < len; i++){ 
			if (this[i] > max) { 
				max = this[i]; 
			} 
		} 
		return max;
	}
}
function draw(data){
	  
	 var xdata = [],clBz = [],pxf = [],pxBz = [],pxJt = [],pxZs = [],xj = [];
	 for (var i = 0; i < data.length; i++) {
		 if(data[i].userName=='合计'){
			 continue;
		 }
 		xdata.push(data[i].userName);
 		clBz.push(data[i].clBz);
 		pxf.push(data[i].pxf);
 		pxBz.push(data[i].pxBz);
 		pxJt.push(data[i].pxJt);
 		pxZs.push(data[i].pxZs);
 	}
	var maxClBz = clBz.max()==0?100:clBz.max();
	var maxPxf = pxf.max()==0?100:pxf.max();
	var maxPxBz = pxBz.max()==0?100:pxBz.max();
	var maxPxJt = pxJt.max()==0?100:pxJt.max();
	var maxPxZs = pxZs.max()==0?100:pxZs.max();
	
	var ops = function(){//返回options数据
							var v = [];
							var s,ss;
							for(var j = 0; j < data.length; j++){
								if(data[j].userName=='合计'){
									continue;
								}
								s = {};
								s.title = {};
								s.title.text = data[j].userName+'个人费用统计';
								s.series = [];
								ss={};
								ss.data=[];
								var ssd = {};
								ssd.value=[data[j].clBz,data[j].pxf,data[j].pxBz,data[j].pxJt,data[j].pxZs];
								ssd.name='费用';
								ss.data.push(ssd);
								s.series.push(ss);
								v.push(s);
							}
							
							return v;
						}();
	var myChart = echarts.init(document.getElementById('main'));
	option = {
			baseOption: {
		        timeline: {
		            axisType: 'category',
		            autoPlay: true,
		            playInterval: 1000,
		            data: xdata
		        },
		        /* title: {
		            subtext: '数据来自国家统计局'
		        }, */
		        tooltip: { trigger: 'axis'},
		        legend: {
		            x: 'right',
		            data: ['费用']
		        },
		        calculable : true,
		        grid: {
		            top: 80,
		            bottom: 100
		        } ,
		        radar: [
				        {
				            indicator: [
				                {text: '差旅补助',max:maxClBz},
				                {text: '培训费',max:maxPxf},
				                {text: '培训补助',max:maxPxBz},
				                {text: '交通费（培训）',max:maxPxJt},
				                {text: '住宿费（培训）',max:maxPxZs}
				            ],
				            center: ['50%','50%'],
				            radius: 80
				        }
				    ],
				    series: [
				        {   name:'费用',
				        	 tooltip: {
				                 trigger: 'item'
				             },
				        	itemStyle: {normal: {areaStyle: {type: 'default'}}},
				            type: 'radar'
				        }
				    ]
		    },
		   
		    options:ops
		};
	myChart.setOption(option);
}
</script>
</html>