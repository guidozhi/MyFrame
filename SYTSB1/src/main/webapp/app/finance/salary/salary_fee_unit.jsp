<%@ page contentType="text/html;charset=UTF-8" %>
<%@page import="java.util.Calendar"%>
<%@page import="com.khnt.utils.DateUtil"%>
<%@page import="java.util.Date"%>

<%@ taglib uri="http://khnt.com/tags/ui" prefix="u" %>
<%
    String firstDate = DateUtil.getFirstDateStringOfYear("yyyy-MM-dd");
    String curDate  = DateUtil.getDateTime("yyyy-MM-dd", new Date());
    String[] type={"基本工资岗位工资","基本工资薪级工资",
            "基本工资保留补贴","基本工资粮贴","基本工资补发项",
            "基本工资独子","基本工资小计","绩效工资基础绩效每月","绩效工资基础绩效补发",
            "绩效工资季度绩效","绩效工资补贴驻场补贴","绩效工资补贴其他",
            "绩效工资小计","扣款项职业年金每月","扣款项职业年金补扣",
            "扣款项医疗保险每月","扣款项医疗保险补发","扣款项失业每月","扣款项失业补发",
            "扣款项公积金每月","扣款项公积金补发","扣款项工会经费","扣款项所得税","扣款项养老保险每月","扣款项养老保险补发","扣款项小计","应发合计"};
	
    String gzSql = "select t1.COLUMN_NAME, t3.COMMENTS "
    +"from user_tab_columns t1, user_tab_comments t2, user_col_comments t3 "
    +"where t1.TABLE_NAME = t2.TABLE_NAME "
    +"and t1.COLUMN_NAME = t3.COLUMN_NAME "
    +"and t1.TABLE_NAME = t3.TABLE_NAME "
    +"and t1.TABLE_NAME ='TJY2_CW_SALARY_NEW' "
    +"and t1.COLUMN_NAME not in ('IMPORT_ID','CREATER_ID','CREATER_NAME','ID','NAME') "
    +"order by t1.COLUMN_ID";
    
    String gzColum = "[{id:'JBGZ_GWGZ',text:'基本工资岗位工资'},{id:'JBGZ_XJGZ',text:'基本工资薪级工资'},"
            +" {id:'JBGZ_BLBT',text:'基本工资保留补贴'},{id:'JBGZ_LT',text:'基本工资粮贴'},"
            +"{id:'JBGZ_BFX',text:'基本工资补发项'},{id:'JBGZ_XJ',text:'基本工资小计'},"
            +" {id:'JXGZ_JCJX_MY',text:'绩效工资基础绩效每月'},{id:'JXGZ_JCJX_BF',text:'绩效工资基础绩效补发'},"
            +" {id:'JXGZ_JDJX',text:'绩效工资季度绩效'},{id:'JXGZ_BT_ZCBT',text:'绩效工资补贴驻场补贴'},"
            +"{id:'JXGZ_BT_QT',text:'绩效工资补贴其他'},{id:'JXGZ_BY',text:'绩效工资备用'},"
            +" {id:'KKX_ZYNJ_MY',text:'扣款项职业年金每月'},{id:'KKX_ZYNJ_BK',text:'扣款项职业年金补扣'},"
            +" {id:'KKX_YLBX_MY',text:'扣款项医疗保险每月'},{id:'KKX_YLBX_BF',text:'扣款项医疗保险补发'},"
            +" {id:'KKX_SY_MY',text:'扣款项失业每月'},{id:'KKX_SY_BF',text:'扣款项失业补发'},"
            +"{id:'KKX_GJJ_MY',text:'扣款项公积金每月'},{id:'KKX_GJJ_BF',text:'扣款项公积金补发'},"
            +" {id:'KKX_GHJF',text:'扣款项工会经费'},{id:'KKX_SDS',text:'扣款项所得税'},"
            +" {id:'JXGZ_XJ',text:'绩效工资小计'},{id:'JBGZ_DZ',text:'基本工资独子'},{id:'KKX_BY',text:'扣款项备用'},"
            +" {id:'KKX_XJ',text:'扣款项小计'},{id:'FSALARY',text:'应发合计'},{id:'KKX_OLDBX_MY',text:'扣款项养老保险每月'},"
            +" {id:'KKX_OLDBX_BF',text:'扣款项养老保险补发'}]";
    
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title></title>
    <%@ include file="/k/kui-base-list.jsp" %>
    <%@ taglib uri="http://khnt.com/tags/ui" prefix="u" %>
    <link rel="stylesheet" type="text/css" href="app/finance/css/stylegx.css" media="all" />
    <script type="text/javascript" src="app/finance/js/echarts.min.js"></script>
    <script test="text/javascript">
    var type=['基本工资岗位工资','基本工资薪级工资','基本工资保留补贴','基本工资粮贴','基本工资补发项','基本工资独子','基本工资小计','绩效工资基础绩效每月','绩效工资基础绩效补发','绩效工资季度绩效','绩效工资补贴驻场补贴','绩效工资补贴其他','绩效工资小计','扣款项职业年金每月','扣款项职业年金补扣','扣款项医疗保险每月','扣款项医疗保险补发','扣款项失业每月','扣款项失业补发','扣款项公积金每月','扣款项公积金补发','扣款项工会经费','扣款项所得税','扣款项养老保险每月','扣款项养老保险补发','扣款项小计','应发合计']
	var types=['JBGZ_GWGZ','JBGZ_XJGZ','JBGZ_BLBT','JBGZ_LT','JBGZ_BFX','JBGZ_DZ','JBGZ_XJ','JXGZ_JCJX_MY','JXGZ_JCJX_BF','JXGZ_JDJX','JXGZ_BT_ZCBT','JXGZ_BT_QT','JXGZ_XJ','KKX_ZYNJ_MY','KKX_ZYNJ_BK','KKX_YLBX_MY','KKX_YLBX_BF','KKX_SY_MY','KKX_SY_BF','KKX_GJJ_MY','KKX_GJJ_BF','KKX_GHJF','KKX_SDS','KKX_OLDBX_MY','KKX_OLDBX_BF','KKX_XJ','FSALARY']
	
    var selected =  null;
    var grid = null;
     rows = []; 
     names=[]
    var clsses = [];
    var depts = [];
    $(function () {
        /* $("#btn1").css({"height":"20px","line-height":"18px"});
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
    				$("#main").css("display","none");
    				$("#container").css("display","block");
    				if($("#btn3").find('span.l-button-text').html()=='万元'){
    					loadGrid(rows,2);
                	}else{
                		loadGrid(rows,4);
                	}
    			}else{
    				sp2.attr("class","l-button-icon iconfont l-icon-table");
    				sp1.html("表格");
    				$("#main").css("display","block");
    				$("#container").css("display","none");
    				if($("#btn3").find('span.l-button-text').html()=='万元'){
                		drawChart(clsses,rows,2);
                	}else{
                		drawChart(clsses,rows,4);
                	}
    			}
    			
    		},
    		text:"图表"
    	});
        $("#form1").ligerForm();
       // $("#dept").ligerGetTreeManager().setValue("100020");
        getRows(true); 
    });
  //将数据转成“万元”
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
  //保留小数点后n位
    function point(rows,num){
    	var data = [];
		for(var i=0;i<rows.length;i++){
			var row = rows[i];
			var item = {};
			item.NAME=row.NAME;
			$.each(row,function(k,v){
				if(typeof v == 'number'){
					item[k]=parseFloat(v).toFixed(num);
				}
			});
			data.push(item);
		}
		return data;
    }
  //获取数据
    function getRows(isInitSelect){
        var startDate = $("#startDate").val();
        var endDate = $("#endDate").val();
        var unit = "检验院";//$("#unit").ligerGetComboBoxManager().getValue();
        var dept = $("#dept").val();// $("#dept").ligerGetTreeManager().getValue();
        var gzItem = $("#gzItem").ligerGetComboBoxManager().getValue();
        var typess = $("#gzItem").val();
        if(gzItem!=null&&gzItem!=""){
        	types = gzItem.split(",");
        	//types[types.length] = "FSALARY";
        	type = typess.split(",");
        	//type[type.length] = "合计";
        }else{
        	 type=['基本工资岗位工资','基本工资薪级工资','基本工资保留补贴','基本工资粮贴','基本工资补发项','基本工资独子','基本工资小计','绩效工资基础绩效每月','绩效工资基础绩效补发','绩效工资季度绩效','绩效工资补贴驻场补贴','绩效工资补贴其他','绩效工资小计','扣款项职业年金每月','扣款项职业年金补扣','扣款项医疗保险每月','扣款项医疗保险补发','扣款项失业每月','扣款项失业补发','扣款项公积金每月','扣款项公积金补发','扣款项工会经费','扣款项所得税','扣款项养老保险每月','扣款项养老保险补发','扣款项小计','应发合计']
        	 types=['JBGZ_GWGZ','JBGZ_XJGZ','JBGZ_BLBT','JBGZ_LT','JBGZ_BFX','JBGZ_DZ','JBGZ_XJ','JXGZ_JCJX_MY','JXGZ_JCJX_BF','JXGZ_JDJX','JXGZ_BT_ZCBT','JXGZ_BT_QT','JXGZ_XJ','KKX_ZYNJ_MY','KKX_ZYNJ_BK','KKX_YLBX_MY','KKX_YLBX_BF','KKX_SY_MY','KKX_SY_BF','KKX_GJJ_MY','KKX_GJJ_BF','KKX_GHJF','KKX_SDS','KKX_OLDBX_MY','KKX_OLDBX_BF','KKX_XJ','FSALARY']
        		
        }
        $.post("finance/feeSalaryAction/salaryFeeUnit.do",
        		{"start":startDate,"end":endDate,'dept':dept,'unit':unit,'gzItem':gzItem,'types':typess}, 
        		function(res){
        			//接收数据
		            var data = res.data;
		            //组装数据
		             rows = []; 
    				 names=[];
    				 clsses = [];
    				 depts = [];
		            	for(var k=0;k<data.length;k++){
		            		var row = {};
		            		if(data[k]!=null){
		            			names.push(data[k][0])
		            			
		            			depts[depts.length] = data[k][0];
		            			//alert("-----"+depts[depts.length-1])
			            		row['NAME'] = data[k][0];
		                      	row['JBGZ_GWGZ'] = data[k][1];
			                    row['JBGZ_XJGZ'] = data[k][2];
			                    row['JBGZ_BLBT'] = data[k][3];
			                    row['JBGZ_LT'] = data[k][4];
			                    row['JBGZ_BFX'] = data[k][5];
			                    row['JBGZ_DZ'] = data[k][6];
			                    row['JBGZ_XJ'] = data[k][7];
			                    row['JXGZ_JCJX_MY'] = data[k][8];
			                    row['JXGZ_JCJX_BF'] = data[k][9];
			                    row['JXGZ_JDJX'] = data[k][10];
			                    row['JXGZ_BT_ZCBT'] = data[k][11];
			                    row['JXGZ_BT_QT']= data[k][12];
			                    row['JXGZ_XJ'] = data[k][13];
			                    row['KKX_ZYNJ_MY'] = data[k][14];
			                    row['KKX_ZYNJ_BK'] = data[k][15];
			                    row['KKX_YLBX_MY'] = data[k][16];
			                    row['KKX_YLBX_BF'] = data[k][17];
			                    row['KKX_SY_MY'] = data[k][18];
			                    row['KKX_SY_BF'] = data[k][19]; 
			                    row['KKX_GJJ_MY'] = data[k][20];
			                    row['KKX_GJJ_BF'] = data[k][21];
			                    row['KKX_GHJF'] = data[k][22];
			                    row['KKX_SDS'] = data[k][23];
			                    row['KKX_OLDBX_MY'] = data[k][24];
			                    row['KKX_OLDBX_BF'] = data[k][25];
			                    row['KKX_XJ'] = data[k][26];
			                    row['FSALARY'] = data[k][27];
			                    rows.push(row);
		            		}
		            		
		                }
		            loadGrid(rows,2);
		            drawChart(clsses,rows);
        });
    }
  
    function print_array(arr){  
	    for(var key in arr){  
	        if(typeof(arr[key])=='array'||typeof(arr[key])=='object'){//递归调用    
	            print_array(arr[key]);  
	        }else{  
	            document.write(key + ' = ' + arr[key] + '<br>');  
	        }  
	    }  
	}  
  //生成报表
	function loadGrid(rows,fix){
	  var items = $("#gzItem").ligerGetComboBoxManager().getValue();
	  if(items==""){
		  items =","+ types.toString()+",";
	  }else{
		  items =","+ items +",";
		  
	  }
	  rows = point(rows,2);
		gird = null;
		//定义头部
	    var columns = [];
	    columns.push({
	    	display : '部门', 
	    	name : 'NAME',
	    	align : 'center', 
	    	width : 200,
	    	totalSummary:{
					render : function (e){  
	    			return "<div>合计</div>"; 
	    		} 
	        		}
	    });
	    /* columns.push({display: '年份', 
    		name: 'SALAR_TMONTH',
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
	    columns.push({display: '月份', 
    		name: 'SALART_YEAR',
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
    	});	 */

	    if(((selected!=null&&selected['基本工资岗位工资']==true)||selected==null)&&items.indexOf(",JBGZ_GWGZ,")!=-1){
	    	columns.push({display: '基本工资岗位工资', 
	    		name: 'JBGZ_GWGZ',
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
	    
	    if(((selected!=null&&selected['基本工资薪级工资']==true)||selected==null)&&items.indexOf(",JBGZ_XJGZ,")!=-1){
	    	columns.push({display: '基本工资薪级工资', 
	    		name: 'JBGZ_XJGZ',
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
	    if(((selected!=null&&selected['基本工资保留补贴']==true)||selected==null)&&items.indexOf(",JBGZ_BLBT,")!=-1){
	    	columns.push({display: '基本工资保留补贴', 
	    		name: 'JBGZ_BLBT',
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
	    if(((selected!=null&&selected['基本工资粮贴']==true)||selected==null)&&items.indexOf(",JBGZ_LT,")!=-1){
	    	columns.push({display: '基本工资粮贴', 
	    		name: 'JBGZ_LT',
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
	    if(((selected!=null&&selected['基本工资补发项']==true)||selected==null)&&items.indexOf(",JBGZ_BFX,")!=-1){
	    	columns.push({display: '基本工资补发项', 
	    		name: 'JBGZ_BFX',
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
	    if(((selected!=null&&selected['基本工资独子']==true)||selected==null)&&items.indexOf(",JBGZ_DZ,")!=-1){
	    	columns.push({display: '基本工资独子', 
	    		name: 'JBGZ_DZ',
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
	  
	    if(((selected!=null&&selected['基本工资小计']==true)||selected==null)&&items.indexOf(",JBGZ_XJ,")!=-1){
	    	columns.push({display: '基本工资小计', 
	    		name: 'JBGZ_XJ',
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
	    
	    if(((selected!=null&&selected['绩效工资基础绩效每月']==true)||selected==null)&&items.indexOf(",JXGZ_JCJX_MY,")!=-1){
	    	columns.push({display: '绩效工资基础绩效每月', 
	    		name: 'JXGZ_JCJX_MY',
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
	    
	    if(((selected!=null&&selected['绩效工资基础绩效补发']==true)||selected==null)&&items.indexOf(",JXGZ_JCJX_BF,")!=-1){
	    	columns.push({display: '绩效工资基础绩效补发', 
	    		name: 'JXGZ_JCJX_BF',
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
	    if(((selected!=null&&selected['绩效工资季度绩效']==true)||selected==null)&&items.indexOf(",JXGZ_JDJX,")!=-1){
	    	columns.push({display: '绩效工资季度绩效', 
	    		name: 'JXGZ_JDJX',
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
	    if(((selected!=null&&selected['绩效工资补贴驻场补贴']==true)||selected==null)&&items.indexOf(",JXGZ_BT_ZCBT,")!=-1){
	    	columns.push({display: '绩效工资补贴驻场补贴', 
	    		name: 'JXGZ_BT_ZCBT',
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
	    	if(((selected!=null&&selected['绩效工资补贴其他']==true)||selected==null)&&items.indexOf(",JXGZ_BT_QT,")!=-1){
	    	columns.push({display: '绩效工资补贴其他', 
	    		name: 'JXGZ_BT_QT',
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
	    	if(((selected!=null&&selected['绩效工资备用']==true)||selected==null)&&items.indexOf(",JXGZ_BY,")!=-1){
	    	columns.push({display: '绩效工资备用', 
	    		name: 'JXGZ_BY',
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
	    	if(((selected!=null&&selected['绩效工资小计']==true)||selected==null)&&items.indexOf(",JXGZ_XJ,")!=-1){
	    	columns.push({display: '绩效工资小计', 
	    		name: 'JXGZ_XJ',
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
	    	if(((selected!=null&&selected['扣款项职业年金每月']==true)||selected==null)&&items.indexOf(",KKX_ZYNJ_MY,")!=-1){
	    	columns.push({display: '扣款项职业年金每月', 
	    		name: 'KKX_ZYNJ_MY',
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
	    	});	}
	    	if(((selected!=null&&selected['扣款项职业年金补扣']==true)||selected==null)&&items.indexOf(",KKX_ZYNJ_BK,")!=-1){
	    	columns.push({display: '扣款项职业年金补扣', 
	    		name: 'KKX_ZYNJ_BK',
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
	    	});	}
	    	if(((selected!=null&&selected['扣款项医疗保险每月']==true)||selected==null)&&items.indexOf(",KKX_YLBX_MY,")!=-1){
	    	columns.push({display: '扣款项医疗保险每月', 
	    		name: 'KKX_YLBX_MY',
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
	    	});	}
	    	if(((selected!=null&&selected['扣款项医疗保险补发']==true)||selected==null)&&items.indexOf(",KKX_YLBX_BF,")!=-1){
	    	columns.push({display: '扣款项医疗保险补发', 
	    		name: 'KKX_YLBX_BF',
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
	    	});	}
	    	if(((selected!=null&&selected['扣款项失业每月']==true)||selected==null)&&items.indexOf(",KKX_SY_MY,")!=-1){
	    	columns.push({display: '扣款项失业每月', 
	    		name: 'KKX_SY_MY',
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
	    	});	}
	    	if(((selected!=null&&selected['扣款项失业补发']==true)||selected==null)&&items.indexOf(",KKX_SY_BF,")!=-1){
	    	columns.push({display: '扣款项失业补发', 
	    		name: 'KKX_SY_BF',
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
	    	});	}
	    	if(((selected!=null&&selected['扣款项公积金每月']==true)||selected==null)&&items.indexOf(",KKX_GJJ_MY,")!=-1){
	    	columns.push({display: '扣款项公积金每月', 
	    		name: 'KKX_GJJ_MY',
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
	    	});	}
	    	if(((selected!=null&&selected['扣款项公积金补发']==true)||selected==null)&&items.indexOf(",KKX_GJJ_BF,")!=-1){
	    	columns.push({display: '扣款项公积金补发', 
	    		name: 'KKX_GJJ_BF',
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
	    	});	}
	    	if(((selected!=null&&selected['扣款项工会经费']==true)||selected==null)&&items.indexOf(",KKX_GHJF,")!=-1){
	    	columns.push({display: '扣款项工会经费', 
	    		name: 'KKX_GHJF',
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
	    	});}
	    	if(((selected!=null&&selected['扣款项所得税']==true)||selected==null)&&items.indexOf(",KKX_SDS,")!=-1){
	    	columns.push({display: '扣款项所得税', 
	    		name: 'KKX_SDS',
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
	    	});	}
	    	if(((selected!=null&&selected['扣款项小计']==true)||selected==null)&&items.indexOf(",KKX_XJ,")!=-1){
	    	columns.push({display: '扣款项小计', 
	    		name: 'KKX_XJ',
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
	    	});}
	    	if(((selected!=null&&selected['扣款项养老保险每月']==true)||selected==null)&&items.indexOf(",KKX_OLDBX_MY,")!=-1){
	    	columns.push({display: '扣款项养老保险每月', 
	    		name: 'KKX_OLDBX_MY',
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
	    	});	}
	    	if(((selected!=null&&selected['扣款项养老保险补发']==true)||selected==null)&&items.indexOf(",KKX_OLDBX_BF,")!=-1){
	    	columns.push({display: '扣款项养老保险补发', 
	    		name: 'KKX_OLDBX_BF',
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
	    	});	}
	    	if(((selected!=null&&selected['应发合计']==true)||selected==null)&&items.indexOf(",FSALARY,")!=-1){
	    	columns.push({display: '应发合计', 
	    		name: 'FSALARY',
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
	    //创建表格
		grid = $("#grid").ligerGrid({
								        columns: columns, 
								        data:{Rows:rows},
								        height:'100%',
								        width:'100%',
						                usePager:true,
						                pageSize:"100",//分页页面大小
						                pageSizeOptions:[20,50,100]
								     });
	}
  //导出报表excel
  var hasOut = false;
    function out(){
	  
    	  $("body").mask("正在导出数据,请等待...");
          $("#form1").attr("action","finance/feeSalaryAction/exportSalaryFeeUnit.do"); 
          if(!hasOut){
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
               }else{
            	   $("#form1").append('<input type="hidden" name="items" id="items" value="'+ $("#gzItem").val()+'"/>');
               }
              var itemV = $("#gzItem").ligerGetComboBoxManager().getValue(); 
              $("#form1").append('<input type="hidden" name="gzItemV" id="gzItemV" value="'+itemV+'"/>');
          }else{
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
                $("#items").val(item);
               }else{
            	   $("#items").val($("#gzItem").val());
               }
        	  
        	  var itemV = $("#gzItem").ligerGetComboBoxManager().getValue(); 
        	  $("#gzItemV").val(itemV);
             // $("#form1").append('<input type="hidden" name="gzItemV" id="gzItemV" value="'+itemV+'"/>');
          }
          
          $("#form1").submit();
          hasOut = true;
          $("body").unmask();
	  
    	/*  $("body").mask("正在导出数据,请等待...");
    	 $.post("finance/feeSalaryAction/exportSalaryFeeU.do",{"data":grid.getData().toString()},function(){
    		 
    	 })
        // $("#form1").attr("action","feeStatisticsAction/exportInventory.do"); 
        // $("#form1").submit();
         $("body").unmask(); */
		/*  top.$.dialog({
			width : 500,
			height : 150,
			lock : true,
			title : "导出",
			data : {
				"window" : window,"exportData":grid.getData()
			},
			content : 'url:app/finance/salary/salary_fee_exp2.jsp'
		});  
		 */
		 //exportToExcel();
    }
  
    function exportToExcel() { 
    	var data = grid.getData();
    	
		var winname = window.open('', '_blank',"Excel",'top=10000'); 
		//var strHTML = $("#grid").html(); 
  		winname.document.open('text/html', 'replace'); 
  		var table='<table  width="700" cellpadding="6" cellspacing="0" bgcolor="#cccccc" border="1" id="table">';
  		var type=['基本工资岗位工资','基本工资薪级工资','基本工资保留补贴','基本工资粮贴','基本工资补发项','基本工资独子','基本工资小计','绩效工资基础绩效每月','绩效工资基础绩效补发','绩效工资季度绩效','绩效工资补贴驻场补贴','绩效工资补贴其他','绩效工资小计','扣款项职业年金每月','扣款项职业年金补扣','扣款项医疗保险每月','扣款项医疗保险补发','扣款项失业每月','扣款项失业补发','扣款项公积金每月','扣款项公积金补发','扣款项工会经费','扣款项所得税','扣款项养老保险每月','扣款项养老保险补发','扣款项小计','应发合计']
 		
  		var thead = '<tr><td align="center">部门</td>';
  		for(var i=0;i<type.length;i++){
  			thead = thead + '<td align="center">'+type[i]+'</td>';
  		}
  		thead = thead + '</tr>';
		
  		table = table + thead;
  		//winname.document.writeln(strHTML); 
    	for (var i = 0; i < data.length; i++) {
			var dataa = data[i];
			var ttr = "<tr>"
			for ( var key in dataa) {
				if("nochanged"!=dataa[key]){
					ttr  = ttr + "<td>"+ dataa[key]+"</td>";
				}
				
			}
			ttr  = ttr + "</tr>";
			table = table + ttr;
			//$("#table").append(ttr);
		}
    	table = table + '</table>';
    	
    	winname.document.writeln(table); 
    	//winname.document.execCommand("selectAll")
    	//winname.document.execCommand("saveAs")
     	winname.document.execCommand('saveAs',true,'salary.xls'); 
     	//api.close(); 
  		//closeCurWin();
	}
    
    function changeUnit(val){
    	if(val == "协会"){
    		$("#dept").ligerGetTreeManager().setValue("");
    		$(".dept").hide();
    	}else{
    		$(".dept").show();
    	}
    }
    
    </script>

</head>
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
<input type="hidden" name="org_id1" id="org_id1"/>
<input type="hidden" name="org_id2" id="org_id2"/>
<div class="item-tm">
    <div class="l-page-title2 has-icon has-note" style="height: 100px">
        <div class="l-page-title2-div"></div>
        <div class="l-page-title2-text"><h1>财务工资统计(部门)</h1></div>
        <div class="l-page-title2-note" style="height:20px;">按各类型统计（单位：<span>元</span>）</div>
        <div class="l-page-title2-icon">
        	<img src="k/kui/images/icons/32/statistics.png" border="0"/>
        </div>
        <div class="l-page-title-content" style="top:25px;height:80px;"> 
            <table border="0" cellpadding="0" cellspacing="0" width="">
                <tr>
                <!-- 	 <td width="100" style="text-align: right;width:50px;">单位：</td>
                    <td width="120px">
                       <input id="unit" name="unit"
							type="text" ltype="select" value=""
							ligerui="{isTextBoxMode:true,width:325,
							initValue:'检验院',
							onSelected:changeUnit,
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
                    <td width="70" style="text-align: right;">日期从：</td>
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
                <tr>
                 	<td width="80" style="text-align: right;">工资项目：</td>
                    <td  colspan="7">
                    	 <input id="gzItem" name="gzItem"
							type="text" ltype="select" value=""
							ligerui="{
							initValue:'',
							tree:{checkbox:true,data:<%=gzColum %>,
							isMultiSelect:false}}" />
                    </td>
                </tr>
            </table>
        </div>
    </div>
</div>
</form>
<div id="container" position="center" >
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
 		if(fix == 4){
 			rows = tenThousand(rows);
 		}
 		barChart = echarts.init(div);
 		var ss = [];
 		
 		var colors = [
 			'#0000FF','#3D59AB','#1E90FF','#0B1746','#03A89E',
 			'#191970','#33A1C9','#8A2BE2','#DA70D6','#F0E68C',
 			'#BC8F8F','#C76114','#734A12','#5E2612','#2E8B57',
 			'#00FF7F','#40E0D0','#6A5ACD','#4682B4','#1E90FF',
 			'#483D8B','#CD5555','#FF1493','#8B8989','#CD6889',
 			'#CDB5CD'

 		]

 		if(rows.length>0){
 		for(var h=0;h<type.length;h++){
 			var s = {};
 			s.name = type[h];
			s.type = 'bar';
 			s.stack = '综合';
 			s.yAxisIndex = 0;
 			s.data = [];
 			s.color=colors[h];
 			for(var j=0;j<rows.length;j++){
 				s.data.push(rows[j][types[h]]?rows[j][types[h]]:0);
 			}
 			ss.push(s);
 		}
 		}
 		//合计
 		var tt = [];
 		for(var i=0;i<rows.length;i++){
 			tt.push(rows[i]['total']);
		}
 		ss.push({
 			name : '合计',
	 		type :'line',
			yAxisIndex : 1,
			data : tt
 		});
 		var option = {
 				tooltip : {
 			        trigger: 'axis',
 			        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
 			            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
 			        },
 			       formatter: function (params,ticket,callback) {  
 		                 var res = params[0].axisValue + '<br>';
 		                 for(var i=0;i<params.length;i++){
 		                	if(params[i].value == 0){
 		                		continue;
 		                	}else{
 		                		res += params[i].marker + params[i].seriesName + ':' + params[i].value +'<br>';
 		                	}
 		                 }
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
			        data: type
			    },
 			    grid: {
 			        left: '10%',
 			        right: '11%',
 			        bottom: '6%',
 			        containLabel: true
 			    },
 			    xAxis : [
 			        {
 			            type : 'category',
 			            data : names
 			        }
 			    ],
 			    yAxis : [
		 			        {
		 			            type : 'value',
		 			            name: '类别',
		 			            position: 'left'
		 			        },
		 			        {
		 			            type: 'value',
		 			            name: '合计',
		 			            position: 'right',
		 			            offset: 80
		 			        }
		 			    ],
 			    series : ss
 		    };

 		   //  if($("#dept").ligerGetComboBoxManager().getValue()==""){
	 		    option = {
	 				tooltip : {
	 			        trigger: 'axis',
	 			        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
	 			            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
	 			        },
	 			       formatter: function (params,ticket,callback) {  
	 		                 var res = params[0].axisValue + '<br>';
	 		                 for(var i=0;i<params.length;i++){
	 		                	if(params[i].value == 0){
	 		                		continue;
	 		                	}else{
	 		                		res += params[i].marker + params[i].seriesName + ':' + params[i].value +'<br>';
	 		                	}
	 		                 }
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
				        data: type
				    },
	 			    grid: {
	 			        left: '10%',
	 			        right: '11%',
	 			        bottom: '6%',
	 			        containLabel: true
	 			    },
	 			    xAxis : [
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
	 		    	            "data": depts,
	 		    	        }
	 			    ],
	 			    yAxis : [
			 			        {
			 			            type : 'value',
			 			            name: '类别',
			 			            position: 'left'
			 			        },
			 			        {
			 			            type: 'value',
			 			            name: '合计',
			 			            position: 'right',
			 			            offset: 80
			 			        }
			 			    ],dataZoom: [
	 			             {
	 			                 show: true,
	 			                 start: 0,
	 			                 end:100,
	 			                 //end: rows.length>12?60:100,
	 			                 top: '95%'
	 			             },
	 			             {
	 			                 type: 'inside',
	 			                 start: 0,
	 			                 end: rows.length>12?60:100,
	 			             }
	 			         ],
	 			    series : ss
	 		    };
	 		    	
 		   // }
 		barChart.setOption(option);
 		barChart.on('click', function (param) {
 			return;
 			//弹框加事件
 			$(".white_content").addClass("md-show");
    		$("#light,#tccontent").show();
    		var ligh=$(window).height()*0.8;
    		$(".tankuang").css({"height":ligh+"px"}); 
    		$("#fade").show();
    		//画出饼图
    		for(var i=0;i<rows.length;i++){
    		//alert(rows[i].NAME+ '-----'+ param.name)
 				if(rows[i].NAME == param.name){
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
 			//alert(k+"----"+v)
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
 		var el = $(".tankuang")[0];
 		pie = echarts.init(el);
 		var option = {
 			    title : {
 			        text: row.NAME,
 			        subtext: '部门各经济类型报销费用',
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
 			            name: '经济类型',
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
 	}
</script>
</body>
</html>